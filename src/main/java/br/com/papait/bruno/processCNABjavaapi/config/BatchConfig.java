package br.com.papait.bruno.processCNABjavaapi.config;

import br.com.papait.bruno.processCNABjavaapi.domain.model.Transacao;
import br.com.papait.bruno.processCNABjavaapi.domain.model.TransacaoCNAB;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.math.BigDecimal;

@Configuration
public class BatchConfig {

  private final PlatformTransactionManager platformTransactionManager;
  private final JobRepository jobRepository;

  public BatchConfig(PlatformTransactionManager platformTransactionManager, JobRepository jobRepository) {
    this.platformTransactionManager = platformTransactionManager;
    this.jobRepository = jobRepository;
  }

  @Bean
  public Job job(final Step step) {
    return new JobBuilder("job", this.jobRepository)
        .start(step)
        .build();
  }

  @Bean
  Step step(
      FlatFileItemReader<TransacaoCNAB> reader,
      ItemProcessor<TransacaoCNAB, Transacao> processor,
      ItemWriter<Transacao> writer) {
    return new StepBuilder("step", this.jobRepository)
        .<TransacaoCNAB, Transacao>chunk(1000, this.platformTransactionManager)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .build();
  }

  @Bean
  public FlatFileItemReader<TransacaoCNAB> reader() {
    return new FlatFileItemReaderBuilder<TransacaoCNAB>()
        .name("reader")
        .resource(new FileSystemResource("files/CNAB.txt"))
        .fixedLength()
        .columns(
            new Range(1, 1), new Range(2, 9),
            new Range(10, 19), new Range(20, 30),
            new Range(31, 42), new Range(43, 48),
            new Range(49, 62), new Range(63, 80)
        )
        .names("tipo", "data", "valor", "cpf", "cartao", "hora", "donoDaLoja", "nomeDaLoja")
        .targetType(TransacaoCNAB.class)
        .build();
  }

  @Bean
  public ItemProcessor<TransacaoCNAB, Transacao> processor() {
    return item -> {
      var transacao = new Transacao(
          null,
          item.tipo(),
          null,
          null,
          item.cpf(),
          item.cartao(),
          null,
          item.donoDaLoja().trim(),
          item.nomeDaLoja().trim()
      )
          .withValor(item.valor().divide(BigDecimal.valueOf(100)))
          .withData(item.data())
          .withHora(item.hora());

      return transacao;
    };
  }

  @Bean
  public JdbcBatchItemWriter<Transacao> writer(final DataSource dataSource) {
    return new JdbcBatchItemWriterBuilder<Transacao>()
        .dataSource(dataSource)
        .sql("""
            INSERT INTO transacao (tipo, data, valor, cpf, cartao, hora, dono_loja, nome_loja)
            VALUES (:tipo, :data, :valor, :cpf, :cartao, :hora, :donoLoja, :nomeLoja)
            """)
        .beanMapped()
        .build();
  }
}

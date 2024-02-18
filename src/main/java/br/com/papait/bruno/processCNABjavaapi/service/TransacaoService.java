package br.com.papait.bruno.processCNABjavaapi.service;

import br.com.papait.bruno.processCNABjavaapi.entity.TipoTransacao;
import br.com.papait.bruno.processCNABjavaapi.entity.TransacaoReport;
import br.com.papait.bruno.processCNABjavaapi.repository.TransacaoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class TransacaoService {

  private final TransacaoRepository transacaoRepository;

  public TransacaoService(TransacaoRepository transacaoRepository) {
    this.transacaoRepository = transacaoRepository;
  }

  public List<TransacaoReport> listTotaisTransacoesPorNomeLoja() {
    var transacoes = this.transacaoRepository.findAllByOrderByNomeLojaAscIdDesc();
    var reportMap = new LinkedHashMap<String, TransacaoReport>();

    transacoes.forEach(transacao -> {
      var nomeLoja = transacao.nomeLoja();
      var tipoTransacao = TipoTransacao.findByTipo(transacao.tipo());
      var valor = transacao.valor().multiply(tipoTransacao.getSinal());

      reportMap.compute(nomeLoja, (key, existingReport) -> {
        var report = (existingReport != null) ?
            existingReport :
            new TransacaoReport(key, BigDecimal.ZERO, new ArrayList<>());

        return report.addTotal(valor).addTransacao(transacao.withValor(valor));
      });
    });

    return new ArrayList<>(reportMap.values());
  }
}

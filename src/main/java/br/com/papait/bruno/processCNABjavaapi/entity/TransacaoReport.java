package br.com.papait.bruno.processCNABjavaapi.entity;

import java.math.BigDecimal;
import java.util.List;

public record TransacaoReport(
    String nomeLoja,
    BigDecimal total,
    List<Transacao> transacoes
) {
  public TransacaoReport addTotal(BigDecimal valor) {
    return new TransacaoReport(this.nomeLoja(), this.total.add(valor), this.transacoes);
  }

  public TransacaoReport addTransacao(Transacao transacao) {
    this.transacoes.add(transacao);
    return new TransacaoReport(this.nomeLoja(), this.total, this.transacoes);
  }
}

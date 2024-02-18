package br.com.papait.bruno.processCNABjavaapi.entity;

import java.math.BigDecimal;
import java.util.List;

public record TransacaoReport(
    String nomeLoja,
    BigDecimal total,
    List<Transacao> transacoes
) {
}

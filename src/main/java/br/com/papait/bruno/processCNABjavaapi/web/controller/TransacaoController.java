package br.com.papait.bruno.processCNABjavaapi.web.controller;

import br.com.papait.bruno.processCNABjavaapi.entity.Transacao;
import br.com.papait.bruno.processCNABjavaapi.entity.TransacaoReport;
import br.com.papait.bruno.processCNABjavaapi.service.TransacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

  private final TransacaoService transacaoService;

  public TransacaoController(TransacaoService transacaoService) {
    this.transacaoService = transacaoService;
  }

  @GetMapping
  public ResponseEntity<List<TransacaoReport>> getAll() {
    return ResponseEntity.ok().body(this.transacaoService.listTotaisTransacoesPorNomeLoja());
  }
}

package br.com.papait.bruno.processCNABjavaapi.repository;

import br.com.papait.bruno.processCNABjavaapi.entity.Transacao;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransacaoRepository extends CrudRepository<Transacao, Long> {

  // SELECT * FROM TRANSACAO ORDER BY NOME_LOJA ASC, ID DESC
  List<Transacao> findAllByOrderByNomeLojaAscIdDesc();
}

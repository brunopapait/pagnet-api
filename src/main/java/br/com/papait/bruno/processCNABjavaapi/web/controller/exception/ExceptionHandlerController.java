package br.com.papait.bruno.processCNABjavaapi.web.controller.exception;

import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

  @ExceptionHandler(JobInstanceAlreadyCompleteException.class)
  public ResponseEntity<Object> handleFileAlreadyImported() {
    return ResponseEntity.status(HttpStatus.CONFLICT).body("O arquivo ja foi importado no sistema!");
  }
}

package br.com.papait.bruno.processCNABjavaapi.web.controller;

import br.com.papait.bruno.processCNABjavaapi.service.CNABService;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/cnab")
public class CNABController {

  private final CNABService cnabService;

  public CNABController(CNABService cnabService) {
    this.cnabService = cnabService;
  }

  @PostMapping("/upload")
  public String upload(@RequestParam("file") MultipartFile file) throws IOException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
    this.cnabService.uploadCnabFile(file);
    return "Processamento iniciado !";
  }
}

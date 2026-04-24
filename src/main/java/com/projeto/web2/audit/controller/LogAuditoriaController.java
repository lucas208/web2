package com.projeto.web2.audit.controller;

import com.projeto.web2.audit.model.LogAuditoria;
import com.projeto.web2.audit.service.LogAuditoriaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/logs")
public class LogAuditoriaController {

    private final LogAuditoriaService logAuditoriaService;

    public LogAuditoriaController(LogAuditoriaService logAuditoriaService) {
        this.logAuditoriaService = logAuditoriaService;
    }

    @GetMapping
    public List<LogAuditoria> listar() {
        return logAuditoriaService.listar();
    }
}

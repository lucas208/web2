package com.projeto.web2.audit.service;

import com.projeto.web2.audit.model.LogAuditoria;
import com.projeto.web2.audit.repository.LogAuditoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogAuditoriaServiceImpl implements LogAuditoriaService {

    private final LogAuditoriaRepository repository;

    public LogAuditoriaServiceImpl(LogAuditoriaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<LogAuditoria> listar() {
        return repository.findAll();
    }
}

package com.projeto.web2.config;

import com.projeto.web2.model.Fornecedor;
import com.projeto.web2.repository.FornecedorRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final FornecedorRepository fornecedorRepository;

    public DataInitializer(FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    @PostConstruct
    public void init() {

        if (fornecedorRepository.count() == 0) {

            Fornecedor f1 = new Fornecedor(null, "Fornecedor A", "12345678000100", null);
            Fornecedor f2 = new Fornecedor(null, "Fornecedor B", "98765432000100", null);

            fornecedorRepository.save(f1);
            fornecedorRepository.save(f2);

            System.out.println(">>> Fornecedores iniciais cadastrados!");
        }
    }
}

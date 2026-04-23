package com.projeto.web2.dto.fornecedor;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FornecedorResponseDTO {

    private Long id;
    private String nome;
    private String cnpj;

    private List<Long> equipamentosIds;
}

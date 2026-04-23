package com.projeto.web2.dto.projeto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class ProjetoResponseDTO {

    private Long id;
    private String nome;
    private String descricao;

    private Set<Long> equipamentosIds;
}

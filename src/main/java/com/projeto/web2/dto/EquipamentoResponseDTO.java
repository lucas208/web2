package com.projeto.web2.dto;

import com.projeto.web2.model.Categoria;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor
public class EquipamentoResponseDTO {
    private Long id;
    private String nome;
    private Double valor;
    private Categoria categoria;
    private Integer quantidade;

    private Long fornecedorId;
    private List<Long> manutencoesIds;
    private Set<Long> projetosIds;

}

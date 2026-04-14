package com.projeto.web2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Equipamento {
    private Long id;
    private String nome;
    private Double valor;
    private Categoria categoria;
    private Integer quantidade;
}

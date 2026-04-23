package com.projeto.web2.dto.manutencao;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ManutencaoResponseDTO {

    private Long id;
    private String descricao;
    private LocalDate data;
    private Double custo;

    private Long equipamentoId;
}

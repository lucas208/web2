package com.projeto.web2.dto.manutencao;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ManutencaoRequestDTO {

    @NotBlank(message = "Descrição é obrigatória")
    @Size(min = 3, message = "Descrição deve ter pelo menos 3 caracteres")
    private String descricao;

    @NotNull(message = "Data é obrigatória")
    private LocalDate data;

    @NotNull(message = "Custo é obrigatório")
    @Min(value = 0, message = "Custo não pode ser negativo")
    private Double custo;

    @NotNull(message = "Equipamento é obrigatório")
    private Long equipamentoId;
}

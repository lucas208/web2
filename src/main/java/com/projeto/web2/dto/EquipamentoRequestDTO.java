package com.projeto.web2.dto;

import com.projeto.web2.model.Categoria;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EquipamentoRequestDTO {

    @NotBlank
    private String nome;

    @NotNull
    @Min(0)
    private Double valor;

    @NotNull
    private Categoria categoria;

    @NotNull
    @Min(0)
    private Integer quantidade;
}

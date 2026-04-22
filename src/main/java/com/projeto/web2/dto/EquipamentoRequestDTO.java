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

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "Valor é obrigatório")
    @Min(value = 1, message = "Valor deve ser maior que zero")
    private Double valor;

    @NotNull(message = "Categoria é obrigatória")
    private Categoria categoria;

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 0, message = "Quantidade não pode ser negativa")
    private Integer quantidade;

    @NotNull(message = "Fornecedor é obrigatório")
    private Long fornecedorId;
}

package com.projeto.web2.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Equipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private Double valor;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    private Integer quantidade;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id")
    private Fornecedor fornecedor;

    @Builder.Default
    @OneToMany(mappedBy = "equipamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Manutencao> manutencoes = new ArrayList<>();

    @Builder.Default
    @ManyToMany
    @JoinTable(name = "equipamento_projeto",
        joinColumns = @JoinColumn(name = "equipamento_id"),
        inverseJoinColumns = @JoinColumn(name = "projeto_id")
    )
    private Set<Projeto> projetos = new HashSet<>();
}

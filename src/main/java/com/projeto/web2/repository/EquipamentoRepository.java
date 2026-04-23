package com.projeto.web2.repository;

import com.projeto.web2.model.Categoria;
import com.projeto.web2.model.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

    List<Equipamento> findByCategoria(Categoria categoria);

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);

    @Query("""
        SELECT COALESCE(SUM(e.quantidade), 0)
        FROM Equipamento e
        WHERE (:idIgnorado IS NULL OR e.id <> :idIgnorado)
    """)
    int somarQuantidade(Long idIgnorado);

    @Query(value = """
        SELECT COUNT(*) 
        FROM equipamento
        WHERE fornecedor_id = :fornecedorId
        AND (:idIgnorado IS NULL OR id <> :idIgnorado)
    """, nativeQuery = true)
    int contarPorFornecedorIgnorandoId(@Param("fornecedorId") Long fornecedorId, @Param("idIgnorado") Long idIgnorado);

    @Query("""
        SELECT DISTINCT e FROM Equipamento e
        LEFT JOIN FETCH e.fornecedor
        LEFT JOIN FETCH e.manutencoes
        LEFT JOIN FETCH e.projetos
        WHERE e.id = :id
    """)
    Optional<Equipamento> buscarCompletoPorId(@Param("id") Long id);
}

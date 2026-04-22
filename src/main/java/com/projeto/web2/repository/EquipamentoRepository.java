package com.projeto.web2.repository;

import com.projeto.web2.model.Categoria;
import com.projeto.web2.model.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

    List<Equipamento> findByCategoria(Categoria categoria);

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);
}

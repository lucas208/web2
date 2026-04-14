package com.projeto.web2.repository;

import com.projeto.web2.model.Categoria;
import com.projeto.web2.model.Equipamento;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class EquipamentoRepository {

    private final Map<Long, Equipamento> banco = new HashMap<>();
    private final AtomicLong contadorId = new AtomicLong(1);

    public Equipamento salvar(Equipamento equipamento) {
        if(equipamento.getId() == null) {
            equipamento.setId(contadorId.getAndIncrement());
        }
        banco.put(equipamento.getId(), equipamento);
        return equipamento;
    }

    public List<Equipamento> buscarTodos(){
        return new ArrayList<>(banco.values());
    }

    public Optional<Equipamento> buscarPorId(Long id) {
        return Optional.ofNullable(banco.get(id));
    }

    public void removerPorId(Long id) {
        banco.remove(id);
    }

    public List<Equipamento> buscarPorCategoria(Categoria categoria) {
        return banco.values()
            .stream()
            .filter(e -> e.getCategoria() == categoria)
            .collect(Collectors.toList());
    }

}

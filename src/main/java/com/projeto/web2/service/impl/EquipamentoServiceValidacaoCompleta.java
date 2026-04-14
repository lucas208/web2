package com.projeto.web2.service.impl;

import com.projeto.web2.dto.EquipamentoRequestDTO;
import com.projeto.web2.dto.EquipamentoResponseDTO;
import com.projeto.web2.exception.RegraNegocioException;
import com.projeto.web2.model.Categoria;
import com.projeto.web2.model.Equipamento;
import com.projeto.web2.repository.EquipamentoRepository;
import com.projeto.web2.service.EquipamentoService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Qualifier("completa")
public class EquipamentoServiceValidacaoCompleta implements EquipamentoService {

    private final EquipamentoRepository equipamentoRepository;

    public EquipamentoServiceValidacaoCompleta(EquipamentoRepository equipamentoRepository) {
        this.equipamentoRepository = equipamentoRepository;
    }

    @Override
    public EquipamentoResponseDTO criar(EquipamentoRequestDTO dto) {
        Equipamento e = toEntity(dto);

        validarRegra(e);

        Equipamento salvo = equipamentoRepository.salvar(e);

        return toResponse(salvo);
    }

    @Override
    public List<EquipamentoResponseDTO> listar() {
        return equipamentoRepository.buscarTodos()
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public EquipamentoResponseDTO buscarPorId(Long id) {
        Equipamento e = equipamentoRepository.buscarPorId(id).orElseThrow(() -> new RuntimeException("Equipamento não encontrado"));
        return toResponse(e);
    }

    @Override
    public EquipamentoResponseDTO atualizarPorId(Long id, EquipamentoRequestDTO dto) {
        Equipamento e = equipamentoRepository.buscarPorId(id).orElseThrow(() -> new RuntimeException("Equipamento não encontrado"));

        e.setNome(dto.getNome());
        e.setValor(dto.getValor());
        e.setCategoria(dto.getCategoria());
        e.setQuantidade(dto.getQuantidade());

        validarRegra(e);

        return toResponse(equipamentoRepository.salvar(e));
    }

    @Override
    public void removerPorId(Long id) {
        equipamentoRepository.removerPorId(id);
    }

    private void validarRegra(Equipamento e) {
        if (e.getValor() <= 0) {
            throw new RegraNegocioException("Valor deve ser maior que zero");
        }

        if (e.getQuantidade() < 0) {
            throw new RegraNegocioException("Quantidade não pode ser negativa");
        }

        if (e.getCategoria() == Categoria.INDUSTRIAL && e.getValor() < 500) {
            throw new RegraNegocioException("Equipamento industrial deve ter valor mínimo de 500");
        }

        if (e.getCategoria() == Categoria.ELETRONICO && e.getQuantidade() < 5) {
            throw new RegraNegocioException("Equipamento eletrônico deve ter estoque mínimo de 5 unidades");
        }

        if (e.getValor() > 10000) {
            throw new RegraNegocioException("Valor acima de 10000 requer aprovação especial");
        }
    }

    private Equipamento toEntity(EquipamentoRequestDTO dto) {
        return new Equipamento(
            null,
            dto.getNome(),
            dto.getValor(),
            dto.getCategoria(),
            dto.getQuantidade()
        );
    }

    private EquipamentoResponseDTO toResponse(Equipamento e) {
        return new EquipamentoResponseDTO(
            e.getId(),
            e.getNome(),
            e.getValor(),
            e.getCategoria(),
            e.getQuantidade()
        );
    }
}

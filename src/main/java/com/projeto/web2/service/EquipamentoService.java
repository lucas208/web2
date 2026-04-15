package com.projeto.web2.service;

import com.projeto.web2.dto.EquipamentoRequestDTO;
import com.projeto.web2.dto.EquipamentoResponseDTO;
import com.projeto.web2.model.Categoria;

import java.util.List;

public interface EquipamentoService {

    EquipamentoResponseDTO criar(EquipamentoRequestDTO dto);

    List<EquipamentoResponseDTO> listar();

    EquipamentoResponseDTO buscarPorId(Long id);

    EquipamentoResponseDTO atualizarPorId(Long id, EquipamentoRequestDTO dto);

    void removerPorId(Long id);

    List<EquipamentoResponseDTO> buscarPorCategoria(Categoria categoria);
}

package com.projeto.web2.service;

import com.projeto.web2.dto.EquipamentoRequestDTO;
import com.projeto.web2.dto.EquipamentoResponseDTO;

import java.util.List;

public interface EquipamentoService {

    EquipamentoResponseDTO criar(EquipamentoRequestDTO dto);

    List<EquipamentoResponseDTO> listar();

    EquipamentoResponseDTO buscarPorId(Long id);

    EquipamentoResponseDTO atualizarPorId(Long id, EquipamentoRequestDTO dto);

    void removerPorId(Long id);
}

package com.projeto.web2.service.projeto;

import com.projeto.web2.dto.projeto.ProjetoRequestDTO;
import com.projeto.web2.dto.projeto.ProjetoResponseDTO;

import java.util.List;

public interface ProjetoService {

    ProjetoResponseDTO criar(ProjetoRequestDTO dto);

    List<ProjetoResponseDTO> listar();

    ProjetoResponseDTO buscarPorId(Long id);

    ProjetoResponseDTO atualizarPorId(Long id, ProjetoRequestDTO dto);

    void removerPorId(Long id);
}

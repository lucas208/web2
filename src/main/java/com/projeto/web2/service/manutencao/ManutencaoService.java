package com.projeto.web2.service.manutencao;

import com.projeto.web2.dto.manutencao.ManutencaoRequestDTO;
import com.projeto.web2.dto.manutencao.ManutencaoResponseDTO;

import java.util.List;

public interface ManutencaoService {

    ManutencaoResponseDTO criar(ManutencaoRequestDTO dto);

    List<ManutencaoResponseDTO> listar();

    ManutencaoResponseDTO buscarPorId(Long id);

    ManutencaoResponseDTO atualizarPorId(Long id, ManutencaoRequestDTO dto);

    void removerPorId(Long id);
}

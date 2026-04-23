package com.projeto.web2.service.fornecedor;

import com.projeto.web2.dto.fornecedor.FornecedorRequestDTO;
import com.projeto.web2.dto.fornecedor.FornecedorResponseDTO;

import java.util.List;

public interface FornecedorService {

    FornecedorResponseDTO criar(FornecedorRequestDTO dto);

    List<FornecedorResponseDTO> listar();

    FornecedorResponseDTO buscarPorId(Long id);

    FornecedorResponseDTO atualizarPorId(Long id, FornecedorRequestDTO dto);

    void removerPorId(Long id);
}

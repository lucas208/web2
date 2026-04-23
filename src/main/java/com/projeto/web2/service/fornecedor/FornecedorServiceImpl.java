package com.projeto.web2.service.fornecedor;

import com.projeto.web2.dto.fornecedor.FornecedorRequestDTO;
import com.projeto.web2.dto.fornecedor.FornecedorResponseDTO;
import com.projeto.web2.exception.RegraNegocioException;
import com.projeto.web2.model.Equipamento;
import com.projeto.web2.model.Fornecedor;
import com.projeto.web2.repository.FornecedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FornecedorServiceImpl implements FornecedorService {

    private final FornecedorRepository fornecedorRepository;

    public FornecedorServiceImpl(FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    @Override
    public FornecedorResponseDTO criar(FornecedorRequestDTO dto) {
        Fornecedor fornecedor = toEntity(dto);
        Fornecedor salvo = fornecedorRepository.save(fornecedor);
        return toResponse(salvo);
    }

    @Override
    public List<FornecedorResponseDTO> listar() {
        return fornecedorRepository.findAll()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    @Override
    public FornecedorResponseDTO buscarPorId(Long id) {
        Fornecedor fornecedor = fornecedorRepository.findById(id)
            .orElseThrow(() -> new RegraNegocioException("Fornecedor não encontrado"));
        return toResponse(fornecedor);
    }

    @Override
    public FornecedorResponseDTO atualizarPorId(Long id, FornecedorRequestDTO dto) {
        Fornecedor fornecedor = fornecedorRepository.findById(id)
            .orElseThrow(() -> new RegraNegocioException("Fornecedor não encontrado"));

        fornecedor.setNome(dto.getNome());
        fornecedor.setCnpj(dto.getCnpj());

        return toResponse(fornecedorRepository.save(fornecedor));
    }

    @Override
    public void removerPorId(Long id) {
        fornecedorRepository.deleteById(id);
    }

    private Fornecedor toEntity(FornecedorRequestDTO dto) {
        return new Fornecedor(
            null,
            dto.getNome(),
            dto.getCnpj(),
            null
        );
    }

    private FornecedorResponseDTO toResponse(Fornecedor f) {
        return new FornecedorResponseDTO(
            f.getId(),
            f.getNome(),
            f.getCnpj(),
            f.getEquipamentos() != null
                ? f.getEquipamentos().stream()
                .map(Equipamento::getId)
                .toList()
                : List.of()
        );
    }
}

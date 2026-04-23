package com.projeto.web2.service.equipamento;

import com.projeto.web2.dto.equipamento.EquipamentoRequestDTO;
import com.projeto.web2.dto.equipamento.EquipamentoResponseDTO;
import com.projeto.web2.exception.RegraNegocioException;
import com.projeto.web2.model.*;
import com.projeto.web2.repository.EquipamentoRepository;
import com.projeto.web2.repository.FornecedorRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Qualifier("completa")
public class EquipamentoServiceValidacaoCompleta implements EquipamentoService {

    private final EquipamentoRepository equipamentoRepository;

    private final FornecedorRepository fornecedorRepository;

    public EquipamentoServiceValidacaoCompleta(EquipamentoRepository equipamentoRepository, FornecedorRepository fornecedorRepository) {
        this.equipamentoRepository = equipamentoRepository;
        this.fornecedorRepository = fornecedorRepository;
    }

    @Override
    public EquipamentoResponseDTO criar(EquipamentoRequestDTO dto) {
        Equipamento e = toEntity(dto);

        validarRegra(e, null);

        Equipamento salvo = equipamentoRepository.save(e);

        return toResponse(salvo);
    }

    @Override
    public List<EquipamentoResponseDTO> listar() {
        return equipamentoRepository.findAll()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    @Override
    public EquipamentoResponseDTO buscarPorId(Long id) {
        Equipamento e = equipamentoRepository.buscarCompletoPorId(id).orElseThrow(() -> new RegraNegocioException("Equipamento não encontrado"));
        return toResponse(e);
    }

    @Override
    public EquipamentoResponseDTO atualizarPorId(Long id, EquipamentoRequestDTO dto) {
        Equipamento e = equipamentoRepository.findById(id).orElseThrow(() -> new RegraNegocioException("Equipamento não encontrado"));

        e.setNome(dto.getNome());
        e.setValor(dto.getValor());
        e.setCategoria(dto.getCategoria());
        e.setQuantidade(dto.getQuantidade());

        validarRegra(e, id);

        return toResponse(equipamentoRepository.save(e));
    }

    @Override
    public void removerPorId(Long id) {
        equipamentoRepository.deleteById(id);
    }

    @Override
    public List<EquipamentoResponseDTO> buscarPorCategoria(Categoria categoria) {
        return equipamentoRepository.findByCategoria(categoria)
            .stream()
            .map(this::toResponse)
            .toList();
    }

    private void validarRegra(Equipamento e, Long idIgnorado) {
        int total = equipamentoRepository.somarQuantidade(idIgnorado);

        if (total + e.getQuantidade() > 1000) {
            throw new RegraNegocioException("Limite total de estoque excedido");
        }

        boolean nomeDuplicado = (idIgnorado == null)
                ? equipamentoRepository.existsByNomeIgnoreCase(e.getNome())
                : equipamentoRepository.existsByNomeIgnoreCaseAndIdNot(e.getNome(), idIgnorado);

        if (nomeDuplicado) {
            throw new RegraNegocioException("Já existe um equipamento com esse nome");
        }

        if (Categoria.INDUSTRIAL.equals(e.getCategoria()) && e.getValor() < 500) {
            throw new RegraNegocioException("Equipamento industrial deve ter valor mínimo de 500");
        }

        if (Categoria.ELETRONICO.equals(e.getCategoria()) && e.getQuantidade() < 5) {
            throw new RegraNegocioException("Equipamento eletrônico deve ter estoque mínimo de 5 unidades");
        }

        if (e.getValor() > 10000) {
            throw new RegraNegocioException("Valor acima de 10000 requer aprovação especial");
        }

        if (e.getFornecedor() == null || e.getFornecedor().getId() == null) {
            throw new RegraNegocioException("Fornecedor é obrigatório");
        }

        int totalFornecedor = equipamentoRepository.contarPorFornecedorIgnorandoId(e.getFornecedor().getId(), idIgnorado);

        if (totalFornecedor >= 50) {
            throw new RegraNegocioException("Fornecedor já atingiu o limite de equipamentos cadastrados");
        }
    }

    private Equipamento toEntity(EquipamentoRequestDTO dto) {
        Fornecedor fornecedor = fornecedorRepository.findById(dto.getFornecedorId())
            .orElseThrow(() -> new RegraNegocioException("Fornecedor não encontrado"));

        return Equipamento.builder()
            .nome(dto.getNome())
            .valor(dto.getValor())
            .categoria(dto.getCategoria())
            .quantidade(dto.getQuantidade())
            .fornecedor(fornecedor)
            .build();
    }

    private EquipamentoResponseDTO toResponse(Equipamento e) {
        return new EquipamentoResponseDTO(
            e.getId(),
            e.getNome(),
            e.getValor(),
            e.getCategoria(),
            e.getQuantidade(),
            e.getFornecedor() != null ? e.getFornecedor().getId() : null,
            e.getManutencoes() != null
                ? e.getManutencoes().stream()
                .map(Manutencao::getId)
                .distinct()
                .toList()
                : List.of(),
            e.getProjetos() != null
                ? e.getProjetos().stream()
                .map(Projeto::getId)
                .collect(Collectors.toSet())
                : Set.of()
        );
    }
}

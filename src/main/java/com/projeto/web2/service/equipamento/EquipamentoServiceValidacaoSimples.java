package com.projeto.web2.service.equipamento;

import com.projeto.web2.audit.model.LogAuditoria;
import com.projeto.web2.audit.repository.LogAuditoriaRepository;
import com.projeto.web2.dto.equipamento.EquipamentoRequestDTO;
import com.projeto.web2.dto.equipamento.EquipamentoResponseDTO;
import com.projeto.web2.exception.RegraNegocioException;
import com.projeto.web2.model.*;
import com.projeto.web2.repository.EquipamentoRepository;
import com.projeto.web2.repository.FornecedorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Qualifier("simples")
public class EquipamentoServiceValidacaoSimples implements EquipamentoService {

    private final EquipamentoRepository equipamentoRepository;

    private final FornecedorRepository fornecedorRepository;

    private final LogAuditoriaRepository logAuditoriaRepository;

    public EquipamentoServiceValidacaoSimples(EquipamentoRepository equipamentoRepository, FornecedorRepository fornecedorRepository, LogAuditoriaRepository logAuditoriaRepository) {
        this.equipamentoRepository = equipamentoRepository;
        this.fornecedorRepository = fornecedorRepository;
        this.logAuditoriaRepository = logAuditoriaRepository;
    }

    @Override
    public EquipamentoResponseDTO criar(EquipamentoRequestDTO dto) {
        Equipamento e = toEntity(dto);

        validarRegra(e, null);

        Equipamento salvo = equipamentoRepository.save(e);

        LogAuditoria log = new LogAuditoria();
        log.setEntidade("Equipamento");
        log.setEntidadeId(salvo.getId());
        log.setDataHora(LocalDateTime.now());

        logAuditoriaRepository.save(log);

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
        Equipamento e = equipamentoRepository.buscarCompletoPorId(id).orElseThrow(() -> new EntityNotFoundException("Equipamento não encontrado"));
        return toResponse(e);
    }

    @Override
    public EquipamentoResponseDTO atualizarPorId(Long id, EquipamentoRequestDTO dto) {
        Equipamento e = equipamentoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Equipamento não encontrado"));

        e.setNome(dto.getNome());
        e.setValor(dto.getValor());
        e.setCategoria(dto.getCategoria());
        e.setQuantidade(dto.getQuantidade());

        validarRegra(e, id);

        return toResponse(equipamentoRepository.save(e));
    }

    @Override
    public void removerPorId(Long id) {
        if (!equipamentoRepository.existsById(id)) {
            throw new EntityNotFoundException("Equipamento não encontrado");
        }
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
    }

    private Equipamento toEntity(EquipamentoRequestDTO dto) {
        Fornecedor fornecedor = fornecedorRepository.findById(dto.getFornecedorId())
            .orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado"));

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

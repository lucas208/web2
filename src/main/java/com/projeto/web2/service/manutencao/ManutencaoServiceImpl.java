package com.projeto.web2.service.manutencao;

import com.projeto.web2.audit.model.LogAuditoria;
import com.projeto.web2.audit.repository.LogAuditoriaRepository;
import com.projeto.web2.dto.manutencao.ManutencaoRequestDTO;
import com.projeto.web2.dto.manutencao.ManutencaoResponseDTO;
import com.projeto.web2.exception.RegraNegocioException;
import com.projeto.web2.model.Equipamento;
import com.projeto.web2.model.Manutencao;
import com.projeto.web2.repository.EquipamentoRepository;
import com.projeto.web2.repository.ManutencaoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ManutencaoServiceImpl implements ManutencaoService {

    private final ManutencaoRepository manutencaoRepository;

    private final EquipamentoRepository equipamentoRepository;

    private final LogAuditoriaRepository logAuditoriaRepository;

    public ManutencaoServiceImpl(ManutencaoRepository manutencaoRepository, EquipamentoRepository equipamentoRepository, LogAuditoriaRepository logAuditoriaRepository) {
        this.manutencaoRepository = manutencaoRepository;
        this.equipamentoRepository = equipamentoRepository;
        this.logAuditoriaRepository = logAuditoriaRepository;
    }

    @Override
    public ManutencaoResponseDTO criar(ManutencaoRequestDTO dto) {
        Manutencao m = toEntity(dto);
        Manutencao salvo = manutencaoRepository.save(m);

        LogAuditoria log = new LogAuditoria();
        log.setEntidade("Manutenção");
        log.setEntidadeId(salvo.getId());
        log.setDataHora(LocalDateTime.now());

        logAuditoriaRepository.save(log);

        return toResponse(salvo);
    }

    @Override
    public List<ManutencaoResponseDTO> listar() {
        return manutencaoRepository.findAll()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    @Override
    public ManutencaoResponseDTO buscarPorId(Long id) {
        Manutencao m = manutencaoRepository.findById(id)
            .orElseThrow(() -> new RegraNegocioException("Manutenção não encontrada"));
        return toResponse(m);
    }

    @Override
    public ManutencaoResponseDTO atualizarPorId(Long id, ManutencaoRequestDTO dto) {
        Manutencao m = manutencaoRepository.findById(id)
            .orElseThrow(() -> new RegraNegocioException("Manutenção não encontrada"));

        Equipamento equipamento = equipamentoRepository.findById(dto.getEquipamentoId())
            .orElseThrow(() -> new RegraNegocioException("Equipamento não encontrado"));

        m.setDescricao(dto.getDescricao());
        m.setData(dto.getData());
        m.setCusto(dto.getCusto());
        m.setEquipamento(equipamento);

        return toResponse(manutencaoRepository.save(m));
    }

    @Override
    public void removerPorId(Long id) {
        manutencaoRepository.deleteById(id);
    }

    private Manutencao toEntity(ManutencaoRequestDTO dto) {
        Equipamento equipamento = equipamentoRepository.findById(dto.getEquipamentoId())
            .orElseThrow(() -> new RegraNegocioException("Equipamento não encontrado"));

        return new Manutencao(
            null,
            dto.getDescricao(),
            dto.getData(),
            dto.getCusto(),
            equipamento
        );
    }

    private ManutencaoResponseDTO toResponse(Manutencao m) {
        return new ManutencaoResponseDTO(
            m.getId(),
            m.getDescricao(),
            m.getData(),
            m.getCusto(),
            m.getEquipamento() != null ? m.getEquipamento().getId() : null
        );
    }
}

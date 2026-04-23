package com.projeto.web2.service.projeto;

import com.projeto.web2.dto.projeto.ProjetoRequestDTO;
import com.projeto.web2.dto.projeto.ProjetoResponseDTO;
import com.projeto.web2.exception.RegraNegocioException;
import com.projeto.web2.model.Equipamento;
import com.projeto.web2.model.Projeto;
import com.projeto.web2.repository.ProjetoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ProjetoServiceImpl implements ProjetoService {

    private final ProjetoRepository projetoRepository;

    public ProjetoServiceImpl(ProjetoRepository projetoRepository) {
        this.projetoRepository = projetoRepository;
    }

    @Override
    public ProjetoResponseDTO criar(ProjetoRequestDTO dto) {
        Projeto projeto = toEntity(dto);
        Projeto salvo = projetoRepository.save(projeto);
        return toResponse(salvo);
    }

    @Override
    public List<ProjetoResponseDTO> listar() {
        return projetoRepository.findAll()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    @Override
    public ProjetoResponseDTO buscarPorId(Long id) {
        Projeto projeto = projetoRepository.findById(id)
            .orElseThrow(() -> new RegraNegocioException("Projeto não encontrado"));
        return toResponse(projeto);
    }

    @Override
    public ProjetoResponseDTO atualizarPorId(Long id, ProjetoRequestDTO dto) {
        Projeto projeto = projetoRepository.findById(id)
            .orElseThrow(() -> new RegraNegocioException("Projeto não encontrado"));

        projeto.setNome(dto.getNome());
        projeto.setDescricao(dto.getDescricao());

        return toResponse(projetoRepository.save(projeto));
    }

    @Override
    public void removerPorId(Long id) {
        projetoRepository.deleteById(id);
    }
    private Projeto toEntity(ProjetoRequestDTO dto) {
        return new Projeto(
            null,
            dto.getNome(),
            dto.getDescricao(),
            null
        );
    }

    private ProjetoResponseDTO toResponse(Projeto p) {
        return new ProjetoResponseDTO(
            p.getId(),
            p.getNome(),
            p.getDescricao(),
            p.getEquipamentos() != null
                ? p.getEquipamentos().stream()
                .map(Equipamento::getId)
                .collect(java.util.stream.Collectors.toSet())
                : Set.of()
        );
    }

}

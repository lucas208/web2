package com.projeto.web2.controller;

import com.projeto.web2.dto.equipamento.EquipamentoRequestDTO;
import com.projeto.web2.dto.equipamento.EquipamentoResponseDTO;
import com.projeto.web2.exception.RegraNegocioException;
import com.projeto.web2.model.Categoria;
import com.projeto.web2.service.equipamento.EquipamentoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/equipamentos")
public class EquipamentoController {

    private final EquipamentoService equipamentoServiceValidacaoSimples;
    private final EquipamentoService equipamentoServiceValidacaoCompleta;

    public EquipamentoController(@Qualifier("simples") EquipamentoService equipamentoServiceValidacaoSimples,
                                 @Qualifier("completa") EquipamentoService equipamentoServiceValidacaoCompleta) {
        this.equipamentoServiceValidacaoSimples = equipamentoServiceValidacaoSimples;
        this.equipamentoServiceValidacaoCompleta = equipamentoServiceValidacaoCompleta;
    }

    private EquipamentoService selecionarService(String tipo) {
        if ("completa".equalsIgnoreCase(tipo)) {
            return equipamentoServiceValidacaoCompleta;
        }
        return equipamentoServiceValidacaoSimples;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MASTER','ROLE_CONTRIBUTOR')")
    @PostMapping
    public ResponseEntity<EquipamentoResponseDTO> criar(
            @RequestParam(defaultValue = "simples") String tipo,
            @RequestBody @Valid EquipamentoRequestDTO dto) {
        EquipamentoResponseDTO response = selecionarService(tipo).criar(dto);
        return ResponseEntity.status(201).body(response);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MASTER','ROLE_CONTRIBUTOR','ROLE_AUDITOR')")
    @GetMapping
    public ResponseEntity<List<EquipamentoResponseDTO>> listar() {
        return ResponseEntity.ok(equipamentoServiceValidacaoSimples.listar());
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MASTER','ROLE_CONTRIBUTOR','ROLE_AUDITOR')")
    @GetMapping("/{id}")
    public ResponseEntity<EquipamentoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(equipamentoServiceValidacaoSimples.buscarPorId(id));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MASTER','ROLE_CONTRIBUTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<EquipamentoResponseDTO> atualizarPorId(
            @PathVariable Long id,
            @RequestParam(defaultValue = "simples") String tipo,
            @RequestBody @Valid EquipamentoRequestDTO dto) {
        return ResponseEntity.ok(selecionarService(tipo).atualizarPorId(id, dto));
    }

    @PreAuthorize("hasAuthority('ROLE_MASTER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerPorId(@PathVariable Long id) {
        equipamentoServiceValidacaoSimples.removerPorId(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MASTER','ROLE_CONTRIBUTOR','ROLE_AUDITOR')")
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<EquipamentoResponseDTO>> buscarPorCategoria(@PathVariable String categoria) {
        try {
            Categoria categoriaEnum = Categoria.valueOf(categoria.toUpperCase());
            return ResponseEntity.ok(equipamentoServiceValidacaoSimples.buscarPorCategoria(categoriaEnum));
        } catch (IllegalArgumentException e) {
            throw new RegraNegocioException("Categoria inválida: " + categoria);
        }
    }


    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<String> handleRegraNegocio(RegraNegocioException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(errors);
    }

}

package com.projeto.web2.controller;

import com.projeto.web2.dto.manutencao.ManutencaoRequestDTO;
import com.projeto.web2.dto.manutencao.ManutencaoResponseDTO;
import com.projeto.web2.exception.RegraNegocioException;
import com.projeto.web2.service.manutencao.ManutencaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/manutencoes")
public class ManutencaoController {

    private final ManutencaoService manutencaoService;

    public ManutencaoController(ManutencaoService manutencaoService) {
        this.manutencaoService = manutencaoService;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MASTER','ROLE_CONTRIBUTOR')")
    @PostMapping
    public ResponseEntity<ManutencaoResponseDTO> criar(@RequestBody @Valid ManutencaoRequestDTO dto) {
        return ResponseEntity.status(201).body(manutencaoService.criar(dto));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MASTER','ROLE_CONTRIBUTOR','ROLE_AUDITOR')")
    @GetMapping
    public ResponseEntity<List<ManutencaoResponseDTO>> listar() {
        return ResponseEntity.ok(manutencaoService.listar());
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MASTER','ROLE_CONTRIBUTOR','ROLE_AUDITOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ManutencaoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(manutencaoService.buscarPorId(id));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MASTER','ROLE_CONTRIBUTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ManutencaoResponseDTO> atualizarPorId(@PathVariable Long id,
            @RequestBody @Valid ManutencaoRequestDTO dto) {
        return ResponseEntity.ok(manutencaoService.atualizarPorId(id, dto));
    }

    @PreAuthorize("hasAuthority('ROLE_MASTER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerPorId(@PathVariable Long id) {
        manutencaoService.removerPorId(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<String> handleRegraNegocio(RegraNegocioException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleNotFound(RuntimeException ex) {
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

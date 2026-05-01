package com.projeto.web2.controller;


import com.projeto.web2.dto.fornecedor.FornecedorRequestDTO;
import com.projeto.web2.dto.fornecedor.FornecedorResponseDTO;
import com.projeto.web2.exception.RegraNegocioException;
import com.projeto.web2.service.fornecedor.FornecedorService;
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
@RequestMapping("/api/fornecedores")
public class FornecedorController {

    private final FornecedorService fornecedorService;

    public FornecedorController(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MASTER','ROLE_CONTRIBUTOR')")
    @PostMapping
    public ResponseEntity<FornecedorResponseDTO> criar(@RequestBody @Valid FornecedorRequestDTO dto) {
        return ResponseEntity.status(201).body(fornecedorService.criar(dto));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MASTER','ROLE_CONTRIBUTOR','ROLE_AUDITOR')")
    @GetMapping
    public ResponseEntity<List<FornecedorResponseDTO>> listar() {
        return ResponseEntity.ok(fornecedorService.listar());
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MASTER','ROLE_CONTRIBUTOR','ROLE_AUDITOR')")
    @GetMapping("/{id}")
    public ResponseEntity<FornecedorResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(fornecedorService.buscarPorId(id));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MASTER','ROLE_CONTRIBUTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<FornecedorResponseDTO> atualizarPorId(@PathVariable Long id,
            @RequestBody @Valid FornecedorRequestDTO dto) {
        return ResponseEntity.ok(fornecedorService.atualizarPorId(id, dto));
    }

    @PreAuthorize("hasAuthority('ROLE_MASTER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerPorId(@PathVariable Long id) {
        fornecedorService.removerPorId(id);
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

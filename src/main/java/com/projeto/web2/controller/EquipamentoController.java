package com.projeto.web2.controller;

import com.projeto.web2.dto.EquipamentoRequestDTO;
import com.projeto.web2.dto.EquipamentoResponseDTO;
import com.projeto.web2.exception.RegraNegocioException;
import com.projeto.web2.model.Categoria;
import com.projeto.web2.service.EquipamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/equipamentos")
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

    @PostMapping
    public ResponseEntity<EquipamentoResponseDTO> criar(
            @RequestParam(defaultValue = "simples") String tipo,
            @RequestBody @Valid EquipamentoRequestDTO dto) {
        EquipamentoResponseDTO response = selecionarService(tipo).criar(dto);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<List<EquipamentoResponseDTO>> listar() {
        return ResponseEntity.ok(equipamentoServiceValidacaoSimples.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipamentoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(equipamentoServiceValidacaoSimples.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipamentoResponseDTO> atualizarPorId(
            @PathVariable Long id,
            @RequestParam(defaultValue = "simples") String tipo,
            @RequestBody @Valid EquipamentoRequestDTO dto) {
        return ResponseEntity.ok(selecionarService(tipo).atualizarPorId(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerPorId(@PathVariable Long id) {
        equipamentoServiceValidacaoSimples.removerPorId(id);
        return ResponseEntity.noContent().build();
    }

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

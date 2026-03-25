package com.ibji.app.controller;

import com.ibji.app.dto.request.EscalaRequestDTO;
import com.ibji.app.dto.response.EscalaResponseDTO;
import com.ibji.app.service.EscalaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/escalas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EscalaController {

    private final EscalaService escalaService;

    @GetMapping
    public ResponseEntity<List<EscalaResponseDTO>> findAll() {
        List<EscalaResponseDTO> escalas = escalaService.findAll();
        return ResponseEntity.ok(escalas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EscalaResponseDTO> findById(@PathVariable Long id) {
        EscalaResponseDTO escala = escalaService.findById(id);
        return ResponseEntity.ok(escala);
    }

    @GetMapping("/ministerio/{ministerioId}")
    public ResponseEntity<List<EscalaResponseDTO>> findByMinisterio(@PathVariable Long ministerioId) {
        List<EscalaResponseDTO> escalas = escalaService.findByMinisterio(ministerioId);
        return ResponseEntity.ok(escalas);
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<EscalaResponseDTO>> findByPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        List<EscalaResponseDTO> escalas = escalaService.findByPeriodo(inicio, fim);
        return ResponseEntity.ok(escalas);
    }

    @PostMapping
    public ResponseEntity<EscalaResponseDTO> create(
            @Valid @RequestBody EscalaRequestDTO requestDTO,
            @RequestParam Long criadoPorId) {
        EscalaResponseDTO escala = escalaService.create(requestDTO, criadoPorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(escala);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EscalaResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody EscalaRequestDTO requestDTO) {
        EscalaResponseDTO escala = escalaService.update(id, requestDTO);
        return ResponseEntity.ok(escala);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        escalaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

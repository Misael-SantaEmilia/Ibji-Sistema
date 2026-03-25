package com.ibji.app.controller;

import com.ibji.app.dto.request.RecadoRequestDTO;
import com.ibji.app.dto.response.RecadoResponseDTO;
import com.ibji.app.dto.response.PagedResponseDTO;
import com.ibji.app.enums.PublicoAlvo;
import com.ibji.app.service.RecadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recados")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RecadoController {

    private final RecadoService recadoService;

    @GetMapping
    public ResponseEntity<PagedResponseDTO<RecadoResponseDTO>> findAll(
            @RequestParam(required = false) PublicoAlvo publicoAlvo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "dataCriacao") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<RecadoResponseDTO> recadoPage = recadoService.findAll(publicoAlvo, pageable);

        PagedResponseDTO<RecadoResponseDTO> response = PagedResponseDTO.<RecadoResponseDTO>builder()
                .content(recadoPage.getContent())
                .page(recadoPage.getNumber())
                .size(recadoPage.getSize())
                .totalElements(recadoPage.getTotalElements())
                .totalPages(recadoPage.getTotalPages())
                .last(recadoPage.isLast())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/visiveis")
    public ResponseEntity<List<RecadoResponseDTO>> findRecadosVisiveis(
            @RequestParam(required = false) Long ministerioId) {
        List<RecadoResponseDTO> recados = recadoService.findRecadosVisiveis(ministerioId);
        return ResponseEntity.ok(recados);
    }

    @GetMapping("/igreja")
    public ResponseEntity<List<RecadoResponseDTO>> findRecadosIgreja() {
        List<RecadoResponseDTO> recados = recadoService.findRecadosIgreja();
        return ResponseEntity.ok(recados);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecadoResponseDTO> findById(@PathVariable Long id) {
        RecadoResponseDTO recado = recadoService.findById(id);
        return ResponseEntity.ok(recado);
    }

    @PostMapping
    public ResponseEntity<RecadoResponseDTO> create(
            @Valid @RequestBody RecadoRequestDTO requestDTO,
            @RequestParam Long criadoPorId) {
        RecadoResponseDTO recado = recadoService.create(requestDTO, criadoPorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(recado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecadoResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody RecadoRequestDTO requestDTO) {
        RecadoResponseDTO recado = recadoService.update(id, requestDTO);
        return ResponseEntity.ok(recado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        recadoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

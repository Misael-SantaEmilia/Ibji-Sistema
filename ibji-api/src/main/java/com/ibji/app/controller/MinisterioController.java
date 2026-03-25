package com.ibji.app.controller;

import com.ibji.app.dto.request.MinisterioRequestDTO;
import com.ibji.app.dto.response.MembroResponseDTO;
import com.ibji.app.dto.response.MinisterioResponseDTO;
import com.ibji.app.dto.response.PagedResponseDTO;
import com.ibji.app.service.MinisterioService;
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
@RequestMapping("/api/ministerios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MinisterioController {

    private final MinisterioService ministerioService;

    @GetMapping
    public ResponseEntity<PagedResponseDTO<MinisterioResponseDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "descricao") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<MinisterioResponseDTO> ministerioPage = ministerioService.findAll(pageable);

        PagedResponseDTO<MinisterioResponseDTO> response = PagedResponseDTO.<MinisterioResponseDTO>builder()
                .content(ministerioPage.getContent())
                .page(ministerioPage.getNumber())
                .size(ministerioPage.getSize())
                .totalElements(ministerioPage.getTotalElements())
                .totalPages(ministerioPage.getTotalPages())
                .last(ministerioPage.isLast())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MinisterioResponseDTO> findById(@PathVariable Long id) {
        MinisterioResponseDTO ministerio = ministerioService.findById(id);
        return ResponseEntity.ok(ministerio);
    }

    @PostMapping
    public ResponseEntity<MinisterioResponseDTO> create(@Valid @RequestBody MinisterioRequestDTO requestDTO) {
        MinisterioResponseDTO ministerio = ministerioService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ministerio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MinisterioResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody MinisterioRequestDTO requestDTO) {
        MinisterioResponseDTO ministerio = ministerioService.update(id, requestDTO);
        return ResponseEntity.ok(ministerio);
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<MinisterioResponseDTO> inativar(@PathVariable Long id) {
        MinisterioResponseDTO ministerio = ministerioService.inativar(id);
        return ResponseEntity.ok(ministerio);
    }

    @PostMapping("/{id}/membros/{membroId}")
    public ResponseEntity<MinisterioResponseDTO> adicionarMembro(
            @PathVariable Long id,
            @PathVariable Long membroId) {
        MinisterioResponseDTO ministerio = ministerioService.adicionarMembro(id, membroId);
        return ResponseEntity.ok(ministerio);
    }

    @DeleteMapping("/{id}/membros/{membroId}")
    public ResponseEntity<MinisterioResponseDTO> removerMembro(
            @PathVariable Long id,
            @PathVariable Long membroId) {
        MinisterioResponseDTO ministerio = ministerioService.removerMembro(id, membroId);
        return ResponseEntity.ok(ministerio);
    }

    @GetMapping("/{id}/membros")
    public ResponseEntity<List<MembroResponseDTO>> findMembros(@PathVariable Long id) {
        List<MembroResponseDTO> membros = ministerioService.findMembrosByMinisterio(id);
        return ResponseEntity.ok(membros);
    }
}

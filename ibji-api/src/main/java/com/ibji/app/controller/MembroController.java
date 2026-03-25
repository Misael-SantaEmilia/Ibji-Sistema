package com.ibji.app.controller;

import com.ibji.app.dto.request.MembroRequestDTO;
import com.ibji.app.dto.response.AniversarianteResponseDTO;
import com.ibji.app.dto.response.MembroResponseDTO;
import com.ibji.app.dto.response.PagedResponseDTO;
import com.ibji.app.enums.Status;
import com.ibji.app.service.MembroService;
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
@RequestMapping("/api/membresia")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MembroController {

    private final MembroService membroService;

    @GetMapping
    public ResponseEntity<PagedResponseDTO<MembroResponseDTO>> findAll(
            @RequestParam(required = false) Status status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "nome") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<MembroResponseDTO> membroPage;
        if (status != null) {
            membroPage = membroService.findByStatus(status, pageable);
        } else {
            membroPage = membroService.findAll(pageable);
        }

        PagedResponseDTO<MembroResponseDTO> response = PagedResponseDTO.<MembroResponseDTO>builder()
                .content(membroPage.getContent())
                .page(membroPage.getNumber())
                .size(membroPage.getSize())
                .totalElements(membroPage.getTotalElements())
                .totalPages(membroPage.getTotalPages())
                .last(membroPage.isLast())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MembroResponseDTO> findById(@PathVariable Long id) {
        MembroResponseDTO membro = membroService.findById(id);
        return ResponseEntity.ok(membro);
    }

    @PostMapping
    public ResponseEntity<MembroResponseDTO> create(@Valid @RequestBody MembroRequestDTO requestDTO) {
        MembroResponseDTO membro = membroService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(membro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MembroResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody MembroRequestDTO requestDTO) {
        MembroResponseDTO membro = membroService.update(id, requestDTO);
        return ResponseEntity.ok(membro);
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<MembroResponseDTO> inativar(@PathVariable Long id) {
        MembroResponseDTO membro = membroService.inativar(id);
        return ResponseEntity.ok(membro);
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<MembroResponseDTO> reativar(@PathVariable Long id) {
        MembroResponseDTO membro = membroService.reativar(id);
        return ResponseEntity.ok(membro);
    }
}

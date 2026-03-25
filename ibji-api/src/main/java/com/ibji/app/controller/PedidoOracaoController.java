package com.ibji.app.controller;

import com.ibji.app.dto.request.PedidoOracaoRequestDTO;
import com.ibji.app.dto.response.PedidoOracaoResponseDTO;
import com.ibji.app.dto.response.PagedResponseDTO;
import com.ibji.app.service.PedidoOracaoService;
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
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos-oracao")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PedidoOracaoController {

    private final PedidoOracaoService pedidoOracaoService;

    @GetMapping
    public ResponseEntity<PagedResponseDTO<PedidoOracaoResponseDTO>> findAll(
            @RequestParam(required = false) Boolean publico,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "dataPedido") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PedidoOracaoResponseDTO> pedidoPage = pedidoOracaoService.findAll(publico, pageable);

        PagedResponseDTO<PedidoOracaoResponseDTO> response = PagedResponseDTO.<PedidoOracaoResponseDTO>builder()
                .content(pedidoPage.getContent())
                .page(pedidoPage.getNumber())
                .size(pedidoPage.getSize())
                .totalElements(pedidoPage.getTotalElements())
                .totalPages(pedidoPage.getTotalPages())
                .last(pedidoPage.isLast())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/pendentes")
    public ResponseEntity<List<PedidoOracaoResponseDTO>> findPendentes() {
        List<PedidoOracaoResponseDTO> pedidos = pedidoOracaoService.findPendentes();
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/aprovados")
    public ResponseEntity<List<PedidoOracaoResponseDTO>> findAprovados() {
        List<PedidoOracaoResponseDTO> pedidos = pedidoOracaoService.findAprovados();
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/count-pendentes")
    public ResponseEntity<Map<String, Long>> countPendentes() {
        Long count = pedidoOracaoService.countPendentes();
        return ResponseEntity.ok(Map.of("count", count));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoOracaoResponseDTO> findById(@PathVariable Long id) {
        PedidoOracaoResponseDTO pedido = pedidoOracaoService.findById(id);
        return ResponseEntity.ok(pedido);
    }

    @PostMapping
    public ResponseEntity<PedidoOracaoResponseDTO> create(@Valid @RequestBody PedidoOracaoRequestDTO requestDTO) {
        PedidoOracaoResponseDTO pedido = pedidoOracaoService.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    @PatchMapping("/{id}/aprovar")
    public ResponseEntity<PedidoOracaoResponseDTO> aprovar(
            @PathVariable Long id,
            @RequestParam Long aprovadoPorId) {
        PedidoOracaoResponseDTO pedido = pedidoOracaoService.aprovar(id, aprovadoPorId);
        return ResponseEntity.ok(pedido);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> reprovar(@PathVariable Long id) {
        pedidoOracaoService.reprovar(id);
        return ResponseEntity.noContent().build();
    }
}

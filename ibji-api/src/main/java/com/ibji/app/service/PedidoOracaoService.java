package com.ibji.app.service;

import com.ibji.app.dto.request.PedidoOracaoRequestDTO;
import com.ibji.app.dto.response.PedidoOracaoResponseDTO;
import com.ibji.app.entity.Membro;
import com.ibji.app.entity.PedidoOracao;
import com.ibji.app.exception.ResourceNotFoundException;
import com.ibji.app.repository.PedidoOracaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PedidoOracaoService {

    private final PedidoOracaoRepository pedidoOracaoRepository;
    private final MembroService membroService;

    public List<PedidoOracaoResponseDTO> findPendentes() {
        return pedidoOracaoRepository.findPedidosPendentes().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<PedidoOracaoResponseDTO> findAprovados() {
        return pedidoOracaoRepository.findPedidosAprovados().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Page<PedidoOracaoResponseDTO> findAll(Boolean publico, Pageable pageable) {
        if (publico != null) {
            return pedidoOracaoRepository.findByPublico(publico, pageable).map(this::toResponseDTO);
        }
        return pedidoOracaoRepository.findAll(pageable).map(this::toResponseDTO);
    }

    public PedidoOracaoResponseDTO findById(Long id) {
        PedidoOracao pedido = pedidoOracaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido de Oração", id));
        return toResponseDTO(pedido);
    }

    public PedidoOracaoResponseDTO create(PedidoOracaoRequestDTO requestDTO) {
        Membro membro = null;
        if (requestDTO.getMembroId() != null) {
            membro = membroService.findMembroEntity(requestDTO.getMembroId());
        }

        PedidoOracao pedido = PedidoOracao.builder()
                .membro(membro)
                .titulo(requestDTO.getTitulo())
                .descricao(requestDTO.getDescricao())
                .publico(false)
                .build();

        PedidoOracao saved = pedidoOracaoRepository.save(pedido);
        return toResponseDTO(saved);
    }

    public PedidoOracaoResponseDTO aprovar(Long id, Long aprovadoPorId) {
        PedidoOracao pedido = pedidoOracaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido de Oração", id));

        Membro aprovador = membroService.findMembroEntity(aprovadoPorId);

        pedido.setPublico(true);
        pedido.setAprovadoPor(aprovador);
        pedido.setDataAprovacao(LocalDateTime.now());

        PedidoOracao saved = pedidoOracaoRepository.save(pedido);
        return toResponseDTO(saved);
    }

    public PedidoOracaoResponseDTO reprovar(Long id) {
        PedidoOracao pedido = pedidoOracaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido de Oração", id));

        pedidoOracaoRepository.delete(pedido);

        return toResponseDTO(pedido);
    }

    public Long countPendentes() {
        return pedidoOracaoRepository.countPedidosPendentes();
    }

    private PedidoOracaoResponseDTO toResponseDTO(PedidoOracao pedido) {
        return PedidoOracaoResponseDTO.builder()
                .id(pedido.getId())
                .membroId(pedido.getMembro() != null ? pedido.getMembro().getId() : null)
                .membroNome(pedido.getMembro() != null ? pedido.getMembro().getNome() : "Anônimo")
                .titulo(pedido.getTitulo())
                .descricao(pedido.getDescricao())
                .publico(pedido.getPublico())
                .aprovadoPorId(pedido.getAprovadoPor() != null ? pedido.getAprovadoPor().getId() : null)
                .aprovadoPorNome(pedido.getAprovadoPor() != null ? pedido.getAprovadoPor().getNome() : null)
                .dataPedido(pedido.getDataPedido())
                .dataAprovacao(pedido.getDataAprovacao())
                .build();
    }
}

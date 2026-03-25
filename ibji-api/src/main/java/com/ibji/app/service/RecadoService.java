package com.ibji.app.service;

import com.ibji.app.dto.request.RecadoRequestDTO;
import com.ibji.app.dto.response.RecadoResponseDTO;
import com.ibji.app.entity.Membro;
import com.ibji.app.entity.Recado;
import com.ibji.app.enums.PublicoAlvo;
import com.ibji.app.exception.ResourceNotFoundException;
import com.ibji.app.repository.RecadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RecadoService {

    private final RecadoRepository recadoRepository;
    private final MembroService membroService;

    public List<RecadoResponseDTO> findRecadosVisiveis(Long ministerioId) {
        LocalDate hoje = LocalDate.now();
        return recadoRepository.findRecadosVisiveis(ministerioId, hoje).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<RecadoResponseDTO> findRecadosIgreja() {
        LocalDate hoje = LocalDate.now();
        return recadoRepository.findRecadosIgreja(hoje).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Page<RecadoResponseDTO> findAll(PublicoAlvo publicoAlvo, Pageable pageable) {
        if (publicoAlvo != null) {
            return recadoRepository.findByPublicoAlvo(publicoAlvo, pageable).map(this::toResponseDTO);
        }
        return recadoRepository.findAll(pageable).map(this::toResponseDTO);
    }

    public RecadoResponseDTO findById(Long id) {
        Recado recado = recadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recado", id));
        return toResponseDTO(recado);
    }

    public RecadoResponseDTO create(RecadoRequestDTO requestDTO, Long criadoPorId) {
        Membro criadoPor = membroService.findMembroEntity(criadoPorId);

        Recado.RecadoBuilder builder = Recado.builder()
                .titulo(requestDTO.getTitulo())
                .conteudo(requestDTO.getConteudo())
                .publicoAlvo(requestDTO.getPublicoAlvo())
                .criadoPor(criadoPor)
                .dataValidade(requestDTO.getDataValidade());

        if (requestDTO.getPublicoAlvo() == PublicoAlvo.MINISTERIO && requestDTO.getMinisterioId() != null) {
            builder.ministerioId(requestDTO.getMinisterioId());
        }

        Recado saved = recadoRepository.save(builder.build());
        return toResponseDTO(saved);
    }

    public RecadoResponseDTO update(Long id, RecadoRequestDTO requestDTO) {
        Recado recado = recadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recado", id));

        recado.setTitulo(requestDTO.getTitulo());
        recado.setConteudo(requestDTO.getConteudo());
        recado.setPublicoAlvo(requestDTO.getPublicoAlvo());
        recado.setDataValidade(requestDTO.getDataValidade());

        if (requestDTO.getPublicoAlvo() == PublicoAlvo.MINISTERIO && requestDTO.getMinisterioId() != null) {
            recado.setMinisterioId(requestDTO.getMinisterioId());
        } else {
            recado.setMinisterioId(null);
        }

        Recado saved = recadoRepository.save(recado);
        return toResponseDTO(saved);
    }

    public void delete(Long id) {
        if (!recadoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recado", id);
        }
        recadoRepository.deleteById(id);
    }

    private RecadoResponseDTO toResponseDTO(Recado recado) {
        return RecadoResponseDTO.builder()
                .id(recado.getId())
                .titulo(recado.getTitulo())
                .conteudo(recado.getConteudo())
                .publicoAlvo(recado.getPublicoAlvo())
                .ministerioId(recado.getMinisterioId())
                .ministerioNome(recado.getMinisterio() != null ? recado.getMinisterio().getDescricao() : null)
                .criadoPorId(recado.getCriadoPor().getId())
                .criadoPorNome(recado.getCriadoPor().getNome())
                .dataValidade(recado.getDataValidade())
                .dataCriacao(recado.getDataCriacao())
                .dataAtualizacao(recado.getDataAtualizacao())
                .build();
    }
}

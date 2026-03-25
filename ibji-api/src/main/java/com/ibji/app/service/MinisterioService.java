package com.ibji.app.service;

import com.ibji.app.dto.request.MinisterioRequestDTO;
import com.ibji.app.dto.response.MinisterioResponseDTO;
import com.ibji.app.dto.response.MembroResponseDTO;
import com.ibji.app.entity.Ministerio;
import com.ibji.app.entity.MinisterioMembro;
import com.ibji.app.entity.Membro;
import com.ibji.app.enums.Status;
import com.ibji.app.exception.BadRequestException;
import com.ibji.app.exception.ResourceNotFoundException;
import com.ibji.app.repository.MinisterioMembroRepository;
import com.ibji.app.repository.MinisterioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MinisterioService {

    private final MinisterioRepository ministerioRepository;
    private final MinisterioMembroRepository ministerioMembroRepository;
    private final MembroService membroService;

    public List<MinisterioResponseDTO> findAll() {
        return ministerioRepository.findByStatus(Status.ATIVO).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Page<MinisterioResponseDTO> findAll(Pageable pageable) {
        return ministerioRepository.findByStatus(Status.ATIVO, pageable)
                .map(this::toResponseDTO);
    }

    public MinisterioResponseDTO findById(Long id) {
        Ministerio ministerio = ministerioRepository.findByIdWithLider(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ministério", id));
        return toResponseDTO(ministerio);
    }

    public MinisterioResponseDTO create(MinisterioRequestDTO requestDTO) {
        Membro lider = membroService.findMembroEntity(requestDTO.getLiderId());

        if (lider.getStatus() == Status.INATIVO) {
            throw new BadRequestException("Líder informado está inativo");
        }

        Ministerio ministerio = Ministerio.builder()
                .descricao(requestDTO.getDescricao())
                .lider(lider)
                .status(Status.ATIVO)
                .build();

        Ministerio saved = ministerioRepository.save(ministerio);

        if (requestDTO.getMembrosIds() != null && !requestDTO.getMembrosIds().isEmpty()) {
            adicionarMembros(saved, requestDTO.getMembrosIds());
        }

        return toResponseDTO(saved);
    }

    public MinisterioResponseDTO update(Long id, MinisterioRequestDTO requestDTO) {
        Ministerio ministerio = ministerioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ministério", id));

        Membro lider = membroService.findMembroEntity(requestDTO.getLiderId());
        if (lider.getStatus() == Status.INATIVO) {
            throw new BadRequestException("Líder informado está inativo");
        }

        ministerio.setDescricao(requestDTO.getDescricao());
        ministerio.setLider(lider);

        Ministerio saved = ministerioRepository.save(ministerio);
        return toResponseDTO(saved);
    }

    public MinisterioResponseDTO inativar(Long id) {
        Ministerio ministerio = ministerioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ministério", id));

        ministerio.setStatus(Status.INATIVO);

        List<MinisterioMembro> membros = ministerioMembroRepository
                .findByMinisterioIdAndStatus(id, Status.ATIVO);
        membros.forEach(mm -> mm.setStatus(Status.INATIVO));

        Ministerio saved = ministerioRepository.save(ministerio);
        return toResponseDTO(saved);
    }

    public MinisterioResponseDTO adicionarMembro(Long ministerioId, Long membroId) {
        Ministerio ministerio = ministerioRepository.findById(ministerioId)
                .orElseThrow(() -> new ResourceNotFoundException("Ministério", ministerioId));

        Membro membro = membroService.findMembroEntity(membroId);

        if (membro.getStatus() == Status.INATIVO) {
            throw new BadRequestException("Membro informado está inativo");
        }

        boolean jaExiste = ministerioMembroRepository
                .existsByMinisterioIdAndMembroIdAndStatus(ministerioId, membroId, Status.ATIVO);

        if (jaExiste) {
            throw new BadRequestException("Membro já pertence a este ministério");
        }

        MinisterioMembro ministerioMembro = MinisterioMembro.builder()
                .ministerio(ministerio)
                .membro(membro)
                .status(Status.ATIVO)
                .build();

        ministerioMembroRepository.save(ministerioMembro);

        return toResponseDTO(ministerio);
    }

    public MinisterioResponseDTO removerMembro(Long ministerioId, Long membroId) {
        Ministerio ministerio = ministerioRepository.findById(ministerioId)
                .orElseThrow(() -> new ResourceNotFoundException("Ministério", ministerioId));

        MinisterioMembro ministerioMembro = ministerioMembroRepository
                .findByMinisterioIdAndMembroId(ministerioId, membroId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Associação entre membro e ministério não encontrada"));

        ministerioMembro.setStatus(Status.INATIVO);
        ministerioMembroRepository.save(ministerioMembro);

        return toResponseDTO(ministerio);
    }

    public List<MembroResponseDTO> findMembrosByMinisterio(Long ministerioId) {
        if (!ministerioRepository.existsById(ministerioId)) {
            throw new ResourceNotFoundException("Ministério", ministerioId);
        }

        return ministerioMembroRepository.findMembrosAtivosByMinisterio(ministerioId)
                .stream()
                .map(mm -> toMembroResponseDTO(mm.getMembro()))
                .collect(Collectors.toList());
    }

    private void adicionarMembros(Ministerio ministerio, List<Long> membrosIds) {
        for (Long membroId : membrosIds) {
            Membro membro = membroService.findMembroEntity(membroId);

            if (membro.getStatus() == Status.ATIVO) {
                MinisterioMembro ministerioMembro = MinisterioMembro.builder()
                        .ministerio(ministerio)
                        .membro(membro)
                        .status(Status.ATIVO)
                        .build();
                ministerioMembroRepository.save(ministerioMembro);
            }
        }
    }

    private MinisterioResponseDTO toResponseDTO(Ministerio ministerio) {
        Long totalMembros = ministerioRepository.countMembrosByMinisterio(ministerio.getId());

        return MinisterioResponseDTO.builder()
                .id(ministerio.getId())
                .descricao(ministerio.getDescricao())
                .liderId(ministerio.getLider().getId())
                .liderNome(ministerio.getLider().getNome())
                .status(ministerio.getStatus())
                .dataCadastro(ministerio.getDataCadastro())
                .dataAtualizacao(ministerio.getDataAtualizacao())
                .totalMembros(totalMembros != null ? totalMembros.intValue() : 0)
                .build();
    }

    private MembroResponseDTO toMembroResponseDTO(Membro membro) {
        return MembroResponseDTO.builder()
                .id(membro.getId())
                .nome(membro.getNome())
                .cpf(membro.getCpf())
                .email(membro.getEmail())
                .telefone(membro.getTelefone())
                .dataNascimento(membro.getDataNascimento())
                .status(membro.getStatus())
                .nivelAcesso(membro.getNivelAcesso())
                .build();
    }
}

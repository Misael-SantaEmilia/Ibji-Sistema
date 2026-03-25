package com.ibji.app.service;

import com.ibji.app.dto.request.EscalaRequestDTO;
import com.ibji.app.dto.response.EscalaResponseDTO;
import com.ibji.app.dto.response.MembroResponseDTO;
import com.ibji.app.entity.Escala;
import com.ibji.app.entity.EscalaMembro;
import com.ibji.app.entity.Membro;
import com.ibji.app.entity.Ministerio;
import com.ibji.app.exception.BadRequestException;
import com.ibji.app.exception.ResourceNotFoundException;
import com.ibji.app.repository.EscalaMembroRepository;
import com.ibji.app.repository.EscalaRepository;
import com.ibji.app.repository.MinisterioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EscalaService {

    private final EscalaRepository escalaRepository;
    private final EscalaMembroRepository escalaMembroRepository;
    private final MinisterioRepository ministerioRepository;
    private final MembroService membroService;

    public List<EscalaResponseDTO> findAll() {
        return escalaRepository.findEscalasFuturas(LocalDate.now()).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<EscalaResponseDTO> findByMinisterio(Long ministerioId) {
        if (!ministerioRepository.existsById(ministerioId)) {
            throw new ResourceNotFoundException("Ministério", ministerioId);
        }

        return escalaRepository.findByMinisterioIdOrderByDataEscalaDesc(ministerioId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public EscalaResponseDTO findById(Long id) {
        Escala escala = escalaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Escala", id));
        return toResponseDTO(escala);
    }

    public EscalaResponseDTO create(EscalaRequestDTO requestDTO, Long criadoPorId) {
        Ministerio ministerio = ministerioRepository.findById(requestDTO.getMinisterioId())
                .orElseThrow(() -> new ResourceNotFoundException("Ministério", requestDTO.getMinisterioId()));

        Membro criadoPor = membroService.findMembroEntity(criadoPorId);

        if (requestDTO.getHorarioFim().isBefore(requestDTO.getHorarioInicio()) ||
            requestDTO.getHorarioFim().equals(requestDTO.getHorarioInicio())) {
            throw new BadRequestException("Horário de fim deve ser posterior ao horário de início");
        }

        Escala escala = Escala.builder()
                .ministerio(ministerio)
                .dataEscala(requestDTO.getDataEscala())
                .horarioInicio(requestDTO.getHorarioInicio())
                .horarioFim(requestDTO.getHorarioFim())
                .observacao(requestDTO.getObservacao())
                .criadoPor(criadoPor)
                .build();

        Escala saved = escalaRepository.save(escala);

        for (Long membroId : requestDTO.getMembrosIds()) {
            Membro membro = membroService.findMembroEntity(membroId);

            EscalaMembro escalaMembro = EscalaMembro.builder()
                    .escala(saved)
                    .membro(membro)
                    .build();

            escalaMembroRepository.save(escalaMembro);
        }

        return toResponseDTO(saved);
    }

    public EscalaResponseDTO update(Long id, EscalaRequestDTO requestDTO) {
        Escala escala = escalaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Escala", id));

        Ministerio ministerio = ministerioRepository.findById(requestDTO.getMinisterioId())
                .orElseThrow(() -> new ResourceNotFoundException("Ministério", requestDTO.getMinisterioId()));

        if (requestDTO.getHorarioFim().isBefore(requestDTO.getHorarioInicio()) ||
            requestDTO.getHorarioFim().equals(requestDTO.getHorarioInicio())) {
            throw new BadRequestException("Horário de fim deve ser posterior ao horário de início");
        }

        escala.setMinisterio(ministerio);
        escala.setDataEscala(requestDTO.getDataEscala());
        escala.setHorarioInicio(requestDTO.getHorarioInicio());
        escala.setHorarioFim(requestDTO.getHorarioFim());
        escala.setObservacao(requestDTO.getObservacao());

        escalaMembroRepository.deleteAll(escalaMembroRepository.findByEscalaId(id));

        for (Long membroId : requestDTO.getMembrosIds()) {
            Membro membro = membroService.findMembroEntity(membroId);

            EscalaMembro escalaMembro = EscalaMembro.builder()
                    .escala(escala)
                    .membro(membro)
                    .build();

            escalaMembroRepository.save(escalaMembro);
        }

        Escala saved = escalaRepository.save(escala);
        return toResponseDTO(saved);
    }

    public void delete(Long id) {
        if (!escalaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Escala", id);
        }
        escalaMembroRepository.deleteAll(escalaMembroRepository.findByEscalaId(id));
        escalaRepository.deleteById(id);
    }

    public List<EscalaResponseDTO> findByPeriodo(LocalDate inicio, LocalDate fim) {
        return escalaRepository.findByPeriodo(inicio, fim).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private EscalaResponseDTO toResponseDTO(Escala escala) {
        List<MembroResponseDTO> membros = escalaMembroRepository.findByEscalaId(escala.getId())
                .stream()
                .map(em -> toMembroResponseDTO(em.getMembro()))
                .collect(Collectors.toList());

        return EscalaResponseDTO.builder()
                .id(escala.getId())
                .ministerioId(escala.getMinisterio().getId())
                .ministerioNome(escala.getMinisterio().getDescricao())
                .dataEscala(escala.getDataEscala())
                .horarioInicio(escala.getHorarioInicio())
                .horarioFim(escala.getHorarioFim())
                .observacao(escala.getObservacao())
                .criadoPorId(escala.getCriadoPor().getId())
                .criadoPorNome(escala.getCriadoPor().getNome())
                .membros(membros)
                .build();
    }

    private MembroResponseDTO toMembroResponseDTO(Membro membro) {
        return MembroResponseDTO.builder()
                .id(membro.getId())
                .nome(membro.getNome())
                .email(membro.getEmail())
                .telefone(membro.getTelefone())
                .build();
    }
}

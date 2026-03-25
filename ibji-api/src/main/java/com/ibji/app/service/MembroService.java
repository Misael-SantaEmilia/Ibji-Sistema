package com.ibji.app.service;

import com.ibji.app.dto.request.MembroRequestDTO;
import com.ibji.app.dto.response.AniversarianteResponseDTO;
import com.ibji.app.dto.response.MembroResponseDTO;
import com.ibji.app.entity.Membro;
import com.ibji.app.enums.NivelAcesso;
import com.ibji.app.enums.Status;
import com.ibji.app.exception.BadRequestException;
import com.ibji.app.exception.ResourceNotFoundException;
import com.ibji.app.repository.MembroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MembroService {

    private final MembroRepository membroRepository;

    public List<MembroResponseDTO> findAll() {
        return membroRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Page<MembroResponseDTO> findAll(Pageable pageable) {
        return membroRepository.findAll(pageable).map(this::toResponseDTO);
    }

    public Page<MembroResponseDTO> findByStatus(Status status, Pageable pageable) {
        return membroRepository.findByStatus(status, pageable).map(this::toResponseDTO);
    }

    public MembroResponseDTO findById(Long id) {
        Membro membro = membroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membro", id));
        return toResponseDTO(membro);
    }

    public MembroResponseDTO create(MembroRequestDTO requestDTO) {
        if (requestDTO.getCpf() != null && membroRepository.existsByCpf(requestDTO.getCpf())) {
            throw new BadRequestException("CPF já cadastrado no sistema");
        }

        if (requestDTO.getEmail() != null && membroRepository.existsByEmail(requestDTO.getEmail())) {
            throw new BadRequestException("Email já cadastrado no sistema");
        }

        Membro membro = toEntity(requestDTO);
        membro.setStatus(Status.ATIVO);

        Membro saved = membroRepository.save(membro);
        return toResponseDTO(saved);
    }

    public MembroResponseDTO update(Long id, MembroRequestDTO requestDTO) {
        Membro membro = membroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membro", id));

        if (requestDTO.getCpf() != null && !requestDTO.getCpf().equals(membro.getCpf())) {
            if (membroRepository.existsByCpf(requestDTO.getCpf())) {
                throw new BadRequestException("CPF já cadastrado no sistema");
            }
        }

        if (requestDTO.getEmail() != null && !requestDTO.getEmail().equals(membro.getEmail())) {
            if (membroRepository.existsByEmail(requestDTO.getEmail())) {
                throw new BadRequestException("Email já cadastrado no sistema");
            }
        }

        updateEntityFromDTO(membro, requestDTO);
        Membro saved = membroRepository.save(membro);
        return toResponseDTO(saved);
    }

    public MembroResponseDTO inativar(Long id) {
        Membro membro = membroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membro", id));

        if (membro.getStatus() == Status.INATIVO) {
            throw new BadRequestException("Membro já está inativo");
        }

        membro.setStatus(Status.INATIVO);
        Membro saved = membroRepository.save(membro);
        return toResponseDTO(saved);
    }

    public MembroResponseDTO reativar(Long id) {
        Membro membro = membroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membro", id));

        if (membro.getStatus() == Status.ATIVO) {
            throw new BadRequestException("Membro já está ativo");
        }

        membro.setStatus(Status.ATIVO);
        Membro saved = membroRepository.save(membro);
        return toResponseDTO(saved);
    }

    public List<AniversarianteResponseDTO> findAniversariantes(String tipo) {
        LocalDate hoje = LocalDate.now();

        List<Membro> aniversariantes;

        switch (tipo.toUpperCase()) {
            case "D":
                aniversariantes = membroRepository.findAniversariantesDoDia(
                        hoje.getDayOfMonth(), hoje.getMonthValue());
                break;
            case "S":
                LocalDate inicioSemana = hoje.minusDays(hoje.getDayOfWeek().getValue() - 1);
                LocalDate fimSemana = inicioSemana.plusDays(6);
                aniversariantes = membroRepository.findAniversariantesDaSemana(inicioSemana, fimSemana);
                break;
            case "M":
                aniversariantes = membroRepository.findAniversariantesDoMes(hoje.getMonthValue());
                break;
            default:
                throw new BadRequestException("Tipo de filtro inválido. Use D (Dia), S (Semana) ou M (Mês)");
        }

        return aniversariantes.stream()
                .map(this::toAniversarianteResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Membro findMembroEntity(Long id) {
        return membroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membro", id));
    }

    private MembroResponseDTO toResponseDTO(Membro membro) {
        return MembroResponseDTO.builder()
                .id(membro.getId())
                .nome(membro.getNome())
                .cpf(membro.getCpf())
                .email(membro.getEmail())
                .telefone(membro.getTelefone())
                .dataNascimento(membro.getDataNascimento())
                .endereco(membro.getEndereco())
                .cidade(membro.getCidade())
                .estado(membro.getEstado())
                .cep(membro.getCep())
                .nivelAcesso(membro.getNivelAcesso())
                .status(membro.getStatus())
                .fotoUrl(membro.getFotoUrl())
                .dataCadastro(membro.getDataCadastro())
                .dataAtualizacao(membro.getDataAtualizacao())
                .build();
    }

    private AniversarianteResponseDTO toAniversarianteResponseDTO(Membro membro) {
        int idade = membro.getDataNascimento() != null
                ? Period.between(membro.getDataNascimento(), LocalDate.now()).getYears()
                : 0;

        return AniversarianteResponseDTO.builder()
                .id(membro.getId())
                .nome(membro.getNome())
                .dataNascimento(membro.getDataNascimento())
                .idade(idade)
                .email(membro.getEmail())
                .telefone(membro.getTelefone())
                .build();
    }

    private Membro toEntity(MembroRequestDTO dto) {
        return Membro.builder()
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .email(dto.getEmail())
                .telefone(dto.getTelefone())
                .dataNascimento(dto.getDataNascimento())
                .endereco(dto.getEndereco())
                .cidade(dto.getCidade())
                .estado(dto.getEstado())
                .cep(dto.getCep())
                .nivelAcesso(dto.getNivelAcesso() != null ? dto.getNivelAcesso() : NivelAcesso.MEMBRO)
                .fotoUrl(dto.getFotoUrl())
                .build();
    }

    private void updateEntityFromDTO(Membro membro, MembroRequestDTO dto) {
        membro.setNome(dto.getNome());
        membro.setCpf(dto.getCpf());
        membro.setEmail(dto.getEmail());
        membro.setTelefone(dto.getTelefone());
        membro.setDataNascimento(dto.getDataNascimento());
        membro.setEndereco(dto.getEndereco());
        membro.setCidade(dto.getCidade());
        membro.setEstado(dto.getEstado());
        membro.setCep(dto.getCep());
        if (dto.getNivelAcesso() != null) {
            membro.setNivelAcesso(dto.getNivelAcesso());
        }
        membro.setFotoUrl(dto.getFotoUrl());
    }
}

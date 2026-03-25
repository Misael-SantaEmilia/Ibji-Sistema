package com.ibji.app.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EscalaResponseDTO {

    private Long id;
    private Long ministerioId;
    private String ministerioNome;
    private LocalDate dataEscala;
    private LocalTime horarioInicio;
    private LocalTime horarioFim;
    private String observacao;
    private Long criadoPorId;
    private String criadoPorNome;
    private List<MembroResponseDTO> membros;
}

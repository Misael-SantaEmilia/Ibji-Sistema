package com.ibji.app.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EscalaRequestDTO {

    @NotNull(message = "ID do ministério é obrigatório")
    private Long ministerioId;

    @NotNull(message = "Data da escala é obrigatória")
    private LocalDate dataEscala;

    @NotNull(message = "Horário de início é obrigatório")
    private LocalTime horarioInicio;

    @NotNull(message = "Horário de fim é obrigatório")
    private LocalTime horarioFim;

    @Size(max = 255, message = "Observação deve ter no máximo 255 caracteres")
    private String observacao;

    @NotEmpty(message = "Deve haver pelo menos um membro na escala")
    private List<Long> membrosIds;
}

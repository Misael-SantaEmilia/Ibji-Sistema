package com.ibji.app.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MinisterioRequestDTO {

    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 80, message = "Descrição deve ter no máximo 80 caracteres")
    private String descricao;

    @NotNull(message = "ID do líder é obrigatório")
    private Long liderId;

    private List<Long> membrosIds;
}

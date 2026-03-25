package com.ibji.app.dto.request;

import com.ibji.app.enums.PublicoAlvo;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecadoRequestDTO {

    @NotBlank(message = "Título é obrigatório")
    @Size(max = 150, message = "Título deve ter no máximo 150 caracteres")
    private String titulo;

    @NotBlank(message = "Conteúdo é obrigatório")
    private String conteudo;

    @NotNull(message = "Público alvo é obrigatório")
    private PublicoAlvo publicoAlvo;

    private Long ministerioId;

    private LocalDate dataValidade;
}

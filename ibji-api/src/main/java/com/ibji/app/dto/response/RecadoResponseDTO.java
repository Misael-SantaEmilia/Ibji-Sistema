package com.ibji.app.dto.response;

import com.ibji.app.enums.PublicoAlvo;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecadoResponseDTO {

    private Long id;
    private String titulo;
    private String conteudo;
    private PublicoAlvo publicoAlvo;
    private Long ministerioId;
    private String ministerioNome;
    private Long criadoPorId;
    private String criadoPorNome;
    private LocalDate dataValidade;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}

package com.ibji.app.dto.response;

import com.ibji.app.enums.Status;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MinisterioResponseDTO {

    private Long id;
    private String descricao;
    private Long liderId;
    private String liderNome;
    private Status status;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;
    private Integer totalMembros;
    private List<MembroResponseDTO> membros;
}

package com.ibji.app.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoOracaoResponseDTO {

    private Long id;
    private Long membroId;
    private String membroNome;
    private String titulo;
    private String descricao;
    private Boolean publico;
    private Long aprovadoPorId;
    private String aprovadoPorNome;
    private LocalDateTime dataPedido;
    private LocalDateTime dataAprovacao;
}

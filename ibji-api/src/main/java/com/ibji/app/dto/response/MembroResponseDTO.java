package com.ibji.app.dto.response;

import com.ibji.app.enums.NivelAcesso;
import com.ibji.app.enums.Status;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembroResponseDTO {

    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private LocalDate dataNascimento;
    private String endereco;
    private String cidade;
    private String estado;
    private String cep;
    private NivelAcesso nivelAcesso;
    private Status status;
    private String fotoUrl;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;
}

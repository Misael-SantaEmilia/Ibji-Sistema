package com.ibji.app.dto.request;

import com.ibji.app.enums.NivelAcesso;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembroRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
    private String nome;

    @Pattern(regexp = "^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$",
             message = "CPF inválido")
    private String cpf;

    @Email(message = "Email inválido")
    @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
    private String email;

    @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
    private String telefone;

    private LocalDate dataNascimento;

    @Size(max = 255, message = "Endereço deve ter no máximo 255 caracteres")
    private String endereco;

    @Size(max = 100, message = "Cidade deve ter no máximo 100 caracteres")
    private String cidade;

    @Pattern(regexp = "^[A-Z]{2}$", message = "Estado deve ter 2 letras maiúsculas")
    private String estado;

    @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "CEP inválido")
    private String cep;

    private NivelAcesso nivelAcesso;

    @Size(max = 500, message = "URL da foto deve ter no máximo 500 caracteres")
    private String fotoUrl;
}

package com.ibji.app.dto.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AniversarianteResponseDTO {

    private Long id;
    private String nome;
    private LocalDate dataNascimento;
    private Integer idade;
    private String email;
    private String telefone;
}

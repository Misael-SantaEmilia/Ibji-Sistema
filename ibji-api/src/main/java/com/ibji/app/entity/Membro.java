package com.ibji.app.entity;

import com.ibji.app.enums.NivelAcesso;
import com.ibji.app.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "membros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Membro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(unique = true, length = 14)
    private String cpf;

    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String telefone;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(length = 255)
    private String endereco;

    @Column(length = 100)
    private String cidade;

    @Column(length = 2)
    private String estado;

    @Column(length = 10)
    private String cep;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_acesso", nullable = false, length = 10)
    @Builder.Default
    private NivelAcesso nivelAcesso = NivelAcesso.MEMBRO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    @Builder.Default
    private Status status = Status.ATIVO;

    @Column(name = "foto_url", length = 500)
    private String fotoUrl;

    @Column(name = "data_cadastro", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime dataCadastro = LocalDateTime.now();

    @Column(name = "data_atualizacao", nullable = false)
    @Builder.Default
    private LocalDateTime dataAtualizacao = LocalDateTime.now();

    @OneToMany(mappedBy = "lider", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private List<Ministerio> ministeriosLiderados = new ArrayList<>();

    @OneToMany(mappedBy = "membro", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private List<MinisterioMembro> ministerios = new ArrayList<>();

    @PreUpdate
    protected void onUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }
}

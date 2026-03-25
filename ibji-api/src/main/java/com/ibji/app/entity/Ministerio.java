package com.ibji.app.entity;

import com.ibji.app.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ministerios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ministerio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lider_id", nullable = false, foreignKey = @ForeignKey(name = "fk_ministerio_lider"))
    private Membro lider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    @Builder.Default
    private Status status = Status.ATIVO;

    @Column(name = "data_cadastro", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime dataCadastro = LocalDateTime.now();

    @Column(name = "data_atualizacao", nullable = false)
    @Builder.Default
    private LocalDateTime dataAtualizacao = LocalDateTime.now();

    @OneToMany(mappedBy = "ministerio", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<MinisterioMembro> membros = new ArrayList<>();

    @PreUpdate
    protected void onUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }
}

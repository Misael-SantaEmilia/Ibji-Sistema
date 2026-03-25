package com.ibji.app.entity;

import com.ibji.app.enums.PublicoAlvo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "recados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String conteudo;

    @Enumerated(EnumType.STRING)
    @Column(name = "publico_alvo", nullable = false, length = 15)
    private PublicoAlvo publicoAlvo;

    @Column(name = "ministerio_id")
    private Long ministerioId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ministerio_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_recados_ministerio"))
    @ToString.Exclude
    private Ministerio ministerio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criado_por", nullable = false, foreignKey = @ForeignKey(name = "fk_recados_criador"))
    private Membro criadoPor;

    @Column(name = "data_validade")
    private LocalDate dataValidade;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(name = "data_atualizacao", nullable = false)
    @Builder.Default
    private LocalDateTime dataAtualizacao = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }
}

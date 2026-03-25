package com.ibji.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "escalas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Escala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ministerio_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_escala_ministerio"))
    private Ministerio ministerio;

    @Column(name = "data_escala", nullable = false)
    private LocalDate dataEscala;

    @Column(name = "horario_inicio", nullable = false)
    private LocalTime horarioInicio;

    @Column(name = "horario_fim", nullable = false)
    private LocalTime horarioFim;

    @Column(length = 255)
    private String observacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criado_por", nullable = false,
                foreignKey = @ForeignKey(name = "fk_escala_criador"))
    private Membro criadoPor;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @OneToMany(mappedBy = "escala", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<EscalaMembro> membros = new ArrayList<>();
}

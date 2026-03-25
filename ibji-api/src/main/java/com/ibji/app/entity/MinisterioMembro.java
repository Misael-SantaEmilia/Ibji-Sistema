package com.ibji.app.entity;

import com.ibji.app.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "ministerio_membros",
       uniqueConstraints = @UniqueConstraint(columnNames = {"ministerio_id", "membro_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MinisterioMembro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ministerio_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_mm_ministerio"))
    @ToString.Exclude
    private Ministerio ministerio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membro_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_mm_membro"))
    private Membro membro;

    @Column(name = "data_entrada", nullable = false)
    @Builder.Default
    private LocalDate dataEntrada = LocalDate.now();

    @Column(name = "data_saida")
    private LocalDate dataSaida;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    @Builder.Default
    private Status status = Status.ATIVO;
}

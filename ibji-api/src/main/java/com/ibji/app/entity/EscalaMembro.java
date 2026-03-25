package com.ibji.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "escala_membros",
       uniqueConstraints = @UniqueConstraint(columnNames = {"escala_id", "membro_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EscalaMembro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escala_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_em_escala"))
    @ToString.Exclude
    private Escala escala;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membro_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_em_membro"))
    private Membro membro;
}

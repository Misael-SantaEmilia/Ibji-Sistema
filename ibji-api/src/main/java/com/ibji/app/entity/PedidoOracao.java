package com.ibji.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos_oracao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoOracao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membro_id", foreignKey = @ForeignKey(name = "fk_po_membro"))
    private Membro membro;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false)
    @Builder.Default
    private Boolean publico = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aprovado_por", foreignKey = @ForeignKey(name = "fk_po_aprovador"))
    private Membro aprovadoPor;

    @Column(name = "data_pedido", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime dataPedido = LocalDateTime.now();

    @Column(name = "data_aprovacao")
    private LocalDateTime dataAprovacao;
}

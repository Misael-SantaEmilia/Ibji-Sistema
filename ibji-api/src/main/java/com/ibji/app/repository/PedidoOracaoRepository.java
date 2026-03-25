package com.ibji.app.repository;

import com.ibji.app.entity.PedidoOracao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoOracaoRepository extends JpaRepository<PedidoOracao, Long> {

    List<PedidoOracao> findByPublicoTrueOrderByDataPedidoDesc();

    Page<PedidoOracao> findByPublico(Boolean publico, Pageable pageable);

    List<PedidoOracao> findByMembroIdOrderByDataPedidoDesc(Long membroId);

    @Query("SELECT p FROM PedidoOracao p WHERE p.publico = true " +
           "ORDER BY p.dataPedido DESC")
    List<PedidoOracao> findPedidosAprovados();

    @Query("SELECT p FROM PedidoOracao p WHERE p.publico = false " +
           "ORDER BY p.dataPedido ASC")
    List<PedidoOracao> findPedidosPendentes();

    @Query("SELECT COUNT(p) FROM PedidoOracao p WHERE p.publico = false")
    Long countPedidosPendentes();
}

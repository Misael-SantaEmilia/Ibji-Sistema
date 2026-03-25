package com.ibji.app.repository;

import com.ibji.app.entity.Escala;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EscalaRepository extends JpaRepository<Escala, Long> {

    List<Escala> findByDataEscalaOrderByHorarioInicio(LocalDate dataEscala);

    List<Escala> findByMinisterioIdOrderByDataEscalaDesc(Long ministerioId);

    Page<Escala> findByMinisterioId(Long ministerioId, Pageable pageable);

    @Query("SELECT e FROM Escala e WHERE e.dataEscala BETWEEN :inicio AND :fim " +
           "ORDER BY e.dataEscala, e.horarioInicio")
    List<Escala> findByPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    @Query("SELECT e FROM Escala e WHERE e.ministerio.id = :ministerioId " +
           "AND e.dataEscala BETWEEN :inicio AND :fim " +
           "ORDER BY e.dataEscala, e.horarioInicio")
    List<Escala> findByMinisterioAndPeriodo(
            @Param("ministerioId") Long ministerioId,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim);

    @Query("SELECT e FROM Escala e WHERE e.dataEscala >= :dataAtual " +
           "ORDER BY e.dataEscala, e.horarioInicio")
    List<Escala> findEscalasFuturas(@Param("dataAtual") LocalDate dataAtual);

    @Query("SELECT e FROM Escala e JOIN FETCH e.ministerio " +
           "WHERE e.criadoPor.id = :liderId ORDER BY e.dataEscala DESC")
    List<Escala> findByLider(@Param("liderId") Long liderId);
}

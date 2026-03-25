package com.ibji.app.repository;

import com.ibji.app.entity.Recado;
import com.ibji.app.enums.PublicoAlvo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RecadoRepository extends JpaRepository<Recado, Long> {

    List<Recado> findByPublicoAlvoAndDataValidadeGreaterThanEqualOrderByDataCriacaoDesc(
            PublicoAlvo publicoAlvo, LocalDate dataAtual);

    Page<Recado> findByPublicoAlvo(PublicoAlvo publicoAlvo, Pageable pageable);

    List<Recado> findByMinisterioIdAndDataValidadeGreaterThanEqualOrderByDataCriacaoDesc(
            Long ministerioId, LocalDate dataAtual);

    @Query("SELECT r FROM Recado r WHERE " +
           "(r.publicoAlvo = 'IGREJA' OR (r.publicoAlvo = 'MINISTERIO' AND r.ministerio.id = :ministerioId)) " +
           "AND (r.dataValidade IS NULL OR r.dataValidade >= :dataAtual) " +
           "ORDER BY r.dataCriacao DESC")
    List<Recado> findRecadosVisiveis(@Param("ministerioId") Long ministerioId,
                                     @Param("dataAtual") LocalDate dataAtual);

    @Query("SELECT r FROM Recado r WHERE r.publicoAlvo = 'IGREJA' " +
           "AND (r.dataValidade IS NULL OR r.dataValidade >= :dataAtual) " +
           "ORDER BY r.dataCriacao DESC")
    List<Recado> findRecadosIgreja(@Param("dataAtual") LocalDate dataAtual);

    @Query("SELECT r FROM Recado r WHERE r.dataValidade < :dataAtual")
    List<Recado> findRecadosExpirados(@Param("dataAtual") LocalDate dataAtual);
}

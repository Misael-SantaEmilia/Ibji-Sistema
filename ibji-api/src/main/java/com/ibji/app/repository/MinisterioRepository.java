package com.ibji.app.repository;

import com.ibji.app.entity.Ministerio;
import com.ibji.app.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MinisterioRepository extends JpaRepository<Ministerio, Long> {

    List<Ministerio> findByStatus(Status status);

    Page<Ministerio> findByStatus(Status status, Pageable pageable);

    List<Ministerio> findByLiderId(Long liderId);

    List<Ministerio> findByLiderIdAndStatus(Long liderId, Status status);

    Optional<Ministerio> findByIdAndStatus(Long id, Status status);

    @Query("SELECT m FROM Ministerio m WHERE m.status = 'ATIVO' " +
           "AND LOWER(m.descricao) LIKE LOWER(CONCAT('%', :descricao, '%'))")
    Page<Ministerio> findByDescricaoContaining(@Param("descricao") String descricao, Pageable pageable);

    @Query("SELECT m FROM Ministerio m JOIN FETCH m.lider WHERE m.id = :id")
    Optional<Ministerio> findByIdWithLider(@Param("id") Long id);

    @Query("SELECT COUNT(mm) FROM MinisterioMembro mm " +
           "WHERE mm.ministerio.id = :ministerioId AND mm.status = 'ATIVO'")
    Long countMembrosByMinisterio(@Param("ministerioId") Long ministerioId);
}

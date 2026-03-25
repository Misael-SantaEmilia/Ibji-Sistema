package com.ibji.app.repository;

import com.ibji.app.entity.MinisterioMembro;
import com.ibji.app.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MinisterioMembroRepository extends JpaRepository<MinisterioMembro, Long> {

    List<MinisterioMembro> findByMinisterioIdAndStatus(Long ministerioId, Status status);

    List<MinisterioMembro> findByMembroIdAndStatus(Long membroId, Status status);

    Optional<MinisterioMembro> findByMinisterioIdAndMembroId(Long ministerioId, Long membroId);

    boolean existsByMinisterioIdAndMembroIdAndStatus(Long ministerioId, Long membroId, Status status);

    @Query("SELECT mm FROM MinisterioMembro mm " +
           "WHERE mm.ministerio.id = :ministerioId AND mm.status = 'ATIVO' " +
           "ORDER BY mm.membro.nome")
    List<MinisterioMembro> findMembrosAtivosByMinisterio(@Param("ministerioId") Long ministerioId);

    @Query("SELECT mm FROM MinisterioMembro mm " +
           "WHERE mm.membro.id = :membroId AND mm.status = 'ATIVO' " +
           "ORDER BY mm.ministerio.descricao")
    List<MinisterioMembro> findMinisteriosAtivosByMembro(@Param("membroId") Long membroId);
}

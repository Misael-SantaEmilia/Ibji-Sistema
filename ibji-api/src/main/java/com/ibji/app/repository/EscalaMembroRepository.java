package com.ibji.app.repository;

import com.ibji.app.entity.EscalaMembro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EscalaMembroRepository extends JpaRepository<EscalaMembro, Long> {

    List<EscalaMembro> findByEscalaId(Long escalaId);

    List<EscalaMembro> findByMembroId(Long membroId);

    Optional<EscalaMembro> findByEscalaIdAndMembroId(Long escalaId, Long membroId);

    boolean existsByEscalaIdAndMembroId(Long escalaId, Long membroId);

    @Query("SELECT em FROM EscalaMembro em " +
           "WHERE em.escala.id = :escalaId ORDER BY em.membro.nome")
    List<EscalaMembro> findByEscalaIdOrdered(@Param("escalaId") Long escalaId);

    void deleteByEscalaIdAndMembroId(Long escalaId, Long membroId);
}

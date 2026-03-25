package com.ibji.app.repository;

import com.ibji.app.entity.Membro;
import com.ibji.app.enums.NivelAcesso;
import com.ibji.app.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MembroRepository extends JpaRepository<Membro, Long> {

    Optional<Membro> findByCpf(String cpf);

    Optional<Membro> findByEmail(String email);

    List<Membro> findByStatus(Status status);

    Page<Membro> findByStatus(Status status, Pageable pageable);

    List<Membro> findByNivelAcesso(NivelAcesso nivelAcesso);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    @Query("SELECT m FROM Membro m WHERE m.status = :status " +
           "AND LOWER(m.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    Page<Membro> findByStatusAndNomeContaining(
            @Param("status") Status status,
            @Param("nome") String nome,
            Pageable pageable);

    @Query("SELECT m FROM Membro m WHERE FUNCTION('MONTH', m.dataNascimento) = :mes " +
           "AND FUNCTION('DAY', m.dataNascimento) = :dia AND m.status = 'ATIVO'")
    List<Membro> findAniversariantesDoDia(@Param("dia") int dia, @Param("mes") int mes);

    @Query("SELECT m FROM Membro m WHERE FUNCTION('MONTH', m.dataNascimento) = :mes " +
           "AND m.status = 'ATIVO' ORDER BY FUNCTION('DAY', m.dataNascimento)")
    List<Membro> findAniversariantesDoMes(@Param("mes") int mes);

    @Query("SELECT m FROM Membro m WHERE m.status = 'ATIVO' " +
           "AND m.dataNascimento BETWEEN :inicio AND :fim")
    List<Membro> findAniversariantesDaSemana(
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim);

    @Query("SELECT m FROM Membro m WHERE m.status = 'ATIVO' AND m.nivelAcesso = 'LIDER'")
    List<Membro> findAllLideres();

    @Query("SELECT m FROM Membro m LEFT JOIN FETCH m.ministerios mm " +
           "WHERE mm.ministerio.id = :ministerioId AND mm.status = 'ATIVO'")
    List<Membro> findMembrosByMinisterio(@Param("ministerioId") Long ministerioId);
}

package com.unipiagte.uniescola.repository;

import com.unipiagte.uniescola.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    List<Aluno> findByEscolaId(Long escolaId);
    Optional<Aluno> findByMatricula(String matricula);
    
    @Query("SELECT a FROM Aluno a WHERE LOWER(a.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Aluno> findByNomeContaining(String nome);
    
    boolean existsByMatricula(String matricula);
    
    @Query("SELECT COUNT(a) FROM Aluno a WHERE a.escola.id = :escolaId")
    long countByEscolaId(Long escolaId);
}
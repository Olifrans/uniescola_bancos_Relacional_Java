package com.unipiagte.uniescola.repository;


import com.unipiagte.uniescola.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    List<Professor> findByEscolaId(Long escolaId);
    
    @Query("SELECT p FROM Professor p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Professor> findByNomeContaining(String nome);
    
    List<Professor> findByEspecialidadeIgnoreCase(String especialidade);
    
    boolean existsByEmail(String email);
}
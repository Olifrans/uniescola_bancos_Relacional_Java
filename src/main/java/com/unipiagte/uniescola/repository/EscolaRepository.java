package com.unipiagte.uniescola.repository;

import com.unipiagte.uniescola.entity.Escola;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EscolaRepository extends JpaRepository<Escola, Long> {
    Optional<Escola> findByCnpj(String cnpj);
    
    @Query("SELECT e FROM Escola e WHERE LOWER(e.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Escola> findByNomeContaining(String nome);
    
    boolean existsByCnpj(String cnpj);
}
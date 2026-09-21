package com.unipiagte.uniescola.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "alunos")
public class Aluno {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Column(length = 20, unique = true)
    private String matricula;
    
    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;
    
    @Column(length = 150)
    private String email;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "escola_id", nullable = false)
    @JsonIgnoreProperties({"professores", "alunos", "hibernateLazyInitializer", "handler"})
    private Escola escola;
    
    // --- GETTERS AND SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Escola getEscola() { return escola; }
    public void setEscola(Escola escola) { this.escola = escola; }
}
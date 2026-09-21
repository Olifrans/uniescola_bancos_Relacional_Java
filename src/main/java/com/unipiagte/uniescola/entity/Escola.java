package com.unipiagte.uniescola.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "escolas")
public class Escola {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(length = 18, unique = true)
    private String cnpj;

    @Column(length = 255)
    private String endereco;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @OneToMany(mappedBy = "escola", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // <--- IMPEDIR LazyInitializationException E LOOP INFINITO
    private List<Professor> professores = new ArrayList<>();

    @OneToMany(mappedBy = "escola", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // <--- IMPEDIR LazyInitializationException E LOOP INFINITO
    private List<Aluno> alunos = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
    }

    // --- GETTERS AND SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }

    public List<Professor> getProfessores() { return professores; }
    public void setProfessores(List<Professor> professores) { this.professores = professores; }

    public List<Aluno> getAlunos() { return alunos; }
    public void setAlunos(List<Aluno> alunos) { this.alunos = alunos; }
}
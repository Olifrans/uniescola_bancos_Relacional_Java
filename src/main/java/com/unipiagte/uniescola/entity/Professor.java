package com.unipiagte.uniescola.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "professores")
public class Professor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Column(length = 150, unique = true)
    private String email;
    
    @Column(length = 80)
    private String especialidade;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal salario;
    
    @Column(name = "data_contratacao")
    private LocalDate dataContratacao;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "escola_id", nullable = false)
    @JsonIgnoreProperties({"professores", "alunos", "hibernateLazyInitializer", "handler"})
    private Escola escola;
    
    // --- GETTERS AND SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }

    public BigDecimal getSalario() { return salario; }
    public void setSalario(BigDecimal salario) { this.salario = salario; }

    public LocalDate getDataContratacao() { return dataContratacao; }
    public void setDataContratacao(LocalDate dataContratacao) { this.dataContratacao = dataContratacao; }

    public Escola getEscola() { return escola; }
    public void setEscola(Escola escola) { this.escola = escola; }
}
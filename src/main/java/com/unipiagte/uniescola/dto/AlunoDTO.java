package com.unipiagte.uniescola.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record AlunoDTO(
    Long id,
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100)
    String nome,
    
    @Size(max = 20)
    String matricula,
    
    @Past(message = "Data de nascimento deve ser no passado")
    LocalDate dataNascimento,
    
    @Email(message = "Email inválido")
    String email,
    
    @NotNull(message = "Escola é obrigatória")
    Long escolaId
) {}
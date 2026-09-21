package com.unipiagte.uniescola.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record AlunoDTO(
    Long id,
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    String nome,
    
    @NotBlank(message = "Matrícula é obrigatória")
    @Size(max = 20, message = "Matrícula deve ter no máximo 20 caracteres")
    String matricula,
    
    @Past(message = "A data de nascimento deve ser no passado")
    LocalDate dataNascimento,
    
    @Email(message = "Formato de e-mail inválido")
    String email,
    
    @NotNull(message = "A escola é obrigatória")
    Long escolaId
) {}
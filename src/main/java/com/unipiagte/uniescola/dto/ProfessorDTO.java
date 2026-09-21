package com.unipiagte.uniescola.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ProfessorDTO(
    Long id,
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    String nome,
    
    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    String email,
    
    String especialidade,
    
    @DecimalMin(value = "0.0", inclusive = true, message = "O salário não pode ser negativo")
    BigDecimal salario,
    
    LocalDate dataContratacao,
    
    @NotNull(message = "A escola é obrigatória")
    Long escolaId
) {}
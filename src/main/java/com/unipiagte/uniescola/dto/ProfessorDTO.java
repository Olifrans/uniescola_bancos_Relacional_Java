package com.unipiagte.uniescola.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ProfessorDTO(
    Long id,
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100)
    String nome,
    
    @Email(message = "Email inválido")
    String email,
    
    String especialidade,
    
    @DecimalMin(value = "0.0", message = "Salário não pode ser negativo")
    BigDecimal salario,
    
    LocalDate dataContratacao,
    
    @NotNull(message = "Escola é obrigatória")
    Long escolaId
) {}
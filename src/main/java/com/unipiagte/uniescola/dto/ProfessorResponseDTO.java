// package com.unipiagte.uniescola.dto;

// import jakarta.validation.constraints.*;
// import java.math.BigDecimal;
// import java.time.LocalDate;

// public record ProfessorDTO(
//     Long id,
    
//     @NotBlank(message = "Nome é obrigatório")
//     @Size(min = 3, max = 100)
//     String nome,
    
//     @Email(message = "Email inválido")
//     String email,
    
//     String especialidade,
    
//     @DecimalMin(value = "0.0", message = "Salário não pode ser negativo")
//     BigDecimal salario,
    
//     LocalDate dataContratacao,
    
//     @NotNull(message = "Escola é obrigatória")
//     Long escolaId
// ) {}


package com.unipiagte.uniescola.dto;

import com.unipiagte.uniescola.entity.Professor;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ProfessorResponseDTO(
    Long id,
    String nome,
    String email,
    String especialidade,
    BigDecimal salario,
    LocalDate dataContratacao,
    EscolaSimplesDTO escola
) {
    public record EscolaSimplesDTO(Long id, String nome) {}
    
    public static ProfessorResponseDTO fromEntity(Professor entity) {
        EscolaSimplesDTO escolaDTO = entity.getEscola() != null 
            ? new EscolaSimplesDTO(entity.getEscola().getId(), entity.getEscola().getNome())
            : null;
            
        return new ProfessorResponseDTO(
            entity.getId(),
            entity.getNome(),
            entity.getEmail(),
            entity.getEspecialidade(),
            entity.getSalario(),
            entity.getDataContratacao(),
            escolaDTO
        );
    }
}
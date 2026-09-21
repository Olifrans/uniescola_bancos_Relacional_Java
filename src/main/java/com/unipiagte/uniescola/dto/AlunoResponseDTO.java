// package com.unipiagte.uniescola.dto;

// import jakarta.validation.constraints.*;
// import java.time.LocalDate;

// public record AlunoDTO(
//     Long id,
    
//     @NotBlank(message = "Nome é obrigatório")
//     @Size(min = 3, max = 100)
//     String nome,
    
//     @Size(max = 20)
//     String matricula,
    
//     @Past(message = "Data de nascimento deve ser no passado")
//     LocalDate dataNascimento,
    
//     @Email(message = "Email inválido")
//     String email,
    
//     @NotNull(message = "Escola é obrigatória")
//     Long escolaId
// ) {}



package com.unipiagte.uniescola.dto;

import com.unipiagte.uniescola.entity.Aluno;
import java.time.LocalDate;

public record AlunoResponseDTO(
    Long id,
    String nome,
    String matricula,
    LocalDate dataNascimento,
    String email,
    EscolaSimplesDTO escola
) {
    public record EscolaSimplesDTO(Long id, String nome) {}
    
    public static AlunoResponseDTO fromEntity(Aluno entity) {
        AlunoResponseDTO.EscolaSimplesDTO escolaDTO = entity.getEscola() != null
            ? new AlunoResponseDTO.EscolaSimplesDTO(entity.getEscola().getId(), entity.getEscola().getNome())
            : null;
            
        return new AlunoResponseDTO(
            entity.getId(),
            entity.getNome(),
            entity.getMatricula(),
            entity.getDataNascimento(),
            entity.getEmail(),
            escolaDTO
        );
    }
}
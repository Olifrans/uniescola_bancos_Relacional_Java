// package com.unipiagte.uniescola.dto;

// import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.Size;

// public record EscolaDTO(
//     Long id,
//     @NotBlank(message = "Nome é obrigatório")
//     @Size(min = 3, max = 150, message = "Nome deve ter entre 3 e 150 caracteres")
//     String nome,
    
//     @Size(max = 18, message = "CNPJ inválido")
//     String cnpj,
    
//     String endereco
// ) {
//     public EscolaDTO {
//         if (cnpj != null) cnpj = cnpj.replaceAll("[^0-9]", "");
//     }
// }

package com.unipiagte.uniescola.dto;

import com.unipiagte.uniescola.entity.Escola;
import java.time.LocalDateTime;

public record EscolaResponseDTO(
    Long id,
    String nome,
    String cnpj,
    String endereco,
    LocalDateTime dataCriacao
) {
    public static EscolaResponseDTO fromEntity(Escola entity) {
        return new EscolaResponseDTO(
            entity.getId(),
            entity.getNome(),
            entity.getCnpj(),
            entity.getEndereco(),
            entity.getDataCriacao()
        );
    }
}
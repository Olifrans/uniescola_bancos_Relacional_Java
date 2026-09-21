package com.unipiagte.uniescola.dto;



import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
    @NotBlank(message = "Usuário é obrigatório")
    String username,
    
    @NotBlank(message = "Senha é obrigatória")
    String password
) {}
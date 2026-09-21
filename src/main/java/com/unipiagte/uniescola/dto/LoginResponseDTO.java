package com.unipiagte.uniescola.dto;



public record LoginResponseDTO(
    String token,
    String username,
    String nome
) {}
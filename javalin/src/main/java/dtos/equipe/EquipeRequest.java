package com.recomendador.dtos.request;

public record GerarEquipesRequestDTO(
        int quantidade,
        boolean porTamanho,
        String estrategia
) {}
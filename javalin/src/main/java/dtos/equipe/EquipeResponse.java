package com.recomendador.dtos.response;

import com.recomendador.models.Equipe;
import java.util.List;
import java.util.stream.Collectors;

public record EquipeResponseDTO(String nome, List<PessoaResponseDTO> integrantes) {
    public static EquipeResponseDTO fromModel(Equipe equipe) {
        List<PessoaResponseDTO> integrantesDto = equipe.getIntegrantes().stream()
                .map(PessoaResponseDTO::fromModel)
                .collect(Collectors.toList());
        return new EquipeResponseDTO(equipe.getNome(), integrantesDto);
    }
}
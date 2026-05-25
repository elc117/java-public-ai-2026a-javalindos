package com.recomendador.dtos.response;

import com.recomendador.models.Pessoa;
import com.recomendador.models.Equipe;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

public record PessoaResponseDTO(String id, String nome, Map<String, String> atributos) {
    public static PessoaResponseDTO fromModel(Pessoa pessoa) {
        return new PessoaResponseDTO(pessoa.getId(), pessoa.getNome(), pessoa.getAtributos());
    }
}
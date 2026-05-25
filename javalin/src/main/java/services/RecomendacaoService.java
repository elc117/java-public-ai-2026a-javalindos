package com.recomendador.services;

import com.recomendador.models.Equipe;
import com.recomendador.models.Turma;
import com.recomendador.repositories.TurmaRepository;
import com.recomendador.strategies.RecomendacaoStrategy;

import java.util.List;

public class RecomendacaoService {
    private final TurmaRepository repository;

    public RecomendacaoService(TurmaRepository repository) {
        this.repository = repository;
    }

    public List<Equipe> gerarEValidarEquipes(String turmaId, int quantidade, boolean porTamanho, RecomendacaoStrategy estrategia) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        Turma turma = repository.buscarPorId(turmaId)
                .orElseThrow(() -> new IllegalArgumentException("Turma não encontrada."));

        if (turma.getMembros().isEmpty()) {
            throw new IllegalStateException("A turma está vazia.");
        }

        return estrategia.gerarEquipes(turma, quantidade, porTamanho);
    }
}
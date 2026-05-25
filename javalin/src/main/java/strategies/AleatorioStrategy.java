package com.recomendador.strategies;

import com.recomendador.models.Equipe;
import com.recomendador.models.Pessoa;
import com.recomendador.models.Turma;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AleatorioStrategy implements RecomendacaoStrategy {
    @Override
    public List<Equipe> gerarEquipes(Turma turma, int quantidade, boolean porTamanho) {
        List<Pessoa> membros = new ArrayList<>(turma.getMembros());
        Collections.shuffle(membros); // Mistura a turma

        List<Equipe> equipes = new ArrayList<>();
        // Lógica simplificada de exemplo:
        equipes.add(new Equipe("Equipe 1", membros));

        return equipes;
    }
}
package com.recomendador.strategies;

import com.recomendador.models.Equipe;
import com.recomendador.models.Turma;
import java.util.List;

public interface RecomendacaoStrategy {
    List<Equipe> gerarEquipes(Turma turma, int quantidade, boolean porTamanho);
}
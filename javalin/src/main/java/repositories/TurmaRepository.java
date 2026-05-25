package com.recomendador.repositories;

import com.recomendador.models.Turma;
import java.util.Optional;

public interface TurmaRepository {
    Optional<Turma> buscarPorId(String id);
    void salvar(Turma turma);
}
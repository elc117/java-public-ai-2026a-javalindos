package com.recomendador;

import com.recomendador.controllers.RecomendacaoController;
import com.recomendador.repositories.TurmaRepository;
// Importe sua implementação SQLite real aqui: import com.recomendador.repositories.SqliteTurmaRepository;
import com.recomendador.services.RecomendacaoService;
import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        // 1. Injeção de Dependências (Montagem)
        TurmaRepository repository = null; // Instancie o seu SqliteTurmaRepository real aqui
        RecomendacaoService service = new RecomendacaoService(repository);
        RecomendacaoController controller = new RecomendacaoController(service);

        // 2. Configuração do Servidor
        Javalin app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> {
                cors.add(it -> it.anyHost()); // Permite o Frontend (React/Vue/Angular) acessar a API
            });
        }).start(7070);

        // 3. Registro de Rotas
        app.post("/turmas/{id}/gerar-equipes", controller::gerarEquipes);

        System.out.println("Servidor Javalin rodando na porta 7070!");
    }
}
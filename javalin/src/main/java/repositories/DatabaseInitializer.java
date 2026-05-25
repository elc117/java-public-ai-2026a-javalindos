package com.recomendador.repositories;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void inicializarBanco() {
        String sqlTurmas = """
            CREATE TABLE IF NOT EXISTS turmas (
                id TEXT PRIMARY KEY,
                nome TEXT NOT NULL
            );
        """;

        String sqlPessoas = """
            CREATE TABLE IF NOT EXISTS pessoas (
                id TEXT PRIMARY KEY,
                nome TEXT NOT NULL
            );
        """;

        // Tabela de junção (Muitos-para-Muitos) para permitir que turmas reutilizem pessoas
        String sqlTurmaPessoas = """
            CREATE TABLE IF NOT EXISTS turma_pessoas (
                turma_id TEXT,
                pessoa_id TEXT,
                PRIMARY KEY (turma_id, pessoa_id),
                FOREIGN KEY (turma_id) REFERENCES turmas(id) ON DELETE CASCADE,
                FOREIGN KEY (pessoa_id) REFERENCES pessoas(id) ON DELETE CASCADE
            );
        """;

        // Tabela para guardar os atributos dinâmicos (Chave-Valor) de cada pessoa
        String sqlAtributos = """
            CREATE TABLE IF NOT EXISTS atributos_pessoa (
                pessoa_id TEXT,
                chave TEXT,
                valor TEXT,
                PRIMARY KEY (pessoa_id, chave),
                FOREIGN KEY (pessoa_id) REFERENCES pessoas(id) ON DELETE CASCADE
            );
        """;

        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement()) {

            // Ativa o suporte a Chaves Estrangeiras no SQLite
            stmt.execute("PRAGMA foreign_keys = ON;");

            stmt.execute(sqlTurmas);
            stmt.execute(sqlPessoas);
            stmt.execute(sqlTurmaPessoas);
            stmt.execute(sqlAtributos);

        } catch (SQLException e) {
            System.err.println("Erro ao inicializar as tabelas do banco de dados: " + e.getMessage());
        }
    }
}
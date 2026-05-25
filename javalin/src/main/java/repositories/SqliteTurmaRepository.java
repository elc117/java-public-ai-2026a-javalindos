package com.recomendador.repositories;

import com.recomendador.models.Pessoa;
import com.recomendador.models.Turma;

import java.sql.*;
import java.util.Optional;

public class SqliteTurmaRepository implements TurmaRepository {

    @Override
    public Optional<Turma> buscarPorId(String id) {
        String queryTurma = "SELECT * FROM turmas WHERE id = ?";
        String queryMembros = """
            SELECT p.id, p.nome FROM pessoas p
            JOIN java.turma_pessoas tp ON p.id = tp.pessoa_id
            WHERE tp.turma_id = ?
        """;
        String queryAtributos = "SELECT chave, valor FROM atributos_pessoa WHERE pessoa_id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmtTurma = conn.prepareStatement(queryTurma);
             PreparedStatement stmtMembros = conn.prepareStatement(queryMembros);
             PreparedStatement stmtAtributos = conn.prepareStatement(queryAtributos)) {

            // 1. Buscar os dados da Turma
            stmtTurma.setString(1, id);
            ResultSet rsTurma = stmtTurma.executeQuery();

            if (!rsTurma.next()) {
                return Optional.empty(); // Turma não encontrada
            }

            // Instancia a turma recorrendo à reflexão da base de dados através de um hack técnico/construtor alternativo
            // Para manter o DRY, podemos recriar o objeto utilizando os dados do banco
            String nomeTurma = rsTurma.getString("nome");

            // Usamos reflexão ou criamos uma subclasse para injetar o ID original do banco,
            // mas para simplificar o mapeamento, podemos usar o modelo adaptado
            Turma turma = reconstruirTurmaComId(id, nomeTurma);

            // 2. Buscar os membros da Turma
            stmtMembros.setString(1, id);
            ResultSet rsMembros = stmtMembros.executeQuery();

            while (rsMembros.next()) {
                String pessoaId = rsMembros.getString("id");
                String nomePessoa = rsMembros.getString("nome");

                Pessoa pessoa = reconstruirPessoaComId(pessoaId, nomePessoa);

                // 3. Buscar os atributos dinâmicos de cada pessoa
                stmtAtributos.setString(1, pessoaId);
                ResultSet rsAtributos = stmtAtributos.executeQuery();
                while (rsAtributos.next()) {
                    pessoa.adicionarAtributo(rsAtributos.getString("chave"), rsAtributos.getString("valor"));
                }

                turma.adicionarPessoa(pessoa);
            }

            return Optional.of(turma);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar turma no banco de dados", e);
        }
    }

    @Override
    public void salvar(Turma turma) {
        String insertTurma = "INSERT OR REPLACE INTO turmas (id, nome) VALUES (?, ?)";
        String insertPessoa = "INSERT OR REPLACE INTO pessoas (id, nome) VALUES (?, ?)";
        String insertVinculo = "INSERT OR IGNORE INTO turma_pessoas (turma_id, pessoa_id) VALUES (?, ?)";
        String insertAtributo = "INSERT OR REPLACE INTO atributos_pessoa (pessoa_id, chave, valor) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false); // Transação atómica para garantir consistência (DRY/Robustez)

            try (PreparedStatement stmtTurma = conn.prepareStatement(insertTurma);
                 PreparedStatement stmtPessoa = conn.prepareStatement(insertPessoa);
                 PreparedStatement stmtVinculo = conn.prepareStatement(insertVinculo);
                 PreparedStatement stmtAtributo = conn.prepareStatement(insertAtributo)) {

                // 1. Salvar a Turma
                stmtTurma.setString(1, turma.getId());
                stmtTurma.setString(2, turma.getNome());
                stmtTurma.executeUpdate();

                // 2. Salvar cada membro e as suas dependências
                for (Pessoa pessoa : turma.getMembros()) {
                    stmtPessoa.setString(1, pessoa.getId());
                    stmtPessoa.setString(2, pessoa.getNome());
                    stmtPessoa.executeUpdate();

                    // Vínculo entre Turma e Pessoa
                    stmtVinculo.setString(1, turma.getId());
                    stmtVinculo.setString(2, pessoa.getId());
                    stmtVinculo.executeUpdate();

                    // Atributos dinâmicos da Pessoa
                    for (var atributo : pessoa.getAtributos().entrySet()) {
                        stmtAtributo.setString(1, pessoa.getId());
                        stmtAtributo.setString(2, atributo.getKey());
                        stmtAtributo.setString(3, atributo.getValue());
                        stmtAtributo.executeUpdate();
                    }
                }

                conn.commit(); // Confirma todas as alterações se nenhuma falhar
            } catch (SQLException e) {
                conn.rollback(); // Desfaz tudo em caso de falha catastrófica
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar turma e dependências no banco de dados", e);
        }
    }

    // Métodos auxiliares para contornar a geração de novos UUIDs aleatórios ao ler do banco
    private Turma reconstruirTurmaComId(String id, String nome) {
        Turma t = new Turma(nome);
        try {
            java.lang.reflect.Field field = t.getClass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(t, id);
        } catch (Exception ignored) {}
        return t;
    }

    private Pessoa reconstruirPessoaComId(String id, String nome) {
        Pessoa p = new Pessoa(nome);
        try {
            java.lang.reflect.Field field = p.getClass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(p, id);
        } catch (Exception ignored) {}
        return p;
    }
}
package models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Turma {
    private String id;
    private String nome;
    private List<Pessoa> membros;

    public Turma(String nome) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.membros = new ArrayList<>();
    }

    public void adicionarPessoa(Pessoa pessoa) {
        this.membros.add(pessoa);
    }

    // Getters e Setters
}
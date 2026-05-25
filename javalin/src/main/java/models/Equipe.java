package models;

import java.util.List;

public class Equipe {
    private String nome;
    private List<Pessoa> integrantes;

    public Equipe(String nome, List<Pessoa> integrantes) {
        this.nome = nome;
        this.integrantes = integrantes;
    }

    // Getters e Setters
}
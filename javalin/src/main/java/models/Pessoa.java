package models;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Pessoa {
    private String id;
    private String nome;
    // Map para armazenar atributos dinâmicos (habilidades, interesses, disponibilidade)
    private Map<String, String> atributos;

    public Pessoa(String nome) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.atributos = new HashMap<>();
    }

    public void adicionarAtributo(String chave, String valor) {
        this.atributos.put(chave, valor);
    }

    // Getters e Setters
    public String getId() { return id; }
    public String getNome() { return nome; }
    public Map<String, String> getAtributos() { return atributos; }
}
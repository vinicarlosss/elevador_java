package br.ufrpe.simulador.simulador.elevadores.modelo;

public class Passageiro {
    private String nome;
    private int andarOrigem;
    private int andarDestino;

    public Passageiro(String nome, int andarOrigem, int andarDestino) {
        this.nome = nome;
        this.andarOrigem = andarOrigem;
        this.andarDestino = andarDestino;
    }

    public int getAndarOrigem() { return andarOrigem; }
    public int getAndarDestino() { return andarDestino; }
    public String getNome() { return nome; }
}
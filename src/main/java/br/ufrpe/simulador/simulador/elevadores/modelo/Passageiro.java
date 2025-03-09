package br.ufrpe.simulador.simulador.elevadores.modelo;

import java.util.Random;

//michael
public class Passageiro {
    private String nome;
    private int andarAtual;
    private int andarDestino;

    public Passageiro(String nome, int andarAtual, int andarDestino) {
        this.nome = nome;
        this.andarAtual = andarAtual;
        this.andarDestino = andarDestino;
    }

    public void solicitarElevador(Elevador elevador) {
        elevador.solicitarElevador(this, andarDestino);
    }

    public void entrarNoElevador(Elevador elevador) {
        System.out.println(nome + " entrou no " + elevador.getNome() + " e está indo para o andar " + andarDestino);
    }

    public String getNome() {
        return nome;
    }

    public int getAndarDestino() {
        return andarDestino;
    }
}

//michael
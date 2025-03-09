package br.ufrpe.simulador.simulador.elevadores.modelo;

import java.util.Random;

//michael
public class Passageiro extends Thread {
    private final int id;
    private final int andarOrigem;
    private final int andarDestino;
    private final Elevador[] elevadores;

    public Passageiro(int id, int totalAndares, Elevador[] elevadores) {
        this.id = id;
        Random rand = new Random();
        this.andarOrigem = rand.nextInt(totalAndares); // Escolhe um andar aleatório
        this.andarDestino = rand.nextInt(totalAndares);
        this.elevadores = elevadores;
    }

    @Override
    public void run() {
        System.out.println("Passageiro " + id + " está no andar " + andarOrigem + " e quer ir para " + andarDestino);
        Elevador escolhido = escolherElevadorMaisProximo();
        escolhido.moverPara(andarOrigem);
        escolhido.moverPara(andarDestino);
        System.out.println("Passageiro " + id + " chegou ao destino!");
    }

    private Elevador escolherElevadorMaisProximo() {
        Elevador maisProximo = elevadores[0];
        for (Elevador e : elevadores) {
            if (Math.abs(e.getAndarAtual() - andarOrigem) < Math.abs(maisProximo.getAndarAtual() - andarOrigem)) {
                maisProximo = e;
            }
        }
        return maisProximo;
    }
}
//michael
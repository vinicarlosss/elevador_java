package br.ufrpe.simulador.simulador.elevadores.modelo;

import java.util.concurrent.Semaphore;

public class Elevador extends Thread {
    private final int id;
    private int andarAtual;
    private final int capacidade;
    private final Semaphore semaforo; // Controle de concorrência

    public Elevador(int id, int andarInicial, int capacidade, Semaphore semaforo) {
        this.id = id;
        this.andarAtual = andarInicial;
        this.capacidade = capacidade;
        this.semaforo = semaforo;
    }

    public synchronized void moverPara(int andarDestino) {
        System.out.println("Elevador " + id + " indo para o andar " + andarDestino);
        try {
            Thread.sleep(Math.abs(andarDestino - andarAtual) * 1000); // Simula tempo de deslocamento
            andarAtual = andarDestino;
            System.out.println("Elevador " + id + " chegou ao andar " + andarAtual);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                semaforo.acquire(); // Aguarda permissão para operar
                System.out.println("Elevador " + id + " está pronto para atender chamadas.");
                Thread.sleep(2000); // Espera para receber uma chamada
                semaforo.release(); // Libera para outro elevador
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public int getAndarAtual() {
        return andarAtual;
    }
}

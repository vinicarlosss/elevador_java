package br.ufrpe.simulador.simulador.elevadores.modelo;


import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Elevador extends Thread {
    private String nome;
    private int andarAtual;
    private Queue<Passageiro> passageiros;
    private int destinoAtual;
    private Queue<Passageiro> passageirosEsperando;
    private Lock lock;

    public Elevador(String nome) {
        this.nome = nome;
        this.andarAtual = 0;
        this.passageiros = new LinkedList<>();
        this.passageirosEsperando = new LinkedList<>();
        this.destinoAtual = -1;
        this.lock = new ReentrantLock();
    }

    public void solicitarElevador(Passageiro passageiro) {
        lock.lock();
        try {
            passageirosEsperando.add(passageiro);
            passageiros.add(passageiro);
            if (destinoAtual == -1) destinoAtual = passageiro.getAndarOrigem();
        } finally {
            lock.unlock();
        }
    }

    public void removerPassageiro(Passageiro passageiro) {
        lock.lock();
        try {
            passageiros.remove(passageiro);
        } finally {
            lock.unlock();
        }
    }

    public void run() {
        while (true) {
            moverElevador();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void moverElevador() {
        lock.lock();
        try {
            if (destinoAtual == -1 && !passageirosEsperando.isEmpty()) {
                destinoAtual = passageirosEsperando.peek().getAndarOrigem();
            }
            if (destinoAtual != -1) {
                if (andarAtual < destinoAtual) andarAtual++;
                else if (andarAtual > destinoAtual) andarAtual--;
                else {
                    passageirosEsperando.removeIf(p -> p.getAndarOrigem() == andarAtual);
                    passageiros.removeIf(p -> p.getAndarDestino() == andarAtual);
                    destinoAtual = passageiros.isEmpty() ? -1 : passageiros.peek().getAndarDestino();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public int getAndarAtual() { return andarAtual; }
    public String getNome() { return nome; }
    public Queue<Passageiro> getPassageirosNoElevador() { return passageiros; }
}

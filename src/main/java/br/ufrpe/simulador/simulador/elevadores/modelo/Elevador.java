package br.ufrpe.simulador.simulador.elevadores.modelo;

import lombok.Data;

import java.util.LinkedList;
import java.util.Queue;


public class Elevador extends Thread {
    private String nome;
    private int andarAtual;
    private Queue<Passageiro> filaDeEspera;
    private boolean emMovimento;

    public Elevador(String nome) {
        this.nome = nome;
        this.andarAtual = 0; // Começa no térreo
        this.filaDeEspera = new LinkedList<>();
        this.emMovimento = false;
    }

    public synchronized void solicitarElevador(Passageiro passageiro, int andarDestino) {
        System.out.println(passageiro.getNome() + " solicitou " + nome + " para o andar " + andarDestino);
        filaDeEspera.add(passageiro);
        notify(); // Avisa o elevador que tem passageiro esperando
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                while (filaDeEspera.isEmpty()) {
                    try {
                        wait(); // Espera um passageiro chamar o elevador
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            Passageiro passageiro = filaDeEspera.poll();
            int destino = passageiro.getAndarDestino();
            moverPara(destino);
            passageiro.entrarNoElevador(this);
        }
    }

    private void moverPara(int destino) {
        System.out.println(nome + " indo para o andar " + destino);
        emMovimento = true;
        try {
            Thread.sleep(Math.abs(destino - andarAtual) * 1000); // 1 segundo por andar
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        andarAtual = destino;
        emMovimento = false;
        System.out.println(nome + " chegou ao andar " + andarAtual);
    }

    public int getAndarAtual() {
        return andarAtual;
    }

    public Queue<Passageiro> getFilaDeEspera() {
        return filaDeEspera;
    }

    public String getNome() {
        return nome;
    }
}

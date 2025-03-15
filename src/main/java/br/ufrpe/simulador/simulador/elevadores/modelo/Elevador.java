package br.ufrpe.simulador.simulador.elevadores.modelo;


import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Elevador extends Thread {
    private String nome;
    private int andarAtual;
    private int destinoAtual; // Novo atributo
    private Queue<Passageiro> passageiros;
    private Queue<Passageiro> passageirosEsperando;
    private Lock lock;
    private Direcao direcao;

    public Elevador(String nome) {
        this.nome = nome;
        this.andarAtual = 0;
        this.destinoAtual = -1; // Sem destino inicial
        this.passageiros = new LinkedList<>();
        this.passageirosEsperando = new LinkedList<>();
        this.lock = new ReentrantLock();
        this.direcao = Direcao.PARADO;
    }

    public void solicitarElevador(Passageiro passageiro) {
        lock.lock();
        try {
            // Adiciona o passageiro à lista de espera
            passageirosEsperando.add(passageiro);

            // Verifica se o elevador está parado ou em movimento
            if (destinoAtual == -1) {
                // Elevador parado, define a direção com base no andar de origem do passageiro
                if (passageiro.getAndarOrigem() > andarAtual) {
                    direcao = Direcao.SUBINDO;
                } else if (passageiro.getAndarOrigem() < andarAtual) {
                    direcao = Direcao.DESCENDO;
                } else {
                    direcao = Direcao.PARADO;
                }
                destinoAtual = passageiro.getAndarOrigem(); // O destino será o andar de origem do passageiro
            } else {
                // Elevador em movimento
                if (direcao == Direcao.SUBINDO) {
                    // Só aceita passageiros acima do andar atual, se a direção for subindo
                    if (passageiro.getAndarOrigem() > andarAtual) {
                        // O elevador já está subindo e o passageiro está em um andar acima
                        destinoAtual = passageiro.getAndarOrigem();
                    } else {
                        // Passageiro está em um andar abaixo, o elevador não vai voltar
                        System.out.println("O elevador está subindo e não pode atender a sua solicitação.");
                    }
                } else if (direcao == Direcao.DESCENDO) {
                    // Só aceita passageiros abaixo do andar atual, se a direção for descendo
                    if (passageiro.getAndarOrigem() < andarAtual) {
                        // O elevador já está descendo e o passageiro está em um andar abaixo
                        destinoAtual = passageiro.getAndarOrigem();
                    } else {
                        // Passageiro está em um andar acima, o elevador não vai subir
                        System.out.println("O elevador está descendo e não pode atender a sua solicitação.");
                    }
                }
            }

            // Exibe a solicitação e a direção atual
            System.out.println(passageiro.getNome() + " solicitou o elevador no andar " +
                    passageiro.getAndarOrigem() + " até o andar " + passageiro.getAndarDestino() +
                    " | Direção atual: " + direcao);
        } finally {
            lock.unlock();
        }
    }

    public void run() {
        while (true) {
            moverElevador();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void moverElevador() {
        lock.lock();
        try {
            embarcarPassageirosEsperando();
            if (destinoAtual == -1) return; // Sem destino, não faz nada

            if (andarAtual < destinoAtual) {
                andarAtual++;
            } else if (andarAtual > destinoAtual) {
                andarAtual--;
            } else {
                System.out.println("Elevador " + nome + " chegou ao andar " + andarAtual);

                // Desembarcar passageiros
                passageiros.removeIf(p -> {
                    if (p.getAndarDestino() == andarAtual) {
                        System.out.println(p.getNome() + " desceu no andar " + andarAtual);
                        return true;
                    }
                    return false;
                });

                // Definir novo destino
                determinarProximoDestino();
            }
        } finally {
            lock.unlock();
        }
    }

    private void determinarProximoDestino() {
        if (passageiros.isEmpty()) {
            destinoAtual = passageirosEsperando.isEmpty() ? -1 : passageirosEsperando.peek().getAndarOrigem();
        } else {
            destinoAtual = passageiros.peek().getAndarDestino();
        }
    }

    private void embarcarPassageirosEsperando() {
        Iterator<Passageiro> esperaIterator = passageirosEsperando.iterator();
        while (esperaIterator.hasNext()) {
            Passageiro p = esperaIterator.next();
            if (p.getAndarOrigem() == andarAtual) {
                System.out.println(p.getNome() + " entrou no elevador no andar " + andarAtual);
                passageiros.add(p);

                // Atualizar destino se necessário
                if (destinoAtual == -1 ||
                        (andarAtual < destinoAtual && p.getAndarDestino() > destinoAtual) ||
                        (andarAtual > destinoAtual && p.getAndarDestino() < destinoAtual)) {
                    destinoAtual = p.getAndarDestino();
                }

                esperaIterator.remove();
            }
        }
    }

    public void removerPassageiro(Passageiro passageiro) {
        lock.lock();
        try {
            passageiros.remove(passageiro);
            System.out.println(passageiro.getNome()+" desceu no andar "+this.getAndarAtual());
        } finally {
            lock.unlock();
        }
    }

    public int getAndarAtual() { return andarAtual; }
    public String getNome() { return nome; }
    public Queue<Passageiro> getPassageirosNoElevador() { return passageiros; }
    public Direcao getDirecao() {
        return direcao;
    }
    public int getDestinoAtual(){
        return this.destinoAtual;
    }
}

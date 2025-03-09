package br.ufrpe.simulador.simulador.elevadores.modelo;

import java.util.LinkedList;
import java.util.Queue;


public class Elevador {
    private String nome;
    private int andarAtual;
    private Queue<Passageiro> passageiros;
    private int destinoAtual;

    public Elevador(String nome) {
        this.nome = nome;
        this.andarAtual = 0; // Começa no andar 0
        this.passageiros = new LinkedList<>();
        this.destinoAtual = -1; // -1 indica que não há destino definido
    }

    // Agora o método solicitaElevador recebe um Passageiro
    public void solicitarElevador(Passageiro passageiro) {
        // Adiciona o passageiro na lista de passageiros
        passageiros.add(passageiro);
        // Se o elevador não tem destino, define o destino do primeiro passageiro
        if (destinoAtual == -1) {
            destinoAtual = passageiro.getAndarDestino();
        }
    }

    public boolean temDestino() {
        return destinoAtual != -1;
    }

    public void moverElevador() {
        if (temDestino()) {
            // Se o elevador está no andar correto, então ele pega o próximo passageiro
            if (andarAtual < destinoAtual) {
                andarAtual++; // Subindo
            } else if (andarAtual > destinoAtual) {
                andarAtual--; // Descendo
            }

            // Quando o elevador chega ao destino
            if (andarAtual == destinoAtual) {
                System.out.println("Elevador chegou ao andar " + andarAtual);
                // Remover passageiro do elevador ou atualizar o destino
                if (!passageiros.isEmpty()) {
                    Passageiro p = passageiros.poll();
                    destinoAtual = p.getAndarDestino();
                    System.out.println("Próximo destino: Andar " + destinoAtual);
                } else {
                    destinoAtual = -1; // Nenhum passageiro esperando
                }
            }
        }
    }

    public int getAndarAtual() {
        return andarAtual;
    }

    public String getNome() {
        return nome;
    }

    public Queue<Passageiro> getPassageirosNoElevador() {
        return passageiros;
    }

    // Método para remover um passageiro específico do elevador
    public void removerPassageiro(Passageiro passageiro) {
        // Remover passageiro pela instância (caso o passageiro tenha saído do elevador)
        passageiros.remove(passageiro);
    }
}


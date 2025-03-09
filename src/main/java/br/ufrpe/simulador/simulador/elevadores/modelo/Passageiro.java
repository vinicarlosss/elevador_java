package br.ufrpe.simulador.simulador.elevadores.modelo;

//michael
public class Passageiro {
    private String nome;
    private int andarOrigem;
    private int andarDestino;
    private int andarAtual;

    public Passageiro(String nome, int andarOrigem, int andarDestino) {
        this.nome = nome;
        this.andarOrigem = andarOrigem;
        this.andarDestino = andarDestino;
    }

    public int getAndarDestino() {
        return andarDestino;
    }

    public void solicitarElevador(Elevador elevador) {
        // Agora chamamos o método de solicitar elevador corretamente, passando apenas o passageiro
        elevador.solicitarElevador(this);
    }

    public String getNome(){
        return this.nome;
    }

    public int getAndarAtual(){
        return this.getAndarAtual();
    }
}

//michael
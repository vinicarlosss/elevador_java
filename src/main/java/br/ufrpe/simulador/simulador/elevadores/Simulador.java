package br.ufrpe.simulador.simulador.elevadores;


import br.ufrpe.simulador.simulador.elevadores.modelo.Elevador;
import br.ufrpe.simulador.simulador.elevadores.modelo.Passageiro;

import java.util.concurrent.Semaphore;

//michael
public class Simulador {
    public static void main(String[] args) {
        final int NUM_ELEVADORES = 3;
        final int NUM_PASSAGEIROS = 5;
        final int TOTAL_ANDARES = 10;

        Semaphore semaforo = new Semaphore(NUM_ELEVADORES);
        Elevador[] elevadores = new Elevador[NUM_ELEVADORES];

        // Criando os elevadores
        for (int i = 0; i < NUM_ELEVADORES; i++) {
            elevadores[i] = new Elevador(i + 1, 0, 5, semaforo);
            elevadores[i].start();
        }

        // Criando passageiros
        for (int i = 0; i < NUM_PASSAGEIROS; i++) {
            new Passageiro(i + 1, TOTAL_ANDARES, elevadores).start();
        }
    }
}
//michael

package br.ufrpe.simulador.simulador.elevadores.ui;


import br.ufrpe.simulador.simulador.elevadores.modelo.Direcao;
import br.ufrpe.simulador.simulador.elevadores.modelo.Elevador;
import br.ufrpe.simulador.simulador.elevadores.modelo.Passageiro;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class ElevadorApp extends Application {
    private Stage primaryStage;
    private Stage escolhaElevadoresStage;
    private List<Elevador> elevadores;
    private VBox listaPassageiros;
    private TextField nomePassageiroField, andarOrigemField, andarDestinoField;
    private List<Label> andarElevadorLabels;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;  // Armazena o valor de primaryStage na variável de instância

        // Janela para perguntar o número de elevadores
        escolhaElevadoresStage = new Stage();  // Agora a variável é visível em toda a classe
        escolhaElevadoresStage.setTitle("Escolha a Quantidade de Elevadores");

        TextField numeroElevadoresField = new TextField();
        Label label = new Label("Quantos elevadores você quer simular?");
        numeroElevadoresField.setTooltip(new Tooltip("Número de Elevadores"));

        Button iniciarSimuladorButton = new Button("Iniciar Simulador");
        iniciarSimuladorButton.setOnAction(e -> {
            try {
                int numeroElevadores = Integer.parseInt(numeroElevadoresField.getText());
                if (numeroElevadores < 1) {
                    mostrarErro("O número de elevadores deve ser maior que 0.");
                } else {
                    // Fechar a janela de escolha e iniciar o simulador
                    escolhaElevadoresStage.close();  // Agora funciona corretamente
                    iniciarSimulador(numeroElevadores);
                    primaryStage.show();  // Agora é possível usar o primaryStage aqui
                }
            } catch (NumberFormatException ex) {
                mostrarErro("Por favor, insira um número válido.");
            }
        });

        VBox escolhaElevadoresLayout = new VBox(10, label, numeroElevadoresField, iniciarSimuladorButton);
        escolhaElevadoresLayout.setAlignment(Pos.CENTER);
        Scene escolhaElevadoresScene = new Scene(escolhaElevadoresLayout, 300, 150);
        escolhaElevadoresStage.setScene(escolhaElevadoresScene);
        escolhaElevadoresStage.show();
    }

    private void iniciarSimulador(int numeroElevadores) {
        elevadores = new ArrayList<>();
        andarElevadorLabels = new ArrayList<>();
        listaPassageiros = new VBox(10);
        listaPassageiros.setAlignment(Pos.TOP_LEFT);

        ScrollPane scrollPane = new ScrollPane(listaPassageiros);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(150);

        // Criar os elevadores
        for (int i = 1; i <= numeroElevadores; i++) {
            Elevador elevador = new Elevador("Elevador " + (char) ('A' + i - 1));
            elevadores.add(elevador);

            Label label = new Label(elevador.getNome() + " - Andar Atual: 0");
            andarElevadorLabels.add(label);
        }

        Label nomeLabel = new Label("Nome do passageiro:");
        nomePassageiroField = new TextField();
        nomePassageiroField.setPromptText("Nome");

        Label andarOrigemLabel = new Label("Andar de origem do passageiro:");
        andarOrigemField = new TextField();
        andarOrigemField.setPromptText("Andar Origem");

        Label andarDestinoLabel = new Label("Andar de destino do passageiro:");
        andarDestinoField = new TextField();
        andarDestinoField.setPromptText("Andar Destino");

        Button adicionarPassageiroButton = new Button("Adicionar Passageiro");
        adicionarPassageiroButton.setOnAction(e -> adicionarPassageiro());

        VBox root = new VBox(10, nomeLabel, nomePassageiroField, andarOrigemLabel, andarOrigemField,
                andarDestinoLabel, andarDestinoField, adicionarPassageiroButton, scrollPane);
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-padding: 20px; -fx-background-color: #f0f0f0;");

        // Criar um layout para mostrar o status de todos os elevadores
        VBox statusElevadores = new VBox(10);
        for (Label label : andarElevadorLabels) {
            statusElevadores.getChildren().add(label);
        }

        VBox mainLayout = new VBox(10, root, statusElevadores);
        Scene mainScene = new Scene(mainLayout, 400, 500);

        // Use o primaryStage para a janela de simulação
        primaryStage.setTitle("Simulador de Elevadores");
        primaryStage.setScene(mainScene);

        // Atualizar a movimentação dos elevadores
        iniciarMovimentacaoElevadores();

        // Fechar a janela de escolha de elevadores e exibir a janela principal
        escolhaElevadoresStage.close();
        primaryStage.show();  // Exibe a janela principal (simulação)
    }

    private void iniciarMovimentacaoElevadores() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            for (Elevador elevador : elevadores) {
                elevador.moverElevador();
            }
            atualizarInterface();
            atualizarListaPassageiros();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void adicionarPassageiro() {
        String nome = nomePassageiroField.getText();
        String andarOrigemTexto = andarOrigemField.getText();
        String andarDestinoTexto = andarDestinoField.getText();

        if (!nome.isEmpty() && !andarOrigemTexto.isEmpty() && !andarDestinoTexto.isEmpty()) {
            try {
                int andarOrigem = Integer.parseInt(andarOrigemTexto);
                int andarDestino = Integer.parseInt(andarDestinoTexto);
                Passageiro passageiro = new Passageiro(nome, andarOrigem, andarDestino);
                escolherElevador(passageiro);
                atualizarInterface();
            } catch (NumberFormatException e) {
                mostrarErro("Andares devem ser números válidos.");
            }
        } else {
            mostrarErro("Preencha todos os campos.");
        }
    }

    private void escolherElevador(Passageiro passageiro) {
        Elevador melhorElevador = null;
        int menorDistancia = Integer.MAX_VALUE;

        // Verifica se há algum elevador parado (ou seja, destinoAtual == -1)
        for (Elevador elevador : elevadores) {
            if (elevador.getDestinoAtual() == -1) {
                // Elevador está parado, então escolhe o mais próximo
                int distancia = Math.abs(elevador.getAndarAtual() - passageiro.getAndarOrigem());
                if (distancia < menorDistancia) {
                    menorDistancia = distancia;
                    melhorElevador = elevador;
                }
            }
        }

        // Caso não haja elevadores parados, escolhe o elevador que está na direção correta
        if (melhorElevador == null) {
            for (Elevador elevador : elevadores) {
                if (elevador.getDirecao() == Direcao.PARADO) {
                    // Elevador está parado, pode ser escolhido
                    int distancia = Math.abs(elevador.getAndarAtual() - passageiro.getAndarOrigem());
                    if (distancia < menorDistancia) {
                        menorDistancia = distancia;
                        melhorElevador = elevador;
                    }
                } else if (elevador.getDirecao() == Direcao.SUBINDO && elevador.getAndarAtual() < passageiro.getAndarOrigem()) {
                    // Elevador subindo, verifica se está subindo em direção ao andar de origem do passageiro
                    int distancia = Math.abs(elevador.getAndarAtual() - passageiro.getAndarOrigem());
                    if (distancia < menorDistancia) {
                        menorDistancia = distancia;
                        melhorElevador = elevador;
                    }
                } else if (elevador.getDirecao() == Direcao.DESCENDO && elevador.getAndarAtual() > passageiro.getAndarOrigem()) {
                    // Elevador descendo, verifica se está descendo em direção ao andar de origem do passageiro
                    int distancia = Math.abs(elevador.getAndarAtual() - passageiro.getAndarOrigem());
                    if (distancia < menorDistancia) {
                        menorDistancia = distancia;
                        melhorElevador = elevador;
                    }
                }
            }
        }

        // Solicita o elevador escolhido
        if (melhorElevador != null) {
            melhorElevador.solicitarElevador(passageiro);
        } else {
            System.out.println("Nenhum elevador disponível para atender à solicitação.");
        }
    }

    private void atualizarListaPassageiros() {
        listaPassageiros.getChildren().clear();
        for (Elevador elevador : elevadores) {
            for (Passageiro passageiro : elevador.getPassageirosNoElevador()) {
                HBox passageiroItem = new HBox(10);
                passageiroItem.setAlignment(Pos.CENTER_LEFT);
                Label nomeLabel = new Label("Nome: " + passageiro.getNome());
                Label destinoLabel = new Label("Destino: " + passageiro.getAndarDestino());
                Label elevadorEmbarcado = new Label("Embarcou no: " + elevador.getNome());
                Button descerButton = new Button("Descer");
                descerButton.setOnAction(e -> {
                    elevador.removerPassageiro(passageiro);
                    atualizarListaPassageiros();
                });
                passageiroItem.getChildren().addAll(nomeLabel, destinoLabel, elevadorEmbarcado, descerButton);
                listaPassageiros.getChildren().add(passageiroItem);
            }
        }
    }

    private void atualizarInterface() {
        Platform.runLater(() -> {
            for (int i = 0; i < elevadores.size(); i++) {
                andarElevadorLabels.get(i).setText(elevadores.get(i).getNome() + " - Andar Atual: " + elevadores.get(i).getAndarAtual());
            }
        });
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

}
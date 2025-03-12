package br.ufrpe.simulador.simulador.elevadores.ui;

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
        numeroElevadoresField.setPromptText("Quantos elevadores você quer simular?");

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

        VBox escolhaElevadoresLayout = new VBox(10, numeroElevadoresField, iniciarSimuladorButton);
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

        // Criar os elevadores
        for (int i = 1; i <= numeroElevadores; i++) {
            Elevador elevador = new Elevador("Elevador " + (char) ('A' + i - 1));
            elevadores.add(elevador);

            Label label = new Label(elevador.getNome() + " - Andar Atual: 0");
            andarElevadorLabels.add(label);
        }

        nomePassageiroField = new TextField();
        nomePassageiroField.setPromptText("Nome");

        andarOrigemField = new TextField();
        andarOrigemField.setPromptText("Andar Origem");

        andarDestinoField = new TextField();
        andarDestinoField.setPromptText("Andar Destino");

        Button adicionarPassageiroButton = new Button("Adicionar Passageiro");
        adicionarPassageiroButton.setOnAction(e -> adicionarPassageiro());

        VBox root = new VBox(10, nomePassageiroField, andarOrigemField, andarDestinoField,
                adicionarPassageiroButton, listaPassageiros);
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
        int distanciaA = Math.abs(elevadores.get(0).getAndarAtual() - passageiro.getAndarOrigem());
        int distanciaB = Math.abs(elevadores.get(1).getAndarAtual() - passageiro.getAndarOrigem());

        if (distanciaA <= distanciaB) elevadores.get(0).solicitarElevador(passageiro);
        else elevadores.get(1).solicitarElevador(passageiro);
    }

    private void atualizarListaPassageiros() {
        listaPassageiros.getChildren().clear();
        for (Elevador elevador : elevadores) {
            for (Passageiro passageiro : elevador.getPassageirosNoElevador()) {
                HBox passageiroItem = new HBox(10);
                passageiroItem.setAlignment(Pos.CENTER_LEFT);
                Label nomeLabel = new Label("Nome: " + passageiro.getNome());
                Label destinoLabel = new Label("Destino: " + passageiro.getAndarDestino());
                Button descerButton = new Button("Descer");
                descerButton.setOnAction(e -> {
                    elevador.removerPassageiro(passageiro);
                    atualizarListaPassageiros();
                });
                passageiroItem.getChildren().addAll(nomeLabel, destinoLabel, descerButton);
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

    public static void main(String[] args) {
        launch(args);
    }
}

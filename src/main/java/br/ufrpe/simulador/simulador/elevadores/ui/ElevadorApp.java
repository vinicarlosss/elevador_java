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

public class ElevadorApp extends Application {
    private Elevador elevador1, elevador2;
    private VBox listaPassageiros;
    private TextField nomePassageiroField, andarOrigemField, andarDestinoField;
    private Label andarElevador1Label, andarElevador2Label;

    @Override
    public void start(Stage stage) {
        elevador1 = new Elevador("Elevador A");
        elevador2 = new Elevador("Elevador B");

        nomePassageiroField = new TextField();
        nomePassageiroField.setPromptText("Nome");

        andarOrigemField = new TextField();
        andarOrigemField.setPromptText("Andar Origem");

        andarDestinoField = new TextField();
        andarDestinoField.setPromptText("Andar Destino");

        Button adicionarPassageiroButton = new Button("Adicionar Passageiro");
        adicionarPassageiroButton.setOnAction(e -> adicionarPassageiro());

        listaPassageiros = new VBox(10);
        listaPassageiros.setAlignment(Pos.TOP_LEFT);
        andarElevador1Label = new Label("Elevador A - Andar Atual: 0");
        andarElevador2Label = new Label("Elevador B - Andar Atual: 0");

        VBox root = new VBox(10, nomePassageiroField, andarOrigemField, andarDestinoField,
                adicionarPassageiroButton , andarElevador1Label, andarElevador2Label, listaPassageiros);
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-padding: 20px; -fx-background-color: #f0f0f0;");

        Scene scene = new Scene(root, 400, 500);
        stage.setTitle("Simulador de Elevadores");
        stage.setScene(scene);
        stage.show();

        iniciarMovimentacaoElevadores();
    }

    private void iniciarMovimentacaoElevadores() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            elevador1.moverElevador();
            elevador2.moverElevador();
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
        int distanciaA = Math.abs(elevador1.getAndarAtual() - passageiro.getAndarOrigem());
        int distanciaB = Math.abs(elevador2.getAndarAtual() - passageiro.getAndarOrigem());

        if (distanciaA <= distanciaB) elevador1.solicitarElevador(passageiro);
        else elevador2.solicitarElevador(passageiro);
    }

    private void atualizarListaPassageiros() {
        listaPassageiros.getChildren().clear();
        for (Elevador elevador : new Elevador[]{elevador1, elevador2}) {
            for (Passageiro passageiro : elevador.getPassageirosNoElevador()) {
                HBox passageiroItem = new HBox(10);
                passageiroItem.setAlignment(Pos.CENTER_LEFT);
                Label nomeLabel = new Label("Nome: " + passageiro.getNome());
                Label destinoLabel = new Label("Destino: " + passageiro.getAndarDestino());
                Button descerButton = new Button("Descer");
                descerButton.setOnAction(e -> {
                    if (passageiro.getAndarDestino() == elevador.getAndarAtual()) {
                        elevador.removerPassageiro(passageiro);
                        atualizarListaPassageiros();
                    } else {
                        mostrarErro("O elevador não está no andar do destino.");
                    }
                });
                passageiroItem.getChildren().addAll(nomeLabel, destinoLabel, descerButton);
                listaPassageiros.getChildren().add(passageiroItem);
            }
        }
    }

    private void atualizarInterface() {
        Platform.runLater(() -> {
            andarElevador1Label.setText("Elevador A - Andar Atual: " + elevador1.getAndarAtual());
            andarElevador2Label.setText("Elevador B - Andar Atual: " + elevador2.getAndarAtual());
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


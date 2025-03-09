package br.ufrpe.simulador.simulador.elevadores.ui;

import br.ufrpe.simulador.simulador.elevadores.SpringConfig;
import br.ufrpe.simulador.simulador.elevadores.modelo.Elevador;
import br.ufrpe.simulador.simulador.elevadores.modelo.Passageiro;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class ElevadorApp extends Application {
    private Label statusElevador;
    private Elevador elevador;
    private ObservableList<String> passageirosList;
    private ListView<String> listViewPassageiros;

    @Override
    public void start(Stage stage) {
        elevador = new Elevador("Elevador A");
        elevador.start(); // Inicia a thread do elevador

        VBox root = new VBox();
        statusElevador = new Label("Elevador no andar 0");

        // Lista de passageiros
        passageirosList = FXCollections.observableArrayList();
        listViewPassageiros = new ListView<>(passageirosList);

        TextField nomePassageiro = new TextField();
        nomePassageiro.setPromptText("Nome do passageiro");

        TextField andarDestino = new TextField();
        andarDestino.setPromptText("Andar desejado");

        Button chamarElevador = new Button("Chamar Elevador");
        chamarElevador.setOnAction(e -> {
            String nome = nomePassageiro.getText();
            int destino = Integer.parseInt(andarDestino.getText());

            // Cria um novo passageiro e solicita o elevador
            Passageiro passageiro = new Passageiro(nome, 0, destino);
            passageiro.solicitarElevador(elevador);

            // Atualiza a lista de passageiros
            Platform.runLater(() -> {
                passageirosList.add(nome + " para o andar " + destino);
            });
        });

        Button atualizarStatus = new Button("Atualizar Status");
        atualizarStatus.setOnAction(e -> Platform.runLater(() -> {
            statusElevador.setText("Elevador no andar " + elevador.getAndarAtual());
        }));

        root.getChildren().addAll(statusElevador, nomePassageiro, andarDestino, chamarElevador, listViewPassageiros, atualizarStatus);
        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Simulador de Elevador");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
package br.ufrpe.simulador.simulador.elevadores.ui;

import br.ufrpe.simulador.simulador.elevadores.modelo.Elevador;
import br.ufrpe.simulador.simulador.elevadores.modelo.Passageiro;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ElevadorApp extends Application {

    private Elevador elevador;
    private VBox listaPassageiros;
    private TextField nomePassageiroField;
    private TextField andarDestinoField;
    private Label andarAtualLabel;

    @Override
    public void start(Stage stage) {
        // Inicializa o elevador
        elevador = new Elevador("Elevador 1");

        // Cria a caixa de entrada para nome e destino
        nomePassageiroField = new TextField();
        nomePassageiroField.setPromptText("Nome do Passageiro");

        andarDestinoField = new TextField();
        andarDestinoField.setPromptText("Andar de Destino");

        // Cria um botão para adicionar o passageiro
        Button adicionarPassageiroButton = new Button("Adicionar Passageiro");
        adicionarPassageiroButton.setOnAction(e -> adicionarPassageiro());

        // Cria uma lista para mostrar os passageiros
        listaPassageiros = new VBox(10);
        listaPassageiros.setAlignment(Pos.TOP_LEFT);

        // Exibe o andar atual do elevador
        andarAtualLabel = new Label("Andar Atual: 0");

        // Cria a VBox para os campos de entrada e a lista de passageiros
        VBox root = new VBox(10, nomePassageiroField, andarDestinoField, adicionarPassageiroButton, listaPassageiros, andarAtualLabel);
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-padding: 20px; -fx-background-color: #f0f0f0;");

        // Cria a cena e o palco
        Scene scene = new Scene(root, 400, 400);
        stage.setTitle("Simulador de Elevadores");
        stage.setScene(scene);
        stage.show();

        // Inicia o processo de movimentação do elevador em uma thread separada
        new Thread(() -> moverElevador()).start();
    }

    // Método para adicionar o passageiro e atualizar a lista
    private void adicionarPassageiro() {
        String nome = nomePassageiroField.getText();
        String andarDestinoTexto = andarDestinoField.getText();
        if (!nome.isEmpty() && !andarDestinoTexto.isEmpty()) {
            try {
                int andarDestino = Integer.parseInt(andarDestinoTexto);
                Passageiro passageiro = new Passageiro(nome, 0, andarDestino);
                elevador.solicitarElevador(passageiro);
                atualizarListaPassageiros();
                nomePassageiroField.clear();
                andarDestinoField.clear();
            } catch (NumberFormatException e) {
                mostrarErro("O andar de destino deve ser um número válido.");
            }
        } else {
            mostrarErro("Por favor, preencha o nome e o andar de destino.");
        }
    }

    // Método para atualizar a lista de passageiros na interface
    private void atualizarListaPassageiros() {
        listaPassageiros.getChildren().clear(); // Limpa a lista antiga

        for (Passageiro passageiro : elevador.getPassageirosNoElevador()) {
            HBox passageiroItem = new HBox(10);
            passageiroItem.setAlignment(Pos.CENTER_LEFT);
            Label nomeLabel = new Label("Nome: " + passageiro.getNome());
            Label destinoLabel = new Label("Destino: " + passageiro.getAndarDestino());
            Button descerButton = new Button("Descer");

            // Adiciona o comportamento para descer o passageiro
            descerButton.setOnAction(e -> {
                if (passageiro.getAndarDestino() == elevador.getAndarAtual() || elevador.getAndarAtual() == passageiro.getAndarAtual()) {
                    elevador.removerPassageiro(passageiro);
                    atualizarListaPassageiros();
                    System.out.println(passageiro.getNome() + " desceu no andar " + elevador.getAndarAtual());
                } else {
                    mostrarErro("O elevador não está no andar do destino.");
                }
            });

            passageiroItem.getChildren().addAll(nomeLabel, destinoLabel, descerButton);
            listaPassageiros.getChildren().add(passageiroItem);
        }
    }

    // Método para atualizar a posição do elevador e a exibição do andar atual
    private void moverElevador() {
        try {
            while (true) {
                // Verifica se há passageiros e move o elevador
                if (!elevador.getPassageirosNoElevador().isEmpty()) {
                    // O elevador sobe ou desce aleatoriamente para simular o movimento
                    elevador.moverElevador();
                    updateAndarAtual();  // Atualiza a interface com o andar atual
                    Thread.sleep(1000); // Tempo de movimento
                }

                // Verifica se algum passageiro chegou no seu destino e permite descer
                for (Passageiro passageiro : elevador.getPassageirosNoElevador()) {
                    if (passageiro.getAndarDestino() == elevador.getAndarAtual()) {
                        elevador.removerPassageiro(passageiro);
                        System.out.println(passageiro.getNome() + " desceu no andar " + elevador.getAndarAtual());
                        atualizarListaPassageiros();
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Método para atualizar a label do andar atual do elevador
    private void updateAndarAtual() {
        // Usando JavaFX para garantir que a atualização ocorra na thread de UI
        javafx.application.Platform.runLater(() -> andarAtualLabel.setText("Andar Atual: " + elevador.getAndarAtual()));
    }

    // Método para exibir erros
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

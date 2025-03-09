package br.ufrpe.simulador.simulador.elevadores.ui;

import br.ufrpe.simulador.simulador.elevadores.SpringConfig;
import br.ufrpe.simulador.simulador.elevadores.modelo.Elevador;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.Semaphore;

public class ElevadorApp extends Application {

    private AnnotationConfigApplicationContext springContext;

    @Override
    public void init() {
        // Inicializa o contexto do Spring manualmente
        springContext = new AnnotationConfigApplicationContext(SpringConfig.class);
    }

    @Override
    public void start(Stage stage) {
        VBox root = new VBox();
        root.getChildren().add(new Label("Simulador de Elevadores"));

        Scene scene = new Scene(root, 300, 200);
        stage.setScene(scene);
        stage.setTitle("Elevadores");
        stage.show();
    }

    @Override
    public void stop() {
        // Fecha o contexto do Spring ao sair
        springContext.close();
    }
}

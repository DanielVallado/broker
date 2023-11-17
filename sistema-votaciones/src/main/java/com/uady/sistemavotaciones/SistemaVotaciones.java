package com.uady.sistemavotaciones;

import com.uady.sistemavotaciones.controller.VotacionesController;
import com.uady.sistemavotaciones.util.Util;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SistemaVotaciones extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        String URL_VISTA = "/com/uady/sistemavotaciones/view/VotacionesView.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(Util.obtenerPath(URL_VISTA));
        Scene scene = new Scene(fxmlLoader.load());
        VotacionesController controller = fxmlLoader.getController();
        stage.setOnCloseRequest(event -> controller.cerrarVentanasGraficas());
        stage.setTitle("Sistema de Votaciones");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
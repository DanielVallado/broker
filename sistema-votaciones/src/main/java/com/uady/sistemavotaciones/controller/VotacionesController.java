package com.uady.sistemavotaciones.controller;

import com.uady.sistemavotaciones.client.Client;
import com.uady.sistemavotaciones.model.Producto;
import com.uady.sistemavotaciones.util.Util;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;


public class VotacionesController implements Initializable {

    @FXML
    private GridPane contenedorProductos;
    @FXML
    private final Map<String, Label> productoContadorMap;
    @FXML
    private final Map<String, Stage> graficaStages;

    private final List<Producto> productosList;
    private final List<GraficaController> controladoresGraficas;

    private final Client client;

    public VotacionesController() {
        this.graficaStages = new HashMap<>();
        this.productoContadorMap = new HashMap<>();
        this.controladoresGraficas = new ArrayList<>();
        this.client = Client.getInstance();
        this.productosList = client.obtenerProductos();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client.log("Se inicializo el controlador de votaciones");
        colocarVotos();
        int row = 0, col = 0;
        for (Producto producto : productosList) {
            VBox productBox = crearComponenteProducto(producto);
            contenedorProductos.add(productBox, col, row);

            col++;
            if (col == 4) {
                col = 0;
                row++;
            }
        }
    }

    private VBox crearComponenteProducto(Producto producto) {
        VBox productBox = new VBox();
        productBox.setSpacing(10);
        productBox.setAlignment(Pos.CENTER);
        productBox.setPadding(new Insets(10, 5, 0, 5));

        ImageView productImage = new ImageView(new Image(Objects.requireNonNull(Util.obtenerImg("/com/uady/sistemavotaciones/images/%s.jpg".formatted(producto.getNombre())))));
        productImage.setFitWidth(170);
        productImage.setPreserveRatio(true);

        Button voteButton = new Button("Votar " + producto.getNombre());
        voteButton.setOnAction(event -> votar(producto.getNombre()));

        Label contadorLabel = new Label(producto.getCantidad().toString());
        contadorLabel.setStyle("-fx-font-size: 18px;");
        productoContadorMap.put(producto.getNombre(), contadorLabel);

        productBox.getChildren().addAll(productImage, voteButton, contadorLabel);
        return productBox;
    }

    @FXML
    public void verEstadisticas() {
        client.log("Ser vieron las estadisticas");
        String graficaBarrasPath = "/com/uady/sistemavotaciones/view/GraficaDeBarrasView.fxml";
        String tituloGraficaBarras = "Sistema de Votaciones | Grafica de Barras";
        mostrarGrafica(Util.obtenerPath(graficaBarrasPath), tituloGraficaBarras);

        String graficaPastelPath = "/com/uady/sistemavotaciones/view/GraficaDePastelView.fxml";
        String tituloGraficaPastel = "Sistema de Votaciones | Grafica de Pastel";
        mostrarGrafica(Util.obtenerPath(graficaPastelPath), tituloGraficaPastel);
    }

    private void mostrarGrafica(URL filePath, String titulo) {
        client.log("Se mostro grafica de " + titulo);
        if (!graficaStages.containsKey(titulo)) {
            FXMLLoader fxmlLoader = new FXMLLoader(filePath);
            crearScene(fxmlLoader, titulo);
            this.controladoresGraficas.add(fxmlLoader.getController());
            actualizarGraficas();
        }
    }

    private void crearScene(FXMLLoader fxmlLoader, String titulo) {
        try {
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(scene);
            stage.setOnCloseRequest(event -> graficaStages.remove(titulo));
            graficaStages.put(titulo, stage);
            stage.show();
        } catch (IOException e) {
            client.log("Error al crear la escena");
            throw new RuntimeException(e);
        }
    }

    private void votar(String productName) {
        client.votar(productName);
        client.log("Voto por " + productName);
        colocarVotos();
    }

    private void colocarVotos() {
        for (Producto producto : productosList) {
            producto.setCantidad(client.contarVotos(producto.getNombre()));
        }

        actualizarEtiquetas();
        actualizarGraficas();
    }

    private void actualizarEtiquetas() {
        for (Producto producto : productosList) {
            Label contadorLabel = productoContadorMap.get(producto.getNombre());

            if (contadorLabel != null) {
                contadorLabel.setText(producto.getCantidad().toString());
            }
        }
    }

    private void actualizarGraficas() {
        client.log("Se actualizaron las graficas");
        controladoresGraficas.stream()
                .filter(Objects::nonNull)
                .forEach(controller -> controller.actualizarDatos(productosList));
    }

    public void cerrarVentanasGraficas() {
        for (Stage stage : graficaStages.values()) {
            stage.close();
        }
    }

}
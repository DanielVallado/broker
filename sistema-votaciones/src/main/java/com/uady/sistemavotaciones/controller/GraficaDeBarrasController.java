package com.uady.sistemavotaciones.controller;

import com.uady.sistemavotaciones.enums.Color;
import com.uady.sistemavotaciones.model.Dato;
import com.uady.sistemavotaciones.model.Producto;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Text;
import lombok.extern.log4j.Log4j2;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Log4j2
public class GraficaDeBarrasController implements Initializable, GraficaController {

    @FXML
    private BarChart<String, Integer> barChart;
    @FXML
    private Text graficaDeBarrasTexto;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("Creación gráfica de barras");
        graficaDeBarrasTexto.setText("Votaciones");
    }

    public void actualizarDatos(List<Producto> datos) {
        barChart.getData().clear();
        Color[] colores = Color.values();

        int indiceColor = 0;
        for (Dato dato : datos) {
            XYChart.Series<String, Integer> series = new XYChart.Series<>();
            XYChart.Data<String, Integer> data = new XYChart.Data<>(dato.getNombreDato(), dato.getCantidadDato());

            series.setName(dato.getNombreDato());
            series.getData().add(data);
            barChart.getData().add(series);

            Node bar = data.getNode();
            if (bar != null) {
                Color color = colores[indiceColor % colores.length];
                bar.setStyle("-fx-bar-fill: " + color.hexCode);
            }

            indiceColor++;
        }

        log.info("Actualización de datos realizada");
    }

}
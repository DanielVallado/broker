package com.uady.sistemavotaciones.controller;

import com.uady.sistemavotaciones.enums.Color;
import com.uady.sistemavotaciones.model.Dato;
import com.uady.sistemavotaciones.model.Producto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import lombok.extern.log4j.Log4j2;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Log4j2
public class GraficaDePastelController implements Initializable, GraficaController {

    @FXML
    private PieChart graficaDePastel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("Creación gráfica de pastel");
        graficaDePastel.setTitle("Votaciones");
    }

    public void actualizarDatos(List<Producto> datos) {
        ObservableList<PieChart.Data> graficaDePastelData = FXCollections.observableArrayList();

        for (Dato dato : datos) {
            PieChart.Data data = new PieChart.Data(dato.getNombreDato(), dato.getCantidadDato());
            graficaDePastelData.add(data);
        }

        graficaDePastel.setData(graficaDePastelData);

        asignarColores(graficaDePastelData);
        log.info("Actualización de datos realizada");
    }

    private static void asignarColores(ObservableList<PieChart.Data> graficaDePastelData) {
        Color[] colores = Color.values();
        for (int i = 0; i < graficaDePastelData.size(); i++) {
            PieChart.Data data = graficaDePastelData.get(i);
            Node node = data.getNode();
            if (node != null) {
                Color color = colores[i % colores.length];
                node.setStyle("-fx-pie-color: " + color.hexCode);
            }
        }
    }

}
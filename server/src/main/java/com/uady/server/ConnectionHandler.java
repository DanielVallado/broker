package com.uady.server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.uady.log.ProductoLogger;
import com.uady.model.Producto;
import com.uady.service.ProductoService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ConnectionHandler implements Runnable {

    private final Socket socket;
    private final ProductoService productoService;

    public ConnectionHandler(Socket socket) {
        this.socket = socket;
        this.productoService = new ProductoService();
    }

    @Override
    public void run() {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            PrintWriter writer = new PrintWriter(this.socket.getOutputStream(), true)) {
            JsonObject request = JsonParser.parseString(reader.readLine()).getAsJsonObject();
            JsonObject response = handleRequest(request);
            writer.println(response);
        } catch (IOException e) {
            System.err.println("Error al leer el mensaje.");
        }
    }

    private JsonObject handleRequest(JsonObject request) {
        JsonObject response = new JsonObject();

        String serviceName = request.get("servicio").getAsString();
        switch (serviceName) {
            case "contar" -> response = count();
            case "votar" -> response = vote(request);
            case "registrar" -> response = register(request);
            case "listar" -> response = list();
            default -> System.out.println("Servicio no encontrado.");
        }

        return response;
    }

    private JsonObject count() {
        JsonObject response = new JsonObject();

        List<Producto> productosList = productoService.obtenerProductos();

        response.addProperty("servicio", "contar");
        response.addProperty("respuestas", productosList.size());

        for (int i = 0; i < productosList.size(); i++) {
            response.addProperty("respuesta" + (i + 1), productosList.get(i).getNombre());
            response.addProperty("valor" + (i + 1), this.productoService.contarVotos(productosList.get(i).getNombre()));
        }

        return response;
    }

    private JsonObject vote(JsonObject request) {
        JsonObject response = new JsonObject();

        String productName = request.get("variable1").getAsString();
        this.productoService.votar(productName);

        response.addProperty("servicio", "votar");
        response.addProperty("respuestas", 1);
        response.addProperty("respuesta1", productName);
        response.addProperty("valor1", this.productoService.contarVotos(productName));

        return response;
    }

    private JsonObject register(JsonObject request) {
        String message = request.get("valor1").getAsString();
        String date = request.get("valor2").getAsString();

        ProductoLogger.log(message + " " + date);

        String response = """
                {
                 "servicio" : "registrar",
                 "respuestas" : 1,
                 "respuesta1" : "eventos",
                 "valor1" : %d
                }
                """.formatted(ProductoLogger.getLogs().size());

        return JsonParser.parseString(response).getAsJsonObject();
    }

    private JsonObject list() {
        JsonObject response = new JsonObject();

        response.addProperty("servicio", "listar");
        response.addProperty("respuestas", ProductoLogger.getLogs().size());

        List<String> logs = ProductoLogger.getLogs();
        for (int i = 0; i < logs.size(); i++) {
            response.addProperty("respuesta" + (i + 1), "evento");
            response.addProperty("valor" + (i + 1), logs.get(i));
        }

        return response;
    }

}

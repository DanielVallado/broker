package com.uady.sistemavotaciones.client;

import com.google.gson.JsonParser;
import com.uady.sistemavotaciones.model.Producto;

import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Client {

    private static Client client;
    private static String brokerIp;
    private static int brokerPort;

    public static void setInstance(String brokerIp) {
        if (client == null) {
            String[] parts = brokerIp.split(":");
            String ip = parts[0];
            int port = Integer.parseInt(parts[1]);
            Client.client = new Client(port, ip);
        }
    }

    public static Client getInstance() {
        if (client == null)
            return new Client(90, "127.0.0.1");
        return client;
    }

    private Client(int brokerPort, String brokerIp) {
        Client.brokerPort = brokerPort;
        Client.brokerIp = brokerIp;
    }

    public String sendJson(JsonObject json) {
        try (Socket socket = new Socket(brokerIp, brokerPort);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println(json);
            return reader.readLine();

        } catch (IOException e) {
            System.err.println("Error al ejecutar servicio: " + e);
        }

        return null;
    }

    public List<Producto> obtenerProductos() {
        String message = """
            {
                "servicio": "ejecutar",
                "variables": 2,
                "variable1": "servicio",
                "valor1": "contar"
            }
            """;
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        String responseString = sendJson(jsonObject);
        JsonObject response = JsonParser.parseString(responseString).getAsJsonObject();

        List<Producto> productList = new ArrayList<>();
        int responseNumber = response.get("respuestas").getAsInt() - 1; // Para no contar el primer valor que es el servicio
        for (int i = 0; i < responseNumber; i++) {
            String productName = response.get("respuesta" + (i + 2)).getAsString();
            int productVotes = response.get("valor" + (i + 2)).getAsInt();

            Producto product = new Producto(productName,
                    productVotes,
                    "/com/uady/sistemavotaciones/images/%s.jpg".formatted(productName));
            productList.add(product);
        }

        return productList;
    }

    public void votar(String vote) {
        String message = """
            {
                "servicio": "ejecutar",
                "variables": 2,
                "variable1": "servicio",
                "valor1": "votar",
                "variable2": "%s",
                "valor2": "1"
            }
            """.formatted(vote);
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        sendJson(jsonObject);
    }

    public void log(String event) {
        String message = """
            {
                "servicio": "ejecutar",
                "variables": 3,
                "variable1": "servicio",
                "valor1": "registrar",
                "variable2": "evento",
                "valor2": "%s",
                "variable3": "fecha",
                "valor3": "%s"
            }
            """.formatted(event, getDate());
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        sendJson(jsonObject);
    }

    public void getLogs() {
        String message = """
            {
                "servicio": "ejecutar",
                "variables": 1,
                "variable1": "servicio",
                "valor1": "listar"
            }
            """;
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        sendJson(jsonObject);
    }

    public String listarServidores() {
        String message = """
            {
                "servicio": "listar",
                "variables": 0
            }
            """;
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        return sendJson(jsonObject);
    }

    public String listarServidores(String serviceName) {
        String message = """
            {
                "servicio": "listar",
                "variables": 1,
                "variable1": "palabra",
                "valor1": "%s"
            }
            """.formatted(serviceName);
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        return sendJson(jsonObject);
    }

    public int contarVotos(String nameProducto) {
        List<Producto> productsList = obtenerProductos();
        for (Producto product : productsList) {
            if (product.getNombre().equalsIgnoreCase(nameProducto))
                return product.getCantidad();
        }
        return 0;
    }

    private String getDate() {
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return currentDate.format(formatDate);
    }

}

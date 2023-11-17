package com.uady.broker;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.uady.registry.Server;
import com.uady.registry.ServerRegistry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ConnectionHandler implements Runnable {

    private final Socket socket;

    public ConnectionHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            PrintWriter writer = new PrintWriter(this.socket.getOutputStream(), true)) {
            JsonObject request = JsonParser.parseString(reader.readLine()).getAsJsonObject();
            JsonObject response = handleRequest(request);
            writer.println(response);
        } catch (IOException e) {
            System.out.println("Error al leer el mensaje.");
        }
    }

    private JsonObject handleRequest(JsonObject request) {
        JsonObject response = new JsonObject();
        String serviceName = request.get("servicio").getAsString();

        switch (serviceName) {
            case "registrar" -> response = registerService(request);

            case "listar" -> {
                if (request.get("variables").getAsInt() == 0)
                    response = listServices();
                else
                    response = listServices(request);
            }

            case "ejecutar" -> response = executeService(request);

            default -> System.err.println("Servicio no encontrado.");
        }

        return response;
    }

    private JsonObject registerService(JsonObject message) {
        String ip = message.get("valor1").getAsString();
        int port = message.get("valor2").getAsInt();
        String serviceName = message.get("valor3").getAsString();
        int numParams = message.get("valor4").getAsInt();

        int serverId = ServerRegistry.addService(ip, port, serviceName, numParams);

        String responseString = """
                {
                 "servicio" : "%s",
                 "respuestas" : 1,
                 "respuesta1" : "identificador",
                 "valor1" : %d
                }
                """.formatted(serviceName, serverId);

        return JsonParser.parseString(responseString).getAsJsonObject();
    }

    private JsonObject listServices() {
        List<Server> servers = ServerRegistry.getServices();
        return createResponseList(servers);
    }

    private JsonObject listServices(JsonObject message) {
        List<Server> servers = ServerRegistry.getServices(message.get("valor1").getAsString());
        return createResponseList(servers);
    }

    private JsonObject executeService(JsonObject message) {
        String serviceName = message.get("valor1").getAsString();
        List<Server> servers = ServerRegistry.getServices(serviceName);

        if (servers.isEmpty()) {
            System.out.println("No se encontró un servidor.");
            return null;
        }

        // Service service = services.get((int) (Math.random() * services.size())); // Obtener un servidor aleatorio
        Server server = servers.get(0);
        String ip = server.getIp();
        int port = server.getPort();

        JsonObject messageJson = createMessageJson(server, message);
        try (Socket socket = new Socket(ip, port);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            writer.println(messageJson);
            String response = reader.readLine();
            return createResponseExecute(JsonParser.parseString(response).getAsJsonObject());
        } catch (IOException e) {
            System.err.println("Error al ejecutar el servicio.");
        }

        return null;
    }

    private JsonObject createResponseExecute(JsonObject response) {
        JsonObject json = new JsonObject();
        json.addProperty("servicio", "ejecutar");
        json.addProperty("respuestas", response.get("respuestas").getAsInt() + 1);

        // Colocar el servicio y las variables de la respuesta del servidor en la primera posición
        json.addProperty("respuesta1", "servicio");
        json.addProperty("valor1", response.get("servicio").getAsString());
        for (int i = 1; i <= response.get("respuestas").getAsInt(); i++) {
            String respuesta = response.get("respuesta" + i).getAsString();
            String valor = response.get("valor" + i).getAsString();

            json.addProperty("respuesta" + (i + 1), respuesta);
            json.addProperty("valor" + (i + 1), valor);
        }

        return json;
    }

    private JsonObject createResponseList(List<Server> servers) {
        JsonObject json = new JsonObject();
        json.addProperty("servicio", "listar");
        json.addProperty("respuestas", servers.size());

        for (int i = 0; i < servers.size(); i++) {
            Server server = servers.get(i);

            json.addProperty("respuesta" + (i + 1), server.getServiceName() );
            json.addProperty("valor" + (i + 1), server.getIp() + ":" + server.getPort());
        }

        return json;
    }

    private JsonObject createMessageJson(Server server, JsonObject message) {
        JsonObject messageJson = new JsonObject();
        String serviceName = message.get("valor1").getAsString();

        messageJson.addProperty("servicio", serviceName);
        messageJson.addProperty("variables", server.getNumberParameters());
        for (int i = 0; i < server.getNumberParameters(); i++) {
            messageJson.addProperty("variable" + (i + 1), message.get("variable" + (i + 2)).getAsString());
            messageJson.addProperty("valor" + (i + 1), message.get("valor" + (i + 2)).getAsString());
        }

        return messageJson;
    }

}

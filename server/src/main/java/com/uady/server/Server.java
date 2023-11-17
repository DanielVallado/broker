package com.uady.server;

import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final int port;
    private ServerSocket serverSocket;
    private ExecutorService executorService;

    public Server(int port, String brokerIp, int maxThreads) {
        this.port = port;
        try {
            registerServices(brokerIp);
            serverSocket = new ServerSocket(port);
            executorService = Executors.newFixedThreadPool(maxThreads);
        } catch (IOException e) {
            System.out.println("Error al crear el socket del servidor.");
        }
    }

    public void start() {
        System.out.println("Server esperando conexiones en el puerto " + serverSocket.getLocalPort());

        Thread commandListener = new Thread(this::listenForCommands);
        commandListener.start();

        while (!serverSocket.isClosed()) {
            try {
                Socket clientSocket = serverSocket.accept();
                executorService.submit(new ConnectionHandler(clientSocket));
            } catch (IOException e) {
                if (!serverSocket.isClosed()) {
                    System.out.println("Error al aceptar la conexión.");
                    break;
                }
            }
        }
    }

    public void stop() {
        try {
            serverSocket.close();
            executorService.shutdown();
            System.out.println("Servidor detenido.");
        } catch (IOException e) {
            System.out.println("Error al cerrar el socket del servidor.");
        }
    }

    private void listenForCommands() {
        System.out.print("Escribe \"stop\" para detener el servidor: ");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine().trim().toLowerCase();
            if (command.equals("stop")) {
                stop();
                break;
            }
        }
    }

    public void registerServices(String brokeIp) {
        String[] parts = brokeIp.split(":");
        String ip = parts[0];
        int port = Integer.parseInt(parts[1]);

        registerService(port, ip, "contar", 0);
        registerService(port, ip, "votar", 1);
        registerService(port, ip, "registrar", 2);
        registerService(port, ip, "listar", 0);
    }

    private void registerService(int brokerPort, String brokerIp, String servicio, int parametros) {

        try (Socket socket = new Socket(brokerIp, brokerPort);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            JsonObject json = createJson(servicio, parametros);
            out.println(json);

            String response = reader.readLine();
        } catch (IOException e) {
            System.err.println("Error al registrar los servicios: " + e);
            System.exit(1);
        }

    }

    private JsonObject createJson(String servicio, int cantidadParametros) {
        JsonObject json = new JsonObject();
        json.addProperty("servicio", "registrar");
        json.addProperty("variables", 4);
        json.addProperty("variable1", "servidor");
        json.addProperty("valor1", "127.0.0.1");
        json.addProperty("variable2", "puerto");
        json.addProperty("valor2", this.port);
        json.addProperty("variable3", "servicio");
        json.addProperty("valor3", servicio);
        json.addProperty("variable4", "parametros");
        json.addProperty("valor4", cantidadParametros);
        return json;
    }

}

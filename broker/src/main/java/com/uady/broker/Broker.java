package com.uady.broker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Broker {

    private ServerSocket serverSocket;
    private ExecutorService executorService;

    public Broker(int port, int maxThreads) {
        try {
            serverSocket = new ServerSocket(port);
            executorService = Executors.newFixedThreadPool(maxThreads);
        } catch (IOException e) {
            System.out.println("Error al crear el socket del servidor.");
        }
    }

    public void start() {
        System.out.println("Broker esperando conexiones en el puerto " + serverSocket.getLocalPort());

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
            System.out.println("Broker detenido.");
        } catch (IOException e) {
            System.out.println("Error al cerrar el socket del servidor.");
        }
    }

    private void listenForCommands() {
        System.out.println("¡Escribe \"stop\" para detener el broker!");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine().trim().toLowerCase();
            if (command.equals("stop")) {
                stop();
                break;
            }
        }
    }

}

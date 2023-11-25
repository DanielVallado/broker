package com.uady;

import com.uady.server.Server;

import java.util.Scanner;

public class ServerApplication {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Escribe el puerto para el server: ");
        int port = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Escribe la ip del broker: ");
        String ipBroker = scanner.nextLine().trim();

        if (port == 0) port = 91;
        if (ipBroker.isEmpty()) ipBroker = "127.0.0.1:90";

        final int MAX_THREADS = 10;
        Server server = new Server(port, ipBroker, MAX_THREADS);
        server.start();
    }

}
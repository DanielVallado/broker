package com.uady;

import com.uady.broker.Broker;
import com.uady.registry.ServerRegistry;

import java.util.Scanner;

public class BrokerApplication {
    public static void main(String[] args) {
        final String FILE_PATH = "servers/registryServer.txt";
        ServerRegistry.getInstance(FILE_PATH);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingresa el puerto del broker: ");
        int port = scanner.nextInt();

        final int MAX_THREADS = 10;
        com.uady.broker.Broker broker = new Broker(port, MAX_THREADS);
        broker.start();
    }
}
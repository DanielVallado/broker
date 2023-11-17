package com.uady;

import com.uady.broker.Broker;
import com.uady.registry.ServerRegistry;

public class BrokerApplication {
    public static void main(String[] args) {
        final String FILE_PATH = "C:\\Users\\danie\\OneDrive - Universidad Autonoma de Yucatan\\LIS\\LIS - Quinto Semestre\\Aquitectura de Software\\ADA 9 - Broker\\broker\\src\\main\\java\\com\\uady\\registry\\registryServer.txt";
        ServerRegistry.getInstance(FILE_PATH);

        int port = 0;

        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        if (port == 0) {
            port = 90;
        }

        final int MAX_THREADS = 10;
        com.uady.broker.Broker broker = new Broker(port, MAX_THREADS);
        broker.start();
    }
}
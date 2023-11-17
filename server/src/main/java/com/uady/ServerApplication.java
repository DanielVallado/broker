package com.uady;

import com.uady.server.Server;

public class ServerApplication {

    public static void main(String[] args) {
        int port = 0;

        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        if (port == 0) {
            port = 91;
        }

        String ipBroker = "127.0.0.1:90";
        final int MAX_THREADS = 10;
        Server server = new Server(port, ipBroker, MAX_THREADS);
        server.start();
    }

}
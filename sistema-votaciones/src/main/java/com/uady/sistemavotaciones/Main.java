package com.uady.sistemavotaciones;

import com.uady.sistemavotaciones.client.Client;



public class Main {

    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 90);
        client.log("Se registró un voto para Windows");
    }

}

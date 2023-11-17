package com.uady.registry;

import lombok.Getter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ServerRegistry {

    private static ServerRegistry instance;
    private static String filePath;
    @Getter
    private static int serviceCount;

    public static void getInstance(String filePath) {
        if (instance == null) {
            instance = new ServerRegistry(filePath);
        }
    }

    private ServerRegistry(String filePath) {
        ServerRegistry.filePath = filePath;
        initializeFile();
    }

    private void initializeFile() {
        try  {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false));
            writer.write("");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error al inicializar el archivo de registro: " + e);
        }
    }

    public static int addService(String ip, int port, String serviceName, int numParams) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            ServerRegistry.serviceCount++;
            String service = String.format("%d %s %d %s %d\n", serviceCount, ip, port, serviceName, numParams);
            writer.write(service);
            return serviceCount;
        } catch (IOException e) {
            System.out.println("Error al registrar el servicio: " + e);
            return 0;
        }
    }

    public static List<Server> getServices() {
        return readServices();
    }

    public static List<Server> getServices(String serviceName) {
        List<Server> filteredServers = new ArrayList<>();
        List<Server> servicesList = readServices();

        for (Server server : servicesList) {
            if (server.getServiceName().equalsIgnoreCase(serviceName)) {
                filteredServers.add(server);
            }
        }

        return filteredServers;
    }

    private static List<Server> readServices() {
        List<Server> servicesList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Server server = processEntry(line);
                if (server != null)
                    servicesList.add(server);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de registro.");
        }
        return servicesList;
    }

    private static Server processEntry(String entry) {
        String[] parts = entry.split("\\s+");
        if (parts.length == 5) {
            return getServiceObject(parts);
        } else {
            System.out.println("Formato de entrada incorrecto: " + entry);
            return null;
        }
    }

    private static Server getServiceObject(String[] parts) {
        int id = Integer.parseInt(parts[0]);
        String ip = parts[1];
        int port = Integer.parseInt(parts[2]);
        String serviceName = parts[3];
        int numberParameters = Integer.parseInt(parts[4]);
        return new Server(id, ip, port, serviceName, numberParameters);
    }

}

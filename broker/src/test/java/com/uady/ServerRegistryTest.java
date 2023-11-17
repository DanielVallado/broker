package com.uady;

import com.uady.registry.Server;
import com.uady.registry.ServerRegistry;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ServerRegistryTest {

    private static final String TEST_FILE_PATH = "test_registry.txt";

    @BeforeAll
    public static void setup() {
        ServerRegistry.getInstance(TEST_FILE_PATH);
    }

    @Test
    void addServiceShouldIncrementServiceCount() {
        int initialServiceCount = ServerRegistry.getServiceCount();

        int id = ServerRegistry.addService("127.0.0.1", 8080, "Listar", 0);

        assertEquals(initialServiceCount + 1, id);
    }

    @Test
    void getServicesShouldReturnNonEmptyList() {
        ServerRegistry.addService("127.0.0.1", 8080, "Votar", 1);

        List<Server> servers = ServerRegistry.getServices();

        assertFalse(servers.isEmpty());
    }

    @Test
    void getServicesShouldReturnCorrectServiceByName() {
        ServerRegistry.addService("127.0.0.1", 8080, "contar", 0);

        List<Server> servers = ServerRegistry.getServices("contar");

        assertFalse(servers.isEmpty());
        assertEquals("contar", servers.get(0).getServiceName());
    }

    @Test
    void getServicesShouldReturnEmptyListForNonExistingService() {
        ServerRegistry.addService("127.0.0.1", 8080, "registrar", 2);

        List<Server> servers = ServerRegistry.getServices("nonExistingService");

        assertTrue(servers.isEmpty());
    }

}

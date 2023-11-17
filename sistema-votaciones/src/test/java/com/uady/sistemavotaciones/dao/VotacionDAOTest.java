package com.uady.sistemavotaciones.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VotacionDAOTest {

    private static final String TEST_FILE = "test-votaciones.txt";
    private VotacionDAO votacionDAO;

    @BeforeEach
    void setUp() throws IOException {
        // Crea un archivo de prueba antes de cada prueba
        Files.write(new File(TEST_FILE).toPath(), "".getBytes());
        votacionDAO = new VotacionDAO(TEST_FILE);
    }

    @Test
    @DisplayName("Registra y obtiene los nombres registrados")
    void registrarYObtenerTodos() {
        // Prueba registrar un mensaje y luego obtenerlo
        votacionDAO.registrar("Votación 1");
        votacionDAO.registrar("Votación 2");
        List<String> votaciones = votacionDAO.obtenerTodos();

        assertEquals(2, votaciones.size());
        assertEquals("Votación 1", votaciones.get(0));
        assertEquals("Votación 2", votaciones.get(1));
    }

    @Test
    @DisplayName("Obtiene todos los registros")
    void obtenerTodosSinRegistros() {
        // Prueba obtener todos cuando no hay registros
        List<String> votaciones = votacionDAO.obtenerTodos();
        assertEquals(0, votaciones.size());
    }

}
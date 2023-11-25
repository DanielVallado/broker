package com.uady.service;

import com.uady.dao.VotacionDAO;
import com.uady.model.Producto;
import com.uady.util.MyFileHandler;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class ProductoService {

    public List<Producto> obtenerProductos() {
        List<String> nombresProductos = MyFileHandler.readFile("data/productos.txt");
        if (nombresProductos == null){
            log.error("No hay productos");
            System.exit(0);
        }

        List<Producto> productos = new ArrayList<>();
        for (String nombre : nombresProductos) {
            String archivoPath = "data/%s.txt".formatted(nombre);
            productos.add(new Producto(nombre, 0, archivoPath));
        }

        return productos;
    }

    public void votar(String nombreProducto) {
        String archivoPath = getArchivoPath(nombreProducto);
        if (archivoPath == null)
            return;

        log.info("Voto registrado");
        String fechaHoraActual = obtenerHora();
        VotacionDAO votacionDAO = new VotacionDAO(archivoPath);
        votacionDAO.registrar(fechaHoraActual);
    }

    public int contarVotos(String nombreProducto) {
        String archivoPath = getArchivoPath(nombreProducto);
        VotacionDAO votacionDAO = new VotacionDAO(archivoPath);
        return votacionDAO.obtenerTodos().size();
    }

    private String obtenerHora() {
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        DateTimeFormatter formatoFechaHora = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return fechaHoraActual.format(formatoFechaHora);
    }

    private String getArchivoPath(String nombreProducto) {
        List<Producto> productos = obtenerProductos();

        for (Producto producto : productos) {
            if (producto.getNombre().equalsIgnoreCase(nombreProducto)) {
                return producto.getArchivoPath();
            }
        }

        log.error("No existe el producto");
        return null;
    }

}

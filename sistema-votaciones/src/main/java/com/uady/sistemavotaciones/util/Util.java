package com.uady.sistemavotaciones.util;

import lombok.extern.log4j.Log4j2;

import java.io.InputStream;
import java.net.URL;

@Log4j2
public class Util {

    public static URL obtenerPath(String filePath) {
        URL path = Util.class.getResource(filePath);
        if (path == null) {
            log.error("Archivo " + filePath +" no encontrado");
            System.exit(1);
        }
        return path;
    }

    public static InputStream obtenerImg(String filePath) {
        InputStream path = Util.class.getResourceAsStream(filePath);
        if (path == null) {
            log.warn("Imagen " + filePath +" no encontrada");
            path = Util.class.getResourceAsStream("/com/uady/sistemavotaciones/images/Default.jpg");
        }
        return path;
    }

}

package com.uady.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Producto implements Dato {

    private String nombre;
    private Integer cantidad;
    private String archivoPath;

    @Override
    public String getNombreDato() {
        return nombre;
    }

    @Override
    public Integer getCantidadDato() {
        return cantidad;
    }

}

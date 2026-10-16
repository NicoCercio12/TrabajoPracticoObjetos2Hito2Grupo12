package com.grupo12.epicentro_gourmet.enums;

import lombok.Getter;

@Getter
public enum RolCocina {
    COCINERO(150000.0),
    COCINERO_AYUDANTE(100000.0),
    LAVAPLATOS(50000.0);

    private final double plusCategoria;

    RolCocina(double plusCategoria) {
        this.plusCategoria = plusCategoria;
    }
}
package com.grupo12.epicentro_gourmet.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class FoodTruck extends UnidadDeVenta {

    private String patente;
    private boolean requiereElectricidad;

    public FoodTruck(String nombreComercial, String codigoUnico, Double superficie,
                     String patente, boolean requiereElectricidad) {
        super(nombreComercial, codigoUnico, superficie);
        this.patente = patente;
        this.requiereElectricidad = requiereElectricidad;
    }
}
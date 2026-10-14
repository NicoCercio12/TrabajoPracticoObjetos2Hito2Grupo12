package com.grupo12.epicentro_gourmet.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter @NoArgsConstructor
public class PuestoDesarmable extends UnidadDeVenta {

    private int cantCarpas;
    private int tiempoMontajeMinutos;

    public PuestoDesarmable(String nombreComercial, String codigoUnico, Double superficie,
                            int cantCarpas, int tiempoMontajeMinutos) {
        super(nombreComercial, codigoUnico, superficie);
        this.cantCarpas = cantCarpas;
        this.tiempoMontajeMinutos = tiempoMontajeMinutos;
    }
}
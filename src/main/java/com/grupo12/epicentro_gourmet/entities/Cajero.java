package com.grupo12.epicentro_gourmet.entities;

import com.grupo12.epicentro_gourmet.enums.Turno;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor
public class Cajero extends Empleado {
    @Enumerated(EnumType.STRING)
    private Turno turno;

    @OneToMany(mappedBy = "cajero", fetch = FetchType.LAZY)
    private Set<CierreCaja> lstCierres = new HashSet<>();

    @Override
    public double calcularSueldo(Festival festival) {
        return 0;
    }
    // calcularSueldo(...), cerrarCaja(...)
}
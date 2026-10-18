package com.grupo12.epicentro_gourmet.entities;

import com.grupo12.epicentro_gourmet.enums.Turno;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor
public class Cajero extends Empleado {
    @Enumerated(EnumType.STRING)
    private Turno turno;

    @OneToMany(mappedBy = "cajero", fetch = FetchType.LAZY)
    private Set<CierreCaja> lstCierres = new HashSet<>();

    public Cajero(String nombre, String apellido, String dni, String email,
                  LocalDate fechaNacimiento, LocalDate fechaIngreso, Turno turno) {
        super(nombre, apellido, dni, email, fechaNacimiento, fechaIngreso);
        this.turno = turno;
    }


    @Override
    public double calcularSueldo(Festival festival) {
        if (festival == null) return 0.0;

        double sueldoBase = 100000.0;
        double montoAntiguedad = calcularAntiguedad() * 5000.0;
        double multiplicador = calcularMultiplicadorDias(festival.getDuracionEnDias());

        return multiplicador * (sueldoBase + montoAntiguedad);
    }
    public CierreCaja cerrarCaja(LocalDate fecha) {
        // Metodo para el cierre de caja del cajero
        return null;
    }
}
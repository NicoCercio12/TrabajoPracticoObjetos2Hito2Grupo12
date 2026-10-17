package com.grupo12.epicentro_gourmet.entities;

import com.grupo12.epicentro_gourmet.enums.RolCocina;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter @NoArgsConstructor

public class Cocinero extends Empleado {
    @Enumerated(EnumType.STRING)
    private RolCocina rolCocina;

    public Cocinero(String nombre, String apellido, String dni, String email,
                    LocalDate fechaNacimiento, LocalDate fechaIngreso, RolCocina rolCocina) {
        super(nombre, apellido, dni, email, fechaNacimiento, fechaIngreso);
        this.rolCocina = rolCocina;
    }


    @Override
    public double calcularSueldo(Festival festival) {
        if (festival == null) return 0.0;

        double sueldoBase = 100000.0;
        double plusCategory = (rolCocina != null) ? rolCocina.getPlusCategoria() : 0.0;
        double multiplicador = calcularMultiplicadorDias(festival.getDuracionEnDias());

        return multiplicador * (sueldoBase + plusCategory);
    }
}
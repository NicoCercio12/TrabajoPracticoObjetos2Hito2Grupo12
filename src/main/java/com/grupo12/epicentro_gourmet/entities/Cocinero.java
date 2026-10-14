package com.grupo12.epicentro_gourmet.entities;

import com.grupo12.epicentro_gourmet.enums.RolCocina;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter @NoArgsConstructor

public class Cocinero extends Empleado {
    @Enumerated(EnumType.STRING)
    private RolCocina rolCocina;

    @Override
    public double calcularSueldo(Festival festival) {
        return 0;
    }
    // calcularSueldo(...)
}
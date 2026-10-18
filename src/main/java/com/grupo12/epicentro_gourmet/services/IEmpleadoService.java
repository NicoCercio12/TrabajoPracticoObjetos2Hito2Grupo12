package com.grupo12.epicentro_gourmet.services;

import com.grupo12.epicentro_gourmet.entities.Empleado;

public interface IEmpleadoService {
    Empleado buscarPorId(Long idEmpleado);
    double calcularLiquidacionHaber(Long idEmpleado, Long idFestival);
}
package com.grupo12.epicentro_gourmet.services;

import com.grupo12.epicentro_gourmet.entities.Empleado;
import java.util.List;
import com.grupo12.epicentro_gourmet.dtos.AltaEmpleadoDTO;

public interface IEmpleadoService {

    Empleado buscarPorId(Long idEmpleado);
    Empleado darDeAlta(AltaEmpleadoDTO dto, String usernameResponsable);
    double calcularLiquidacionHaber(Long idEmpleado, Long idFestival);
    void darDeBaja(Long idEmpleado, String usernameResponsable);
    void reactivar(Long idEmpleado, String usernameResponsable);

    List<Empleado> listarPorResponsable(String usernameResponsable, boolean activo);

}
package com.grupo12.epicentro_gourmet.services;

import com.grupo12.epicentro_gourmet.entities.Empleado;
import java.util.List;

public interface IEmpleadoService {

    Empleado buscarPorId(Long idEmpleado);
    double calcularLiquidacionHaber(Long idEmpleado, Long idFestival);
    void darDeBaja(Long idEmpleado, String usernameResponsable);
    void reactivar(Long idEmpleado, String usernameResponsable);

    List<Empleado> listarPorResponsable(String usernameResponsable, boolean activo);

}
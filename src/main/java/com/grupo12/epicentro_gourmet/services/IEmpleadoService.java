package com.grupo12.epicentro_gourmet.services;

import com.grupo12.epicentro_gourmet.entities.Empleado;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.grupo12.epicentro_gourmet.dtos.AltaEmpleadoDTO;
import com.grupo12.epicentro_gourmet.dtos.ModificarEmpleadoDTO;
import com.grupo12.epicentro_gourmet.entities.Festival;


public interface IEmpleadoService {

    Empleado buscarPorId(Long idEmpleado);
    Empleado darDeAlta(AltaEmpleadoDTO dto, String usernameResponsable);
    double calcularLiquidacionHaber(Long idEmpleado, Long idFestival);
    void darDeBaja(Long idEmpleado, String usernameResponsable);
    void reactivar(Long idEmpleado, String usernameResponsable);
    Empleado buscarPorUsername(String username);
    List<Festival> festivalesDelEmpleado(Empleado empleado);
    List<Empleado> listarPorResponsable(String usernameResponsable, boolean activo);
    Optional<Festival> festivalActivoDelEmpleado(Empleado empleado, LocalDate fecha);
    ModificarEmpleadoDTO obtenerParaModificar(Long idEmpleado, String usernameResponsable);
    void modificar(Long idEmpleado, ModificarEmpleadoDTO dto, String usernameResponsable);
    List<Empleado> filtrarActivosPorResponsable(String usernameResponsable, String texto, Long idUnidad);
    void reasignarUnidad(Long idEmpleado, Long idNuevaUnidad, String usernameResponsable);
}
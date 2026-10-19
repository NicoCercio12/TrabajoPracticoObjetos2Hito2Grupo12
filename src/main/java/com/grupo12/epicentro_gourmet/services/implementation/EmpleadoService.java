package com.grupo12.epicentro_gourmet.services.implementation;

import com.grupo12.epicentro_gourmet.entities.Empleado;
import com.grupo12.epicentro_gourmet.entities.Festival;
import com.grupo12.epicentro_gourmet.repositories.IEmpleadoRepository;
import com.grupo12.epicentro_gourmet.repositories.IFestivalRepository;
import com.grupo12.epicentro_gourmet.services.IEmpleadoService;
import org.springframework.stereotype.Service;
import com.grupo12.epicentro_gourmet.entities.User;
import com.grupo12.epicentro_gourmet.repositories.IUnidadDeVentaRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpleadoService implements IEmpleadoService {

    private final IEmpleadoRepository repository;
    private final IFestivalRepository festivalRepository;
    private final IUnidadDeVentaRepository unidadDeVentaRepository;

    public EmpleadoService(IEmpleadoRepository repository, IFestivalRepository festivalRepository,  IUnidadDeVentaRepository unidadDeVentaRepository) {
        this.repository = repository;
        this.festivalRepository = festivalRepository;
        this.unidadDeVentaRepository = unidadDeVentaRepository;
    }


    @Override
    public Empleado buscarPorId(Long idEmpleado) {
        return repository.findById(idEmpleado)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado con ID: " + idEmpleado));
    }

    @Override
    public double calcularLiquidacionHaber(Long idEmpleado, Long idFestival) {
        Empleado empleado = buscarPorId(idEmpleado);
        Festival festival = festivalRepository.findById(idFestival)
                .orElseThrow(() -> new IllegalArgumentException("Festival no encontrado con ID: " + idFestival));

        return empleado.calcularSueldo(festival);
    }

    @Override
    @Transactional
    public void darDeBaja(Long idEmpleado, String usernameResponsable){

        Empleado empleado = buscarPorId(idEmpleado);

        if (usernameResponsable == null || usernameResponsable.isBlank()) {
            throw new AccessDeniedException("No se identifico al responsable");
        }

        if (empleado.getUnidadAsignada() == null) {
            throw new IllegalArgumentException(
                    "El empleado no tiene una unidad asignada");
        }

        Long idUnidad = empleado.getUnidadAsignada().getId();

        boolean esResponsable =
                unidadDeVentaRepository.existsByIdAndResponsable_Usuario_Username(
                        idUnidad, usernameResponsable);

        if (!esResponsable) {
            throw new AccessDeniedException(
                    "No podés dar de baja empleados de otra unidad");
        }

        if (!empleado.isActivo()) {
            throw new IllegalArgumentException(
                    "El empleado ya está dado de baja");
        }

        empleado.darDeBaja();

        User usuario = empleado.getUsuario();

        if (usuario != null) {
            usuario.setEnabled(false);
        }

        repository.save(empleado);
    }
}
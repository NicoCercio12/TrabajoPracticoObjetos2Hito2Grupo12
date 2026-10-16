package com.grupo12.epicentro_gourmet.services.implementation;

import com.grupo12.epicentro_gourmet.entities.Empleado;
import com.grupo12.epicentro_gourmet.entities.Festival;
import com.grupo12.epicentro_gourmet.repositories.IEmpleadoRepository;
import com.grupo12.epicentro_gourmet.repositories.IFestivalRepository;
import com.grupo12.epicentro_gourmet.services.IEmpleadoService;
import org.springframework.stereotype.Service;

@Service
public class EmpleadoService implements IEmpleadoService {

    private final IEmpleadoRepository repository;
    private final IFestivalRepository festivalRepository;

    public EmpleadoService(IEmpleadoRepository repository, IFestivalRepository festivalRepository) {
        this.repository = repository;
        this.festivalRepository = festivalRepository;
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
}
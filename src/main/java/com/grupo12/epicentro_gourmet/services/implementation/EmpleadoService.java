package com.grupo12.epicentro_gourmet.services.implementation;

import com.grupo12.epicentro_gourmet.repositories.IEmpleadoRepository;
import com.grupo12.epicentro_gourmet.repositories.IFestivalRepository;
import com.grupo12.epicentro_gourmet.services.IEmpleadoService;

public class EmpleadoService implements IEmpleadoService {

    private final IEmpleadoRepository repository;
    private final IFestivalRepository festivalRepository;

    public EmpleadoService(IEmpleadoRepository repository, IFestivalRepository festivalRepository) {
        this.repository = repository;
        this.festivalRepository = festivalRepository;
    }
}

package com.grupo12.epicentro_gourmet.services.implementation;

import com.grupo12.epicentro_gourmet.repositories.IFestivalRepository;
import com.grupo12.epicentro_gourmet.repositories.IUnidadDeVentaRepository;
import com.grupo12.epicentro_gourmet.services.IUnidadDeVentaService;

public class UnidadDeVentaService implements IUnidadDeVentaService {

    private final IUnidadDeVentaRepository repository;
    private final IFestivalRepository festivalRepository;

    public UnidadDeVentaService(IUnidadDeVentaRepository repository, IFestivalRepository festivalRepository) {
        this.repository = repository;
        this.festivalRepository = festivalRepository;
    }
}


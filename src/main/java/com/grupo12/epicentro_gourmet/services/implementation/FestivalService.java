package com.grupo12.epicentro_gourmet.services.implementation;

import com.grupo12.epicentro_gourmet.repositories.IFestivalRepository;
import com.grupo12.epicentro_gourmet.services.IFestivalService;

public class FestivalService implements IFestivalService {

    private final IFestivalRepository repository;

    public FestivalService(IFestivalRepository repository) {
        this.repository = repository;
    }
}

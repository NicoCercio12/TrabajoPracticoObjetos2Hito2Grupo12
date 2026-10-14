package com.grupo12.epicentro_gourmet.services.implementation;

import com.grupo12.epicentro_gourmet.repositories.ICostoRepository;
import com.grupo12.epicentro_gourmet.services.ICostoService;

public class CostoService implements ICostoService {

    private final ICostoRepository repository;

    public CostoService(ICostoRepository repository) {
        this.repository = repository;
    }
}

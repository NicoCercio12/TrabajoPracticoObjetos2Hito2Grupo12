package com.grupo12.epicentro_gourmet.services.implementation;

import com.grupo12.epicentro_gourmet.repositories.IPlatoRepository;
import com.grupo12.epicentro_gourmet.services.IPlatoService;
import org.springframework.stereotype.Service;

@Service
public class PlatoService implements IPlatoService {

    private final IPlatoRepository repository;

    public PlatoService(IPlatoRepository repository) {
        this.repository = repository;
    }
}

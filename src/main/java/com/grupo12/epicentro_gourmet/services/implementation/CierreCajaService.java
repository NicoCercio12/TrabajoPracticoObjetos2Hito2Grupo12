package com.grupo12.epicentro_gourmet.services.implementation;

import com.grupo12.epicentro_gourmet.repositories.ICierreCajaRepository;
import com.grupo12.epicentro_gourmet.services.ICierreCajaService;
import org.springframework.stereotype.Service;

@Service
public class CierreCajaService implements ICierreCajaService {

    private final ICierreCajaRepository repository;

    public CierreCajaService(ICierreCajaRepository repository) {
        this.repository = repository;
    }
}

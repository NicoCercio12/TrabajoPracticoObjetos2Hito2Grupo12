package com.grupo12.epicentro_gourmet.services.implementation;

import com.grupo12.epicentro_gourmet.repositories.IPedidoRepository;
import com.grupo12.epicentro_gourmet.services.IPedidoService;

public class PedidoService implements IPedidoService {

    private final IPedidoRepository repository;

    public PedidoService(IPedidoRepository repository) {
        this.repository = repository;
    }
}

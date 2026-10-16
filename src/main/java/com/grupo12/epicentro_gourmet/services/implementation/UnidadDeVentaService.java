package com.grupo12.epicentro_gourmet.services.implementation;

import com.grupo12.epicentro_gourmet.entities.UnidadDeVenta;
import com.grupo12.epicentro_gourmet.repositories.IFestivalRepository;
import com.grupo12.epicentro_gourmet.repositories.IUnidadDeVentaRepository;
import com.grupo12.epicentro_gourmet.services.IUnidadDeVentaService;
import org.springframework.stereotype.Service;

@Service
public class UnidadDeVentaService implements IUnidadDeVentaService {

    private final IUnidadDeVentaRepository repository;
    private final IFestivalRepository festivalRepository;

    public UnidadDeVentaService(IUnidadDeVentaRepository repository, IFestivalRepository festivalRepository) {
        this.repository = repository;
        this.festivalRepository = festivalRepository;
    }

    @Override
    public UnidadDeVenta buscarPorId(Long idUnidad) {
        return repository.findById(idUnidad)
                .orElseThrow(() -> new IllegalArgumentException("Unidad de Venta no encontrada con ID: " + idUnidad));
    }

    @Override
    public double calcularCanon(Long idUnidad) {
        UnidadDeVenta unidad = buscarPorId(idUnidad);
        return unidad.calcularCanon();
    }
}
package com.grupo12.epicentro_gourmet.services.implementation;

import com.grupo12.epicentro_gourmet.entities.Plato;
import com.grupo12.epicentro_gourmet.entities.UnidadDeVenta;
import com.grupo12.epicentro_gourmet.repositories.IPlatoRepository;
import com.grupo12.epicentro_gourmet.repositories.IUnidadDeVentaRepository;
import com.grupo12.epicentro_gourmet.services.IPlatoService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.grupo12.epicentro_gourmet.dtos.PlatoListadoDTO;
import java.util.List;

@Service
public class PlatoService implements IPlatoService {

    private final IPlatoRepository repository;
    private final IUnidadDeVentaRepository unidadRepository;

    public PlatoService(IPlatoRepository repository, IUnidadDeVentaRepository unidadRepository) {

        this.repository = repository;
        this.unidadRepository = unidadRepository;
    }

    @Override
    @Transactional
    public Plato darDeAlta(String nombre, double precioVenta, double costoProduccion, Long idUnidad, String usernameResponsable) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del plato es obligatorio");
        }

        String nombreNormalizado = nombre.trim();

        if (nombreNormalizado.length() > 255) {
            throw new IllegalArgumentException("El nombre no puede superar los 255 caracteres");
        }

        if (!Double.isFinite(precioVenta) || precioVenta <= 0) {
            throw new IllegalArgumentException("El precio de venta debe ser mayor a cero");
        }

        if (!Double.isFinite(costoProduccion) || costoProduccion < 0) {
            throw new IllegalArgumentException("El costo de producción debe ser cero o mayor");
        }

        if (idUnidad == null || idUnidad <= 0) {
            throw new IllegalArgumentException("Debés seleccionar una unidad de venta válida");
        }

        if (usernameResponsable == null || usernameResponsable.isBlank()) {
            throw new IllegalArgumentException("No se pudo identificar al responsable");
        }

        UnidadDeVenta unidad = unidadRepository.findById(idUnidad).orElseThrow(() -> new IllegalArgumentException("No se encontró la unidad de venta"));

        if (unidad.getResponsable() == null || unidad.getResponsable().getUsuario() == null || !usernameResponsable.equals(
                unidad.getResponsable().getUsuario().getUsername())) {

            throw new IllegalArgumentException("La unidad no pertenece al responsable autenticado");
        }

        for (Plato existente : unidad.getLstPlatos()) {
            if (nombreNormalizado.equalsIgnoreCase(existente.getNombre())) {

                throw new IllegalArgumentException("Ya existe un plato con ese nombre en esta unidad");
            }
        }

        Plato plato = new Plato(nombreNormalizado, precioVenta, costoProduccion);

        Plato platoGuardado = repository.save(plato);

        unidad.getLstPlatos().add(platoGuardado);

        return platoGuardado;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlatoListadoDTO> listarPorResponsable(String usernameResponsable) {

        if (usernameResponsable == null || usernameResponsable.isBlank()) {
            throw new IllegalArgumentException("No se pudo identificar al responsable");
        }

        return repository.listarPorResponsable(usernameResponsable);
    }

}
package com.grupo12.epicentro_gourmet.services.implementation;

import com.grupo12.epicentro_gourmet.dtos.UnidadDeVentaDTO;
import com.grupo12.epicentro_gourmet.entities.FoodTruck;
import com.grupo12.epicentro_gourmet.entities.PuestoDesarmable;
import com.grupo12.epicentro_gourmet.entities.UnidadDeVenta;
import com.grupo12.epicentro_gourmet.repositories.IUnidadDeVentaRepository;
import com.grupo12.epicentro_gourmet.services.IUnidadDeVentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;


  
@Service
@RequiredArgsConstructor
public class UnidadDeVentaService implements IUnidadDeVentaService {

    private final IUnidadDeVentaRepository unidadDeVentaRepository;

    @Override
    @Transactional(readOnly = true)
   public List<UnidadDeVentaDTO> obtenerUnidadesPorFestivalId(Long festivalId) {
        // Consulta las unidades asociadas a dicho festival en la BD
        List<UnidadDeVenta> unidades = unidadDeVentaRepository.findByFestivalesId(festivalId);

        // Mapea la lista a DTOs
        return unidades.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private UnidadDeVentaDTO convertirADTO(UnidadDeVenta uv) {
        UnidadDeVentaDTO.UnidadDeVentaDTOBuilder builder = UnidadDeVentaDTO.builder()
                .id(uv.getId())
                .codigoUnico(uv.getCodigoUnico())
                .nombreComercial(uv.getNombreComercial())
                .superficie(uv.getSuperficie());

        if (uv.getResponsable() != null) {
            builder.responsable(uv.getResponsable().getNombre() + " " + uv.getResponsable().getApellido());
        } else {
            builder.responsable("Sin Asignar");
        }

        // Especialización según tipo
        if (uv instanceof FoodTruck ft) {
            builder.tipoUnidad("Food Truck")
                   .patente(ft.getPatente())
                   .requiereElectricidad(ft.isRequiereElectricidad());
        } else if (uv instanceof PuestoDesarmable pd) {
            builder.tipoUnidad("Puesto Desarmable")
                   .cantCarpas(pd.getCantCarpas())
                   .tiempoMontajeMinutos(pd.getTiempoMontajeMinutos());
        }

        return builder.build();
    }
}


package com.grupo12.epicentro_gourmet.services.implementation;

import com.grupo12.epicentro_gourmet.dtos.CocineroRequestDTO;
import com.grupo12.epicentro_gourmet.dtos.CocineroResumenDTO;
import com.grupo12.epicentro_gourmet.entities.Cocinero;
import com.grupo12.epicentro_gourmet.entities.Festival;
import com.grupo12.epicentro_gourmet.entities.UnidadDeVenta;
import com.grupo12.epicentro_gourmet.enums.RolCocina;
import com.grupo12.epicentro_gourmet.repositories.ICocineroRepository;
import com.grupo12.epicentro_gourmet.repositories.IEmpleadoRepository;
import com.grupo12.epicentro_gourmet.repositories.IFestivalRepository;
import com.grupo12.epicentro_gourmet.repositories.IUnidadDeVentaRepository;
import com.grupo12.epicentro_gourmet.services.ICocineroService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CocineroService implements ICocineroService {

    private final ICocineroRepository cocineroRepository;
    private final IEmpleadoRepository empleadoRepository;
    private final IUnidadDeVentaRepository unidadDeVentaRepository;
    private final IFestivalRepository festivalRepository;

    public CocineroService(
            ICocineroRepository cocineroRepository,
            IEmpleadoRepository empleadoRepository,
            IUnidadDeVentaRepository unidadDeVentaRepository,
            IFestivalRepository festivalRepository
    ) {
        this.cocineroRepository = cocineroRepository;
        this.empleadoRepository = empleadoRepository;
        this.unidadDeVentaRepository = unidadDeVentaRepository;
        this.festivalRepository = festivalRepository;
    }

    @Override
    @Transactional
    public CocineroResumenDTO crearCocinero(CocineroRequestDTO requestDTO) {
        if (empleadoRepository.existsByDni(requestDTO.getDni())) {
            throw new IllegalArgumentException("Ya existe un empleado registrado con el DNI: " + requestDTO.getDni());
        }

        Cocinero cocinero = new Cocinero(
                requestDTO.getNombre(),
                requestDTO.getApellido(),
                requestDTO.getDni(),
                requestDTO.getEmail(),
                requestDTO.getFechaNacimiento(),
                requestDTO.getFechaIngreso(),
                requestDTO.getRolCocina()
        );

        if (!cocinero.esMayorDeEdad()) {
            throw new IllegalArgumentException("El cocinero debe ser mayor de edad");
        }

        if (requestDTO.getUnidadAsignadaId() != null) {
            UnidadDeVenta unidad = unidadDeVentaRepository.findById(requestDTO.getUnidadAsignadaId())
                    .orElseThrow(() -> new IllegalArgumentException("Unidad de venta no encontrada"));
            cocinero.setUnidadAsignada(unidad);
        }

        Cocinero guardado = cocineroRepository.save(cocinero);

        Festival festival = null;
        if (requestDTO.getFestivalIdCalculo() != null) {
            festival = festivalRepository.findById(requestDTO.getFestivalIdCalculo())
                    .orElseThrow(() -> new IllegalArgumentException("Festival no encontrado"));
        }

        return mapearAResumenDTO(guardado, festival);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CocineroResumenDTO> buscarCocineros(
            Long unidadId,
            RolCocina rolCocina,
            Boolean activo,
            Long festivalId
    ) {
        Festival festival = null;
        if (festivalId != null) {
            festival = festivalRepository.findById(festivalId)
                    .orElseThrow(() -> new IllegalArgumentException("Festival no encontrado"));
        }

        final Festival festivalFinal = festival;
        return cocineroRepository.buscarConFiltros(unidadId, rolCocina, activo).stream()
                .map(c -> mapearAResumenDTO(c, festivalFinal))
                .toList();
    }

    private CocineroResumenDTO mapearAResumenDTO(Cocinero cocinero, Festival festival) {
        Long unidadId = cocinero.getUnidadAsignada() != null ? cocinero.getUnidadAsignada().getId() : null;
        String nombreUnidad = cocinero.getUnidadAsignada() != null ? cocinero.getUnidadAsignada().getNombreComercial() : null;
        double sueldo = cocinero.calcularSueldo(festival);

        return CocineroResumenDTO.builder()
                .idEmpleado(cocinero.getIdEmpleado())
                .nombre(cocinero.getNombre())
                .apellido(cocinero.getApellido())
                .dni(cocinero.getDni())
                .activo(cocinero.isActivo())
                .rolCocina(cocinero.getRolCocina())
                .unidadAsignadaId(unidadId)
                .nombreUnidadAsignada(nombreUnidad)
                .antiguedadAnios(cocinero.calcularAntiguedad())
                .fechaIngreso(cocinero.getFechaIngreso())
                .sueldoEstimado(sueldo)
                .build();
    }
}
package com.grupo12.epicentro_gourmet.services.implementation;

import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grupo12.epicentro_gourmet.dtos.RegistroUnidadDTO;
import com.grupo12.epicentro_gourmet.entities.Cajero;
import com.grupo12.epicentro_gourmet.entities.Cocinero;
import com.grupo12.epicentro_gourmet.entities.Empleado;
import com.grupo12.epicentro_gourmet.entities.FoodTruck;
import com.grupo12.epicentro_gourmet.entities.PuestoDesarmable;
import com.grupo12.epicentro_gourmet.entities.UnidadDeVenta;
import com.grupo12.epicentro_gourmet.entities.User;
import com.grupo12.epicentro_gourmet.enums.TipoEmpleado;
import com.grupo12.epicentro_gourmet.enums.TipoUnidad;
import com.grupo12.epicentro_gourmet.helpers.CodigoUnicoHelper;
import com.grupo12.epicentro_gourmet.helpers.Roles;
import com.grupo12.epicentro_gourmet.repositories.IEmpleadoRepository;
import com.grupo12.epicentro_gourmet.repositories.IUnidadDeVentaRepository;
import com.grupo12.epicentro_gourmet.repositories.IUserRepository;
import com.grupo12.epicentro_gourmet.services.IRegistroService;

@Service
public class RegistroService implements IRegistroService {

    private static final Pattern PATENTE = Pattern.compile("^([A-Z]{3}\\d{3}|[A-Z]{2}\\d{3}[A-Z]{2})$");

    private final IUserRepository userRepository;
    private final IEmpleadoRepository empleadoRepository;
    private final IUnidadDeVentaRepository unidadRepository;

    public RegistroService(IUserRepository userRepository, IEmpleadoRepository empleadoRepository,
                           IUnidadDeVentaRepository unidadRepository) {
        this.userRepository = userRepository;
        this.empleadoRepository = empleadoRepository;
        this.unidadRepository = unidadRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean tieneResponsable(String username) {
        return userRepository.findByUsername(username)
                .map(empleadoRepository::existsByUsuario)
                .orElse(false);
    }

    @Transactional
    public UnidadDeVenta registrarUnidadConResponsable(RegistroUnidadDTO dto, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la cuenta"));

        boolean aprobada = user.getUserRoles().stream()
                .anyMatch(r -> Roles.RESPONSABLE.equals(r.getRole()));
        if (!aprobada)
            throw new IllegalArgumentException("Tu cuenta todavía no fue aprobada por un administrador");

        Optional<Empleado> existente = empleadoRepository.findByUsuario(user);

        // --- Ya tiene responsable: solo se agrega la unidad ---
        if (existente.isPresent()) {
            validarUnidad(dto);
            Empleado responsable = existente.get();

            UnidadDeVenta unidad = crearUnidad(dto);
            unidad.setResponsable(responsable);
            unidad = unidadRepository.save(unidad);

            if (responsable.getUnidadAsignada() == null) {
                responsable.setUnidadAsignada(unidad);
                empleadoRepository.save(responsable);
            }
            return unidad;
        }

        // --- Primera vez: se crea el responsable y la unidad ---
        validarResponsable(dto);
        validarUnidad(dto);

        Empleado responsable = crearEmpleado(dto);
        if (!responsable.esMayorDeEdad())
            throw new IllegalArgumentException("El responsable debe ser mayor de edad");
        responsable.setUsuario(user);

        UnidadDeVenta unidad = unidadRepository.save(crearUnidad(dto));

        responsable.setUnidadAsignada(unidad);
        empleadoRepository.save(responsable);
        unidad.setResponsable(responsable);
        return unidadRepository.save(unidad);
    }

    private void validarResponsable(RegistroUnidadDTO dto) {
        if (vacio(dto.getNombre()))
            throw new IllegalArgumentException("El nombre es obligatorio");
        if (vacio(dto.getApellido()))
            throw new IllegalArgumentException("El apellido es obligatorio");
        if (vacio(dto.getDni()))
            throw new IllegalArgumentException("El DNI es obligatorio");
        if (vacio(dto.getEmail()))
            throw new IllegalArgumentException("El email es obligatorio");
        if (dto.getFechaNacimiento() == null)
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria");
        if (dto.getFechaIngreso() == null)
            throw new IllegalArgumentException("La fecha de ingreso es obligatoria");
        if (dto.getTipoEmpleado() == null)
            throw new IllegalArgumentException("Elegí el tipo de empleado");

        if (empleadoRepository.existsByDni(dto.getDni()))
            throw new IllegalArgumentException("Ya existe un empleado con ese DNI");

        if (dto.getTipoEmpleado() == TipoEmpleado.COCINERO && dto.getRolCocina() == null)
            throw new IllegalArgumentException("Elegí el rol en la cocina");
        if (dto.getTipoEmpleado() == TipoEmpleado.CAJERO && dto.getTurno() == null)
            throw new IllegalArgumentException("Elegí el turno del cajero");
    }

    private void validarUnidad(RegistroUnidadDTO dto) {
        String codigo = dto.getCodigoUnico().trim().toUpperCase();
        if (!CodigoUnicoHelper.esValido(codigo, dto.getTipoUnidad()))
            throw new IllegalArgumentException("El código único debe tener 10 caracteres alfanuméricos y empezar con "
                    + (dto.getTipoUnidad() == TipoUnidad.FOOD_TRUCK ? "FT" : "PD"));
        if (unidadRepository.existsByCodigoUnico(codigo))
            throw new IllegalArgumentException("Ya existe una unidad con ese código único");

        if (dto.getTipoUnidad() == TipoUnidad.FOOD_TRUCK) {
            String patente = dto.getPatente() == null ? "" : dto.getPatente().trim().toUpperCase();
            if (!PATENTE.matcher(patente).matches())
                throw new IllegalArgumentException("La patente no es válida (ej.: ABC123 o AB123CD)");
        } else {
            if (dto.getCantCarpas() == null || dto.getCantCarpas() <= 0)
                throw new IllegalArgumentException("La cantidad de carpas debe ser mayor a 0");
            if (dto.getTiempoMontajeMinutos() == null || dto.getTiempoMontajeMinutos() <= 0)
                throw new IllegalArgumentException("El tiempo de montaje debe ser mayor a 0");
        }
    }

    private boolean vacio(String s) {
        return s == null || s.isBlank();
    }

    private Empleado crearEmpleado(RegistroUnidadDTO dto) {
        if (dto.getTipoEmpleado() == TipoEmpleado.COCINERO) {
            return new Cocinero(dto.getNombre(), dto.getApellido(), dto.getDni(), dto.getEmail(),
                    dto.getFechaNacimiento(), dto.getFechaIngreso(), dto.getRolCocina());
        }
        return new Cajero(dto.getNombre(), dto.getApellido(), dto.getDni(), dto.getEmail(),
                dto.getFechaNacimiento(), dto.getFechaIngreso(), dto.getTurno());
    }

    private UnidadDeVenta crearUnidad(RegistroUnidadDTO dto) {
        String codigo = dto.getCodigoUnico().trim().toUpperCase();
        if (dto.getTipoUnidad() == TipoUnidad.FOOD_TRUCK) {
            return new FoodTruck(dto.getNombreComercial(), codigo, dto.getSuperficie(),
                    dto.getPatente().trim().toUpperCase(), dto.isRequiereElectricidad());
        }
        return new PuestoDesarmable(dto.getNombreComercial(), codigo, dto.getSuperficie(),
                dto.getCantCarpas(), dto.getTiempoMontajeMinutos());
    }
}
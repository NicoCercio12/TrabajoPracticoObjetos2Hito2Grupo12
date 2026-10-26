package com.grupo12.epicentro_gourmet.services.implementation;

import com.grupo12.epicentro_gourmet.entities.Empleado;
import com.grupo12.epicentro_gourmet.entities.Festival;
import com.grupo12.epicentro_gourmet.repositories.IEmpleadoRepository;
import com.grupo12.epicentro_gourmet.repositories.IFestivalRepository;
import com.grupo12.epicentro_gourmet.services.IEmpleadoService;
import org.springframework.stereotype.Service;
import com.grupo12.epicentro_gourmet.entities.User;
import com.grupo12.epicentro_gourmet.repositories.IUnidadDeVentaRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Locale;
import com.grupo12.epicentro_gourmet.dtos.AltaEmpleadoDTO;
import com.grupo12.epicentro_gourmet.entities.Cajero;
import com.grupo12.epicentro_gourmet.entities.Cocinero;
import com.grupo12.epicentro_gourmet.entities.UnidadDeVenta;
import com.grupo12.epicentro_gourmet.enums.TipoEmpleado;
import com.grupo12.epicentro_gourmet.helpers.GeneradorPassword;

@Service
public class EmpleadoService implements IEmpleadoService {

    private final IEmpleadoRepository repository;
    private final IFestivalRepository festivalRepository;
    private final IUnidadDeVentaRepository unidadDeVentaRepository;
    private final CuentaService cuentaService;
    private final EmailService emailService;

    public EmpleadoService(IEmpleadoRepository repository, IFestivalRepository festivalRepository,  IUnidadDeVentaRepository unidadDeVentaRepository, CuentaService cuentaService, EmailService emailService) {
        this.repository = repository;
        this.festivalRepository = festivalRepository;
        this.unidadDeVentaRepository = unidadDeVentaRepository;
        this.cuentaService = cuentaService;
        this.emailService = emailService;
    }

    @Override
    public Empleado buscarPorId(Long idEmpleado) {
        return repository.findById(idEmpleado).orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado con ID: " + idEmpleado));
    }

    @Override
    public double calcularLiquidacionHaber(Long idEmpleado, Long idFestival) {
        Empleado empleado = buscarPorId(idEmpleado);
        Festival festival = festivalRepository.findById(idFestival).orElseThrow(() -> new IllegalArgumentException("Festival no encontrado con ID: " + idFestival));

        return empleado.calcularSueldo(festival);
    }

    @Override
    @Transactional
    public void darDeBaja(Long idEmpleado, String usernameResponsable){

        Empleado empleado = buscarPorId(idEmpleado);

        if (usernameResponsable == null || usernameResponsable.isBlank()) {
            throw new AccessDeniedException("No se identifico al responsable");
        }

        if (empleado.getUnidadAsignada() == null) {
            throw new IllegalArgumentException("El empleado no tiene una unidad asignada");
        }

        Long idUnidad = empleado.getUnidadAsignada().getId();

        boolean esResponsable = unidadDeVentaRepository.existsByIdAndResponsable_Usuario_Username(idUnidad, usernameResponsable);

        if (!esResponsable) {
            throw new AccessDeniedException("No podés dar de baja empleados de otra unidad");
        }

        if (!empleado.isActivo()) {
            throw new IllegalArgumentException("El empleado ya está dado de baja");
        }

        if (empleado.getUsuario() != null && usernameResponsable.equals(empleado.getUsuario().getUsername())) {
            throw new IllegalArgumentException("No podés dar de baja tu propia cuenta");
        }

        empleado.darDeBaja();

        User usuario = empleado.getUsuario();

        if (usuario != null) {
            usuario.setEnabled(false);
        }

        repository.save(empleado);
    }

    @Override
    @Transactional
    public void reactivar(Long idEmpleado, String usernameResponsable) {

        if (usernameResponsable == null || usernameResponsable.isBlank()) {
            throw new AccessDeniedException("No se identificó al responsable");
        }

        Empleado empleado = buscarPorId(idEmpleado);

        if (empleado.getUnidadAsignada() == null) {
            throw new IllegalArgumentException("El empleado no tiene una unidad asignada");
        }

        Long idUnidad = empleado.getUnidadAsignada().getId();

        boolean esResponsable =
                unidadDeVentaRepository.existsByIdAndResponsable_Usuario_Username(idUnidad, usernameResponsable);

        if (!esResponsable) {
            throw new AccessDeniedException("No podés reactivar empleados de otra unidad");
        }

        if (empleado.isActivo()) {
            throw new IllegalArgumentException("El empleado ya está activo");
        }

        empleado.reactivar();

        User usuario = empleado.getUsuario();

        if (usuario != null) {
            usuario.setEnabled(true);
        }

        repository.save(empleado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empleado> listarPorResponsable(String username, boolean activo) {
        return repository.findByUnidadAsignada_Responsable_Usuario_UsernameAndActivo(username, activo);
    }

    @Override
    @Transactional
    public Empleado darDeAlta(AltaEmpleadoDTO dto, String usernameResponsable) {

        // Identificar al responsable.
        if (usernameResponsable == null || usernameResponsable.isBlank()) {
            throw new AccessDeniedException("No se identificó al responsable");
        }

        if (dto == null || dto.getIdUnidad() == null) {
            throw new IllegalArgumentException("Seleccioná una unidad de venta");
        }

        // Comprobar que la unidad pertenece al responsable.
        boolean esResponsable = unidadDeVentaRepository.existsByIdAndResponsable_Usuario_Username(dto.getIdUnidad(), usernameResponsable);

        if (!esResponsable) {
            throw new AccessDeniedException("No podés agregar empleados a otra unidad");
        }

        UnidadDeVenta unidad = unidadDeVentaRepository.findById(dto.getIdUnidad()).orElseThrow(() -> new IllegalArgumentException("La unidad de venta no existe"));

        // Validar los datos obligatorios.
        if (dto.getNombre() == null || dto.getNombre().isBlank()
                || dto.getApellido() == null || dto.getApellido().isBlank()
                || dto.getDni() == null || dto.getDni().isBlank()
                || dto.getEmail() == null || dto.getEmail().isBlank()
                || dto.getFechaNacimiento() == null
                || dto.getFechaIngreso() == null
                || dto.getTipoEmpleado() == null) {

            throw new IllegalArgumentException("Completá los datos obligatorios del empleado");
        }

        String dni = dto.getDni().trim();
        String email = dto.getEmail().trim().toLowerCase(Locale.ROOT);

        // No duplicar empleados, incluidos los dados de baja.
        Optional<Empleado> existente = repository.findByDni(dni);

        if (existente.isPresent()) {
            if (!existente.get().isActivo()) {
                throw new IllegalArgumentException("El empleado está dado de baja. Usá la opción Reactivar");
            }
            throw new IllegalArgumentException("Ya existe un empleado activo con ese DNI");
        }

        // Crear el tipo de empleado seleccionado.
        Empleado empleado;

        if (dto.getTipoEmpleado() == TipoEmpleado.COCINERO) {

            if (dto.getRolCocina() == null) {
                throw new IllegalArgumentException("Seleccioná el rol en la cocina");
            }

            empleado = new Cocinero(dto.getNombre().trim(), dto.getApellido().trim(), dni, email, dto.getFechaNacimiento(), dto.getFechaIngreso(), dto.getRolCocina());

        } else if (dto.getTipoEmpleado() == TipoEmpleado.CAJERO) {

            if (dto.getTurno() == null) {
                throw new IllegalArgumentException("Seleccioná el turno del cajero");
            }

            empleado = new Cajero(dto.getNombre().trim(), dto.getApellido().trim(), dni, email, dto.getFechaNacimiento(), dto.getFechaIngreso(), dto.getTurno());

        } else {
            throw new IllegalArgumentException("El tipo de empleado no es válido");
        }

        if (!empleado.esMayorDeEdad()) {
            throw new IllegalArgumentException("El empleado debe ser mayor de edad");
        }

        // Crear la cuenta con contraseña aleatoria y rol EMPLEADO.
        String passwordGenerada = GeneradorPassword.generar();

        User usuario = cuentaService.crearCuentaEmpleado(email, passwordGenerada);

        empleado.setUsuario(usuario);
        empleado.setUnidadAsignada(unidad);
        empleado.setActivo(true);

        // Ejecutar las operaciones pendientes en la base antes del envío.
        Empleado empleadoGuardado = repository.saveAndFlush(empleado);

        emailService.enviarCredenciales(email, empleado.getNombre(), passwordGenerada);

        return empleadoGuardado;
    }

}
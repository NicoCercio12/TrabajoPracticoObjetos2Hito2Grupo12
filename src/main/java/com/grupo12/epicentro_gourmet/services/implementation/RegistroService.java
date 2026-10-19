package com.grupo12.epicentro_gourmet.services.implementation;

import java.util.regex.Pattern;

import com.grupo12.epicentro_gourmet.enums.Turno;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.grupo12.epicentro_gourmet.entities.UserRole;
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
    private final PasswordEncoder passwordEncoder;

    public RegistroService(IUserRepository userRepository, IEmpleadoRepository empleadoRepository,
                           IUnidadDeVentaRepository unidadRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.empleadoRepository = empleadoRepository;
        this.unidadRepository = unidadRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UnidadDeVenta registrarUnidadConResponsable(RegistroUnidadDTO dto, String username) {
        validar(dto);

        // 1) Cuenta existente (la de la sesión)
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la cuenta"));
        if (empleadoRepository.existsByUsuario(user))
            throw new IllegalArgumentException("Esta cuenta ya tiene un responsable registrado");

        // La cuenta pasa de PENDIENTE a RESPONSABLE
        user.getUserRoles().stream()
                .filter(r -> Roles.PENDIENTE.equals(r.getRole()))
                .forEach(r -> r.setRole(Roles.RESPONSABLE));
        if (user.getUserRoles().stream().noneMatch(r -> Roles.RESPONSABLE.equals(r.getRole())))
            user.getUserRoles().add(new UserRole(user, Roles.RESPONSABLE));

        // 2) Empleado responsable (cocinero o cajero)
        Empleado responsable = crearEmpleado(dto);
        if (!responsable.esMayorDeEdad())
            throw new IllegalArgumentException("El responsable debe ser mayor de edad");
        responsable.setUsuario(user);

        // 3) Unidad de venta
        UnidadDeVenta unidad = unidadRepository.save(crearUnidad(dto));

        // 4) Vínculos
        responsable.setUnidadAsignada(unidad);
        empleadoRepository.save(responsable);
        unidad.setResponsable(responsable);
        return unidad;
    }

    private void validar(RegistroUnidadDTO dto) {
        if (empleadoRepository.existsByDni(dto.getDni()))
            throw new IllegalArgumentException("Ya existe un empleado con ese DNI");

        String codigo = dto.getCodigoUnico().trim().toUpperCase();
        if (!CodigoUnicoHelper.esValido(codigo, dto.getTipoUnidad()))
            throw new IllegalArgumentException("El código único debe tener 10 caracteres alfanuméricos y empezar con "
                    + (dto.getTipoUnidad() == TipoUnidad.FOOD_TRUCK ? "FT" : "PD"));
        if (unidadRepository.existsByCodigoUnico(codigo))
            throw new IllegalArgumentException("Ya existe una unidad con ese código único");

        if (dto.getTipoEmpleado() == TipoEmpleado.COCINERO && dto.getRolCocina() == null)
            throw new IllegalArgumentException("Elegí el rol en la cocina");
        if (dto.getTipoEmpleado() == TipoEmpleado.CAJERO && dto.getTurno() == null)
            throw new IllegalArgumentException("Elegí el turno del cajero");

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

    private Empleado crearEmpleado(RegistroUnidadDTO dto) {
        if (dto.getTipoEmpleado() == TipoEmpleado.COCINERO) {
            return new Cocinero(dto.getNombre(), dto.getApellido(), dto.getDni(), dto.getEmail(),
                    dto.getFechaNacimiento(), dto.getFechaIngreso(), dto.getRolCocina());
        }
        return new Cajero(dto.getNombre(), dto.getApellido(), dto.getDni(), dto.getEmail(),
                dto.getFechaNacimiento(), dto.getFechaIngreso(), (Turno) dto.getTurno());
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
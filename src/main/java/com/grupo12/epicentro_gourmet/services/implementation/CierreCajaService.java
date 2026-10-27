package com.grupo12.epicentro_gourmet.services.implementation;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.grupo12.epicentro_gourmet.entities.Cajero;
import com.grupo12.epicentro_gourmet.entities.CierreCaja;
import com.grupo12.epicentro_gourmet.entities.Empleado;
import com.grupo12.epicentro_gourmet.repositories.ICierreCajaRepository;
import com.grupo12.epicentro_gourmet.repositories.IEmpleadoRepository;
import com.grupo12.epicentro_gourmet.repositories.IUserRepository;
import com.grupo12.epicentro_gourmet.services.ICierreCajaService;

@Service
public class CierreCajaService implements ICierreCajaService {

    private final ICierreCajaRepository cierreRepository;
    private final IEmpleadoRepository empleadoRepository;
    private final IUserRepository userRepository;

    public CierreCajaService(ICierreCajaRepository cierreRepository,
                             IEmpleadoRepository empleadoRepository,
                             IUserRepository userRepository) {
        this.cierreRepository = cierreRepository;
        this.empleadoRepository = empleadoRepository;
        this.userRepository = userRepository;
    }

    private Cajero buscarCajero(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario inexistente"));
        Empleado e = empleadoRepository.findByUsuario(user)
                .orElseThrow(() -> new IllegalArgumentException("Tu usuario no está asociado a un empleado"));
        if (!(e instanceof Cajero cajero)) {
            throw new IllegalArgumentException("Solo un cajero puede cerrar la caja");
        }
        if (!cajero.isActivo()) {
            throw new IllegalArgumentException("El cajero está dado de baja");
        }
        return cajero;
    }

    @Override
    @Transactional(readOnly = true)
    public double totalDelDia(String username) {
        return calcularTotal(buscarCajero(username), LocalDate.now());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean yaCerroHoy(String username) {
        return cierreRepository.existsByCajeroAndFecha(buscarCajero(username), LocalDate.now());
    }

    @Override
    @Transactional
    public CierreCaja cerrarCaja(String username) {
        Cajero cajero = buscarCajero(username);
        LocalDate hoy = LocalDate.now();

        CierreCaja cierre = cajero.cerrarCaja(hoy, calcularTotal(cajero, hoy));
        return cierreRepository.save(cierre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CierreCaja> historial(String username) {
        return cierreRepository.findByCajeroOrderByFechaDesc(buscarCajero(username));
    }

    // TODO: reemplazar por la suma de las ventas del cajero en esa fecha
    private double calcularTotal(Cajero cajero, LocalDate fecha) {
        return 0.0;
    }
}
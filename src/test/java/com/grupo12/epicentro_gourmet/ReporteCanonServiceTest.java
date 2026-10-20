
package com.grupo12.epicentro_gourmet;

import com.grupo12.epicentro_gourmet.dtos.ReporteMayoresCanonDTO;
import com.grupo12.epicentro_gourmet.entities.Festival;
import com.grupo12.epicentro_gourmet.repositories.IFestivalRepository;
import com.grupo12.epicentro_gourmet.services.implementation.ReporteCanonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReporteCanonServiceTest {

    @Autowired
    private IFestivalRepository festivalRepository;

    @Test
    void obtenerMayoresCanon_debeDevolverLosDosMayores() {
        Festival festival = festivalRepository.findById(1L)
                .orElseThrow(() ->
                        new RuntimeException("No existe el festival con ID 1"));

        ReporteCanonService servicio = new ReporteCanonService();

        List<ReporteMayoresCanonDTO> resultado =
                servicio.obtenerMayoresCanon(festival, 2);

        assertEquals(2, resultado.size());

        assertEquals("Truck Prueba C",
                resultado.get(0).getNombreComercial());
        assertEquals(12000.0,
                resultado.get(0).getCanon(), 0.01);

        assertEquals("Truck Prueba B",
                resultado.get(1).getNombreComercial());
        assertEquals(9500.0,
                resultado.get(1).getCanon(), 0.01);
    }
}


package com.grupo12.epicentro_gourmet.repositories;

import com.grupo12.epicentro_gourmet.entities.Cajero;
import com.grupo12.epicentro_gourmet.entities.CierreCaja;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ICierreCajaRepository extends JpaRepository<CierreCaja,Long> {

    boolean existsByCajeroAndFecha(Cajero cajero, LocalDate fecha);
    List<CierreCaja> findByCajeroOrderByFechaDesc(Cajero cajero);


}

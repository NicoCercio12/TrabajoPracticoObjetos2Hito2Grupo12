package com.grupo12.epicentro_gourmet.services;

import com.grupo12.epicentro_gourmet.entities.CierreCaja;
import jakarta.transaction.Transactional;

import java.util.List;

public interface ICierreCajaService {

    double totalDelDia(String username);

    boolean yaCerroHoy(String username);

    CierreCaja cerrarCaja(String username);

    List<CierreCaja> historial(String username);
}


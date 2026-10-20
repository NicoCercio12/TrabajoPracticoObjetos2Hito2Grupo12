package com.grupo12.epicentro_gourmet.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class ReporteMayoresCanonDTO {

        private String nombreComercial;
        private String codigoUnico;
        private String tipoUnidad;
        private double canon;

}

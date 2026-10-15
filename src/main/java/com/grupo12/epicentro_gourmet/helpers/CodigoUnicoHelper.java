package com.grupo12.epicentro_gourmet.helpers;

import com.grupo12.epicentro_gourmet.enums.TipoUnidad;

import java.util.regex.Pattern;

public class CodigoUnicoHelper {

    private static final Pattern ALFANUMERICO = Pattern.compile("^[A-Z0-9]+$");

    private CodigoUnicoHelper() {}

    // Son 10 caracteres alfanumericos. Empieza con FT (food truck) o PD (puesto desarmable)
    public static boolean esValido(String codigo, TipoUnidad tipo) {
        if (codigo == null || codigo.length() != Configuracion.CODIGO_UNICO_LONGITUD) return false;
        if (!ALFANUMERICO.matcher(codigo).matches()) return false;
        String prefijo = (tipo == TipoUnidad.FOOD_TRUCK)
                ? Configuracion.PREFIJO_CODIGO_FOOD_TRUCK
                : Configuracion.PREFIJO_CODIGO_PUESTO;
        return codigo.startsWith(prefijo);
    }
}

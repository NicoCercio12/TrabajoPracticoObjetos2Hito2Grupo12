package com.grupo12.epicentro_gourmet.helpers;

import java.security.SecureRandom;

public final class GeneradorPassword {

    private static final String CARACTERES =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                    + "abcdefghijklmnopqrstuvwxyz"
                    + "0123456789"
                    + "!@#$%";

    private static final SecureRandom RANDOM = new SecureRandom();

    private GeneradorPassword() {
    }

    public static String generar() {
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 16; i++) {
            int posicion = RANDOM.nextInt(CARACTERES.length());
            char caracter = CARACTERES.charAt(posicion);

            password.append(caracter);
        }

        return password.toString();
    }
}
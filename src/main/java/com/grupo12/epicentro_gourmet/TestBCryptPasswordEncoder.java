package com.grupo12.epicentro_gourmet;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestBCryptPasswordEncoder {

    public static void main(String[] args) {
        BCryptPasswordEncoder pe = new BCryptPasswordEncoder();
        System.out.println(pe.encode("user"));
    }
}
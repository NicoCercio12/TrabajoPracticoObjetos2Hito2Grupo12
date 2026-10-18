package com.grupo12.epicentro_gourmet.controllers;

import java.util.List;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    private boolean estaLogueado(Authentication auth) {
        return auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
    }

    @ModelAttribute("logueado")
    public boolean logueado(Authentication auth) {
        return estaLogueado(auth);
    }

    @ModelAttribute("username")
    public String username(Authentication auth) {
        return estaLogueado(auth) ? auth.getName() : null;
    }

    @ModelAttribute("roles")
    public List<String> roles(Authentication auth) {
        return estaLogueado(auth)
                ? auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
                : List.of();
    }
}
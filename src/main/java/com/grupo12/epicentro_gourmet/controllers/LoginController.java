package com.grupo12.epicentro_gourmet.controllers;

import com.grupo12.epicentro_gourmet.helpers.Roles;
import com.grupo12.epicentro_gourmet.helpers.ViewRouteHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return ViewRouteHelper.LOGIN;
    }

    @GetMapping("/loginsuccess")
    public String loginSuccess(Authentication auth) {
        if (auth == null) {
            return "redirect:/login";
        }

        // 1. Administrador -> Centro de control general
        if (tieneRol(auth, Roles.ADMINISTRADOR)) {
            return "redirect:/admin";
        }

        // 2. Responsable de puesto -> Gestión de su unidad / personal / menú
        if (tieneRol(auth, Roles.RESPONSABLE)) {
            return "redirect:/responsable";
        }

        // 3. Empleado -> Vista operativa / registro de pedidos / jornada
        if (tieneRol(auth, Roles.EMPLEADO)) {
            return "redirect:/empleado";
        }

        // 4. Pendiente -> Pantalla de espera o alta de unidad de venta
        if (tieneRol(auth, Roles.PENDIENTE)) {
            return "redirect:/cuenta-pendiente";
        }

        return "redirect:/";
    }

    // Método auxiliar para comparar roles con o sin el prefijo "ROLE_"
    private boolean tieneRol(Authentication auth, String rol) {
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_" + rol) || a.equals(rol));
    }
}

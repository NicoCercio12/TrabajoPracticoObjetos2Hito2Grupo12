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
    public String login(){
        return ViewRouteHelper.LOGIN;
    }

    @GetMapping("/loginsuccess")
    public String loginSuccess(Authentication auth) {
        Set<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        if (roles.contains("ROLE_" + Roles.ADMINISTRADOR) || roles.contains("ROLE_" + Roles.RESPONSABLE)) {
            return "redirect:/";
        }
        if (roles.contains("ROLE_" + Roles.CAJERO) || roles.contains("ROLE_" + Roles.COCINERO)) {
            return "redirect:/empleado/mi-perfil";
        }
        if (roles.contains("ROLE_" + Roles.PENDIENTE)) {
            return "redirect:/cuenta-pendiente";
        }
        return "redirect:/";
    }
}




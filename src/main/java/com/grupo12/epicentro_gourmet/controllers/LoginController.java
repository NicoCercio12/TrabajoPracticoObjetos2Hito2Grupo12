package com.grupo12.epicentro_gourmet.controllers;

import com.grupo12.epicentro_gourmet.helpers.Roles;
import com.grupo12.epicentro_gourmet.helpers.ViewRouteHelper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(){
        return ViewRouteHelper.LOGIN;
    }

    @GetMapping("/loginsuccess")
    public String loginSuccess(Authentication auth) {
        boolean pendiente = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + Roles.PENDIENTE));
        return pendiente ? "redirect:/cuenta-pendiente" : "redirect:/";
    }


}

package com.grupo12.epicentro_gourmet.controllers;

import com.grupo12.epicentro_gourmet.helpers.ViewRouteHelper;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.grupo12.epicentro_gourmet.services.IFestivalService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final IFestivalService festivalService;


    public HomeController(IFestivalService festivalService) {
        this.festivalService = festivalService;
    }

    @GetMapping("/")
    public String index(Model model, Authentication auth) {
        model.addAttribute("festivales", festivalService.getAll());
        boolean logueado = auth != null && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken);
        model.addAttribute("logueado", logueado);
        if (logueado) {
            model.addAttribute("username", auth.getName());
            model.addAttribute("roles", auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority).toList());
        }
        return ViewRouteHelper.INDEX;
    }
}

package com.grupo12.epicentro_gourmet.controllers;

import com.grupo12.epicentro_gourmet.dtos.FestivalDTO;
import com.grupo12.epicentro_gourmet.services.IFestivalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/festivales")
@RequiredArgsConstructor
public class FestivalController {

    private final IFestivalService festivalService;

    @GetMapping
    public String listarFestivales(Model model) {
        List<FestivalDTO> festivales = festivalService.obtenerTodosDTO();
        model.addAttribute("festivales", festivales);
        model.addAttribute("titulo", "Listado de Festivales");
        return "festivales/lista";
    }
}
package com.grupo12.epicentro_gourmet.controllers;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.grupo12.epicentro_gourmet.repositories.IFestivalRepository;

@Controller
public class ReporteIndexController {

    private final IFestivalRepository festivalRepository;

    public ReporteIndexController(IFestivalRepository festivalRepository) {
        this.festivalRepository = festivalRepository;
    }

    @GetMapping("/reportes")
    public String index(Model model) {
        model.addAttribute("festivales",
                festivalRepository.findAll(Sort.by(Sort.Direction.DESC, "fechaInicio")));
        return "reportes/index";
    }
}
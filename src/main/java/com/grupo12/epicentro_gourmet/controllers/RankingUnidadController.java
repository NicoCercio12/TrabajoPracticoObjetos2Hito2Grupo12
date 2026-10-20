package com.grupo12.epicentro_gourmet.controllers;

import com.grupo12.epicentro_gourmet.dtos.RankingUnidadDTO;
import com.grupo12.epicentro_gourmet.entities.Festival;
import com.grupo12.epicentro_gourmet.services.IFestivalService;
import com.grupo12.epicentro_gourmet.services.IRankingUnidadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/reportes/ranking")
public class RankingUnidadController {

    private final IRankingUnidadService rankingUnidadService;
    private final IFestivalService festivalService;

    public RankingUnidadController(
            IRankingUnidadService rankingUnidadService,
            IFestivalService festivalService) {
        this.rankingUnidadService = rankingUnidadService;
        this.festivalService = festivalService;
    }

    @GetMapping
    public String mostrarRanking(
            @RequestParam Long idFestival,
            Model model) {

        Festival festival = festivalService.findById(idFestival)
                .orElseThrow(() ->
                        new IllegalArgumentException("Festival no encontrado"));

        List<RankingUnidadDTO> ranking =
                rankingUnidadService.obtenerRankingPorFestival(idFestival);

        model.addAttribute("ranking", ranking);
        model.addAttribute("festival", festival);

        return "reportes/ranking-unidades";
    }
}
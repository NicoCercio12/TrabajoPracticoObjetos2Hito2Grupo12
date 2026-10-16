package com.grupo12.epicentro_gourmet.controllers;

import com.grupo12.epicentro_gourmet.dtos.FestivalDTO;
import com.grupo12.epicentro_gourmet.dtos.UnidadDeVentaDTO;
import com.grupo12.epicentro_gourmet.services.IFestivalService;
import com.grupo12.epicentro_gourmet.services.IUnidadDeVentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/festivales")
@RequiredArgsConstructor
public class FestivalController {

    private final IFestivalService festivalService;
    private final IUnidadDeVentaService unidadDeVentaService;

    //MOSTRAR UNIDADES DE VENTA AL ADMIN
    @GetMapping
    public String listarFestivales(Model model) {
        List<FestivalDTO> festivales = festivalService.obtenerTodosDTO();
        model.addAttribute("festivales", festivales);
        model.addAttribute("titulo", "Listado de Festivales");
        return "festivales/lista"; // Renderiza templates/festivales/lista.html
    }


    //MOSTRAR UNIDADES DE VENTA DE UN FESTIVAL
    @GetMapping("/{id}/unidades")
    public String verUnidadesDeFestival(@PathVariable("id") Long id, Model model) {
        // Pedir al FestivalService los datos de cabecera del festival
        FestivalDTO festival = festivalService.obtenerPorIdDTO(id);
        
        // Pedir al UnidadDeVentaService las unidades de ese festival
        List<UnidadDeVentaDTO> unidades = unidadDeVentaService.obtenerUnidadesPorFestivalId(id);

        model.addAttribute("festival", festival);
        model.addAttribute("unidades", unidades);
        model.addAttribute("titulo", "Unidades de Venta - " + festival.getNombre());

        return "festivales/unidades";
    }
}
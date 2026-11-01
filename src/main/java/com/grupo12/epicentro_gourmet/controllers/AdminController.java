package com.grupo12.epicentro_gourmet.controllers;
import com.grupo12.epicentro_gourmet.dtos.UnidadDeVentaDTO;
import com.grupo12.epicentro_gourmet.dtos.FestivalDTO;
import com.grupo12.epicentro_gourmet.repositories.IEmpleadoRepository;
import com.grupo12.epicentro_gourmet.repositories.IUnidadDeVentaRepository;
import com.grupo12.epicentro_gourmet.services.IPedidoService;
import com.grupo12.epicentro_gourmet.services.IUnidadDeVentaService;
import com.grupo12.epicentro_gourmet.services.implementation.FestivalService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final IEmpleadoRepository empleadoRepository;
    private final IUnidadDeVentaRepository unidadRepository;
    private final FestivalService festivalService;
    private final IUnidadDeVentaService unidadDeVentaService;
    private final IPedidoService pedidoService;

    public AdminController(IEmpleadoRepository empleadoRepository, 
                           IUnidadDeVentaRepository unidadRepository,
                           FestivalService festivalService,IUnidadDeVentaService unidadDeVentaService,IPedidoService pedidoService) {
        this.empleadoRepository = empleadoRepository;
        this.unidadRepository = unidadRepository;
        this.festivalService = festivalService;
        this.unidadDeVentaService = unidadDeVentaService;
        this.pedidoService = pedidoService;
    }

    // Ruta principal: http://localhost:8080/admin
    @GetMapping({"", "/"})
    public String dashboard(Model model, Authentication authentication) {
        if (authentication != null) {
            model.addAttribute("username", authentication.getName());
        }
        // Carga los festivales para la tabla del centro de comando
        model.addAttribute("festivales", festivalService.getAll());
        
        // Muestra src/main/resources/templates/admin/dashboard.html
        return "admin/dashboard";
    }

    @GetMapping("/empleados")
    public String empleados(@RequestParam(required = false) Boolean activo, Model model) {
        model.addAttribute("empleados",
                activo == null ? empleadoRepository.findAll() : empleadoRepository.findByActivo(activo));
        model.addAttribute("filtro", activo);

        return "admin/empleados";
    }

    @GetMapping("/unidades")
    public String unidades(Model model) {
        model.addAttribute("unidades", unidadRepository.findAll());
        return "admin/unidades";
    }


      @GetMapping("/{id}/unidades")
    public String verUnidadesDeFestival(@PathVariable("id") Long id, Model model) {
        FestivalDTO festival = festivalService.obtenerPorIdDTO(id);
        List<UnidadDeVentaDTO> unidades = unidadDeVentaService.obtenerUnidadesPorFestivalId(id);

        // 👉 Opcional: Si quieres inyectar el plato estrella por cada unidad para mostrarlo en el HTML,
        // puedes recorrerlas o consultar según lo necesites en tu lógica.

        model.addAttribute("festival", festival);
        model.addAttribute("unidades", unidades);
        model.addAttribute("pedidoService", pedidoService);
        model.addAttribute("titulo", "Unidades de Venta - " + festival.getNombre());

        return "admin/unidades";
    }
}
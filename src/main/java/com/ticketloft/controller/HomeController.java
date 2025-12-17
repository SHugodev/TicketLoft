package com.ticketloft.controller;

import com.ticketloft.model.Evento;
import com.ticketloft.model.Categoria;
import com.ticketloft.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private EventoService eventoService;

    /**
     * Página de inicio
     */
    @GetMapping("/")
    public String index(Model model) {
        // Obtener eventos públicos (aprobados y futuros)
        List<Evento> eventos = eventoService.obtenerEventosPublicos();
        
        // Limitar a 6 eventos para la página principal
        if (eventos.size() > 6) {
            eventos = eventos.subList(0, 6);
        }

        // Obtener todas las categorías
        List<Categoria> categorias = eventoService.obtenerTodasLasCategorias();

        model.addAttribute("eventos", eventos);
        model.addAttribute("categorias", categorias);

        return "index";
    }
}
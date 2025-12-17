package com.ticketloft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    // Autenticación
    @GetMapping("/login")
    public String login() {
        return "auth";
    }

    @GetMapping("/register")
    public String register() {
        return "redirect:/login";
    }

    // Redirigir a EventoController
    @GetMapping("/crear-evento")
    public String crearEvento() {
        return "redirect:/eventos/crear";
    }

    @GetMapping("/mis-eventos")
    public String misEventos() {
        return "redirect:/eventos/mis-eventos";
    }

}
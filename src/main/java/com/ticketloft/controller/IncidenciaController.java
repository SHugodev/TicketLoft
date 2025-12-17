package com.ticketloft.controller;

import com.ticketloft.model.Incidencia;
import com.ticketloft.model.Usuario;
import com.ticketloft.service.IncidenciaService;
import com.ticketloft.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/incidencias")
public class IncidenciaController {

    @Autowired
    private IncidenciaService incidenciaService;

    @Autowired
    private UsuarioService usuarioService;

    // Formulario para crear nueva incidencia
    @GetMapping("/nueva")
    public String mostrarFormulario(Model model) {
        return "incidencias/formulario";
    }

    // Guardar incidencia
    @PostMapping("/guardar")
    public String guardarIncidencia(
            @RequestParam String asunto,
            @RequestParam String descripcion,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = usuarioService.obtenerUsuarioActual().orElse(null);
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión para contactarnos");
            return "redirect:/login";
        }

        try {
            incidenciaService.crearIncidencia(usuario, asunto, descripcion);
            redirectAttributes.addFlashAttribute("success",
                    "Tu mensaje ha sido enviado correctamente. Te responderemos pronto.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al enviar el mensaje: " + e.getMessage());
        }

        return "redirect:/incidencias/nueva";
    }

    // Vista de administrador para listar incidencias
    @GetMapping("/admin")
    public String listarIncidencias(Model model) {
        List<Incidencia> incidencias = incidenciaService.obtenerTodas();
        model.addAttribute("incidencias", incidencias);
        return "admin/incidencias";
    }

    // Ver detalle de una incidencia
    @GetMapping("/admin/ver/{id}")
    public String verIncidencia(@PathVariable Long id, Model model) {
        Incidencia incidencia = incidenciaService.obtenerPorId(id)
                .orElseThrow(() -> new RuntimeException("Incidencia no encontrada"));
        model.addAttribute("incidencia", incidencia);
        return "admin/ver-incidencia";
    }

    // Resolver incidencia (Admin)
    @PostMapping("/admin/resolver/{id}")
    public String resolverIncidencia(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            incidenciaService.resolverIncidencia(id);
            redirectAttributes.addFlashAttribute("success", "Incidencia marcada como resuelta");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/incidencias/admin";
    }
}

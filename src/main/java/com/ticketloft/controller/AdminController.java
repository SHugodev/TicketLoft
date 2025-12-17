package com.ticketloft.controller;

import com.ticketloft.model.*;
import com.ticketloft.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')") // Asegura que solo admins accedan
public class AdminController {

    @Autowired
    private EventoService eventoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ReservaService reservaService;

    /**
     * Dashboard principal
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            model.addAttribute("totalEventos", eventoService.contarEventos());
            model.addAttribute("totalUsuarios", usuarioService.contarUsuarios());
            model.addAttribute("totalReservas", reservaService.contarReservas());
            model.addAttribute("eventosActivos", eventoService.contarEventosActivos());

            return "admin/dashboard";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al cargar el dashboard: " + e.getMessage());
            return "error";
        }
    }

    /**
     * Gestión de eventos
     */
    @GetMapping("/eventos")
    public String gestionEventos(Model model) {
        List<Evento> eventos = eventoService.obtenerTodosLosEventos();
        List<Evento> eventosPendientes = eventoService.obtenerEventosPendientes();

        model.addAttribute("eventos", eventos);
        model.addAttribute("eventosPendientes", eventosPendientes);

        return "admin/eventos";
    }

    /**
     * Aprobar evento
     */
    @PostMapping("/eventos/aprobar/{id}")
    public String aprobarEvento(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            eventoService.aprobarEvento(id);
            redirectAttributes.addFlashAttribute("success", "Evento aprobado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al aprobar evento: " + e.getMessage());
        }
        return "redirect:/admin/eventos";
    }

    /**
     * Rechazar evento
     */
    @PostMapping("/eventos/rechazar/{id}")
    public String rechazarEvento(
            @PathVariable Long id,
            @RequestParam String motivo,
            RedirectAttributes redirectAttributes) {
        try {
            eventoService.rechazarEvento(id, motivo);
            redirectAttributes.addFlashAttribute("success", "Evento rechazado");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al rechazar evento: " + e.getMessage());
        }
        return "redirect:/admin/eventos";
    }

    /**
     * Eliminar evento
     */
    @PostMapping("/eventos/eliminar/{id}")
    public String eliminarEvento(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            eventoService.eliminarEventoDefinitivamente(id);
            redirectAttributes.addFlashAttribute("success", "Evento eliminado correctamente (Hard Delete)");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar evento: " + e.getMessage());
        }
        return "redirect:/admin/eventos";
    }

    /**
     * Gestión de usuarios
     */
    @GetMapping("/usuarios")
    public String gestionUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
        model.addAttribute("usuarios", usuarios);
        return "admin/usuarios";
    }

    /**
     * Cambiar estado de usuario
     */
    @PostMapping("/usuarios/toggle-activo/{id}")
    public String toggleActivoUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.toggleActivoUsuario(id);
            redirectAttributes.addFlashAttribute("success", "Estado del usuario actualizado");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar usuario: " + e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    /**
     * Cambiar rol de usuario
     */
    @PostMapping("/usuarios/cambiar-rol/{id}")
    public String cambiarRolUsuario(
            @PathVariable Long id,
            @RequestParam String rol,
            RedirectAttributes redirectAttributes) {
        try {
            Usuario.Rol nuevoRol = Usuario.Rol.valueOf(rol);
            usuarioService.cambiarRolUsuario(id, nuevoRol);
            redirectAttributes.addFlashAttribute("success", "Rol actualizado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cambiar rol: " + e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    /**
     * Gestión de reservas
     */
    @GetMapping("/reservas")
    public String gestionReservas(Model model) {
        List<Reserva> reservas = reservaService.obtenerTodasLasReservas();
        model.addAttribute("reservas", reservas);
        return "admin/reservas";
    }
}
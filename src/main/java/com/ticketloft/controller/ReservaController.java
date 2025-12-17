package com.ticketloft.controller;

import com.ticketloft.model.*;
import com.ticketloft.service.*;
import com.ticketloft.repository.TipoEntradaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private EventoService eventoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TipoEntradaRepository tipoEntradaRepository;

    /**
     * Ver mis reservas
     */
    @GetMapping("/mis-reservas")
    public String misReservas(Model model, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioService.obtenerUsuarioActual().orElse(null);

        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión");
            return "redirect:/login";
        }

        List<Reserva> reservas = reservaService.obtenerReservasPorUsuario(usuario);
        model.addAttribute("reservas", reservas);

        return "reservas/mis-reservas";
    }

    /**
     * Confirmar reserva (muestra formulario de pago)
     */
    @PostMapping("/confirmar/{eventoId}")
    public String confirmarReserva(
            @PathVariable Long eventoId,
            @RequestParam Long tipoEntradaId,
            @RequestParam Integer cantidad,
            Model model,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = usuarioService.obtenerUsuarioActual().orElse(null);

        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión");
            return "redirect:/login";
        }

        Evento evento = eventoService.obtenerEventoPorId(eventoId).orElse(null);
        if (evento == null) {
            redirectAttributes.addFlashAttribute("error", "Evento no encontrado");
            return "redirect:/eventos";
        }

        TipoEntrada tipoEntrada = tipoEntradaRepository.findById(tipoEntradaId).orElse(null);
        if (tipoEntrada == null) {
            redirectAttributes.addFlashAttribute("error", "Tipo de entrada no encontrado");
            return "redirect:/eventos/" + eventoId;
        }

        // Calcular precio total con gastos de gestión (3%)
        BigDecimal precioBase = tipoEntrada.getPrecio().multiply(new BigDecimal(cantidad));
        BigDecimal gastosGestion = precioBase.multiply(new BigDecimal("0.03"));
        BigDecimal precioTotal = precioBase.add(gastosGestion);

        model.addAttribute("evento", evento);
        model.addAttribute("tipoEntrada", tipoEntrada);
        model.addAttribute("cantidad", cantidad);
        model.addAttribute("precioBase", precioBase);
        model.addAttribute("gastosGestion", gastosGestion);
        model.addAttribute("precioTotal", precioTotal);

        return "reservas/confirmar-pago";
    }

    /**
     * Procesar pago (simulado)
     */
    @PostMapping("/procesar-pago/{eventoId}")
    public String procesarPago(
            @PathVariable Long eventoId,
            @RequestParam Long tipoEntradaId,
            @RequestParam Integer cantidad,
            @RequestParam String numeroTarjeta,
            @RequestParam String nombreTitular,
            @RequestParam String fechaExpiracion,
            @RequestParam String cvv,
            Model model,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = usuarioService.obtenerUsuarioActual().orElse(null);

        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión");
            return "redirect:/login";
        }

        try {
            Evento evento = eventoService.obtenerEventoPorId(eventoId)
                    .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

            TipoEntrada tipoEntrada = tipoEntradaRepository.findById(tipoEntradaId)
                    .orElseThrow(() -> new RuntimeException("Tipo de entrada no encontrado"));

            // Simular validación de tarjeta (en producción, usar pasarela de pago real)
            if (numeroTarjeta.isEmpty() || nombreTitular.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Datos de pago incompletos");
                return "redirect:/eventos/" + eventoId;
            }

            // Crear reserva
            Reserva reserva = reservaService.crearReserva(usuario, evento, tipoEntrada, cantidad);

            model.addAttribute("reserva", reserva);
            return "reservas/confirmacion";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al procesar el pago: " + e.getMessage());
            return "redirect:/eventos/" + eventoId;
        }
    }

    /**
     * Cancelar reserva
     */
    @PostMapping("/cancelar/{id}")
    public String cancelarReserva(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioService.obtenerUsuarioActual().orElse(null);

        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión");
            return "redirect:/login";
        }

        try {
            reservaService.cancelarReserva(id, usuario);
            redirectAttributes.addFlashAttribute("success", "Reserva cancelada correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cancelar reserva: " + e.getMessage());
        }

        return "redirect:/reservas/mis-reservas";
    }
}
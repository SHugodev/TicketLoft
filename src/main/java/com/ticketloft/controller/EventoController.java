package com.ticketloft.controller;

import com.ticketloft.model.*;
import com.ticketloft.repository.TipoEntradaRepository;
import com.ticketloft.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/eventos")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TipoEntradaRepository tipoEntradaRepository;

    /**
     * Listar todos los eventos públicos
     */
    @GetMapping
    public String listarEventos(
            @RequestParam(required = false) String buscar,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) Long categoria,
            Model model) {

        List<Evento> eventos;

        // Aplicar filtros
        if (categoria != null) {
            eventos = eventoService.obtenerEventosPorCategoria(categoria);
            // Añadir la categoría seleccionada al modelo para mostrar su nombre
            eventoService.obtenerCategoriaPorId(categoria).ifPresent(cat -> {
                model.addAttribute("categoriaActual", cat);
                model.addAttribute("categoriaSeleccionada", cat.getId());
            });
        } else if (ciudad != null && !ciudad.isEmpty()) {
            eventos = eventoService.obtenerEventosPorCiudad(ciudad);
        } else {
            eventos = eventoService.obtenerEventosPublicos();
        }

        // Filtro de búsqueda por nombre
        if (buscar != null && !buscar.isEmpty()) {
            String buscarLower = buscar.toLowerCase();
            eventos = eventos.stream()
                    .filter(e -> e.getNombre().toLowerCase().contains(buscarLower))
                    .toList();
        }

        model.addAttribute("eventos", eventos);
        model.addAttribute("categorias", eventoService.obtenerTodasLasCategorias());

        return "eventos/lista";
    }

    /**
     * Ver detalle de un evento
     */
    @GetMapping("/{id}")
    public String verDetalle(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Evento evento = eventoService.obtenerEventoPorId(id)
                .orElse(null);

        if (evento == null) {
            redirectAttributes.addFlashAttribute("error", "Evento no encontrado");
            return "redirect:/eventos";
        }

        // Obtener tipos de entrada
        List<TipoEntrada> tiposEntrada = eventoService.obtenerTiposEntradaPorEvento(evento);

        // Verificar si el usuario actual tiene reserva
        boolean tieneReserva = false;
        Usuario usuarioActual = usuarioService.obtenerUsuarioActual().orElse(null);
        if (usuarioActual != null) {
            tieneReserva = reservaService.usuarioTieneReserva(usuarioActual, evento);
        }

        model.addAttribute("evento", evento);
        model.addAttribute("tiposEntrada", tiposEntrada);
        model.addAttribute("plazasDisponibles", evento.getPlazasDisponibles());
        model.addAttribute("tieneReserva", tieneReserva);

        return "eventos/detalle";
    }

    /**
     * Listar eventos del usuario
     */
    @GetMapping("/mis-eventos")
    public String misEventos(Model model, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioService.obtenerUsuarioActual().orElse(null);

        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión");
            return "redirect:/auth/login";
        }

        List<Evento> eventos = eventoService.obtenerEventosPorUsuario(usuario);
        model.addAttribute("eventos", eventos);

        return "eventos/mis-eventos";
    }

    /**
     * Mostrar formulario para crear evento
     */
    /**
     * Mostrar formulario para crear evento
     */
    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioService.obtenerUsuarioActual().orElse(null);

        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión");
            return "redirect:/auth/login";
        }

        model.addAttribute("evento", new Evento());
        model.addAttribute("categorias", eventoService.obtenerTodasLasCategorias());
        model.addAttribute("esEdicion", false);
        return "eventos/formulario";
    }

    /**
     * Mostrar formulario para editar evento
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioService.obtenerUsuarioActual().orElse(null);

        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión");
            return "redirect:/auth/login";
        }

        Evento evento = eventoService.obtenerEventoPorId(id).orElse(null);
        if (evento == null) {
            redirectAttributes.addFlashAttribute("error", "Evento no encontrado");
            return "redirect:/eventos/mis-eventos";
        }

        // Verificar permisos
        if (!evento.getCreadoPor().getId().equals(usuario.getId()) && usuario.getRol() != Usuario.Rol.ADMIN) {
            redirectAttributes.addFlashAttribute("error", "No tienes permiso para editar este evento");
            return "redirect:/eventos/mis-eventos";
        }

        model.addAttribute("evento", evento);
        model.addAttribute("categorias", eventoService.obtenerTodasLasCategorias());
        model.addAttribute("tiposEntrada", eventoService.obtenerTiposEntradaPorEvento(evento));
        model.addAttribute("esEdicion", true);
        return "eventos/formulario";
    }

    /**
     * Guardar nuevo evento
     */
    @PostMapping("/guardar")
    public String guardarEvento(
            @ModelAttribute Evento evento,
            @RequestParam(required = false) List<String> tipoNombre,
            @RequestParam(required = false) List<String> tipoDescripcion,
            @RequestParam(required = false) List<Double> tipoPrecio,
            @RequestParam(required = false) List<Integer> tipoCantidad,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = usuarioService.obtenerUsuarioActual().orElse(null);

        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión");
            return "redirect:/auth/login";
        }

        try {
            // Validar listas de entradas
            if (tipoNombre != null) {
                int size = tipoNombre.size();
                if ((tipoDescripcion != null && tipoDescripcion.size() != size) ||
                        (tipoPrecio != null && tipoPrecio.size() != size) ||
                        (tipoCantidad != null && tipoCantidad.size() != size)) {
                    throw new IllegalArgumentException("Datos de tipos de entrada inconsistentes");
                }
            }

            // Guardar evento (crear o actualizar)
            Evento eventoGuardado;
            if (evento.getId() != null) {
                // Es actualización
                Evento eventoExistente = eventoService.obtenerEventoPorId(evento.getId()).orElse(null);
                if (eventoExistente != null) {
                    // Verificar permisos nuevamente
                    if (!eventoExistente.getCreadoPor().getId().equals(usuario.getId())
                            && usuario.getRol() != Usuario.Rol.ADMIN) {
                        throw new IllegalAccessException("No tienes permiso para editar este evento");
                    }
                    // Actualizar campos
                    eventoExistente.setNombre(evento.getNombre());
                    eventoExistente.setDescripcion(evento.getDescripcion());
                    eventoExistente.setFecha(evento.getFecha());
                    eventoExistente.setLugar(evento.getLugar());
                    eventoExistente.setCiudad(evento.getCiudad());
                    eventoExistente.setAforo(evento.getAforo());
                    eventoExistente.setCategoria(evento.getCategoria());
                    eventoExistente.setImagenUrl(evento.getImagenUrl());

                    // Si se edita, vuelve a pendiente de aprobación si no es admin
                    if (usuario.getRol() != Usuario.Rol.ADMIN) {
                        eventoExistente.setEstadoAprobacion(Evento.EstadoAprobacion.PENDIENTE);
                    }

                    eventoGuardado = eventoService.actualizarEvento(eventoExistente);

                    // Eliminar tipos de entrada antiguos si es edición (para simplificar, los
                    // recreamos)
                    // En una app real se debería hacer un merge más inteligente
                    List<TipoEntrada> tiposAntiguos = eventoService.obtenerTiposEntradaPorEvento(eventoExistente);
                    for (TipoEntrada te : tiposAntiguos) {
                        tipoEntradaRepository.delete(te);
                    }
                } else {
                    throw new IllegalArgumentException("Evento no encontrado para actualizar");
                }
            } else {
                // Es creación
                eventoGuardado = eventoService.crearEvento(evento, usuario);
            }

            // Guardar tipos de entrada si existen
            if (tipoNombre != null && !tipoNombre.isEmpty()) {
                for (int i = 0; i < tipoNombre.size(); i++) {
                    // Validaciones básicas
                    String nombre = tipoNombre.get(i);
                    Double precio = (tipoPrecio != null && tipoPrecio.size() > i) ? tipoPrecio.get(i) : 0.0;
                    Integer cantidad = (tipoCantidad != null && tipoCantidad.size() > i) ? tipoCantidad.get(i) : 0;

                    if (nombre == null || nombre.trim().isEmpty())
                        continue;
                    if (precio == null || precio < 0)
                        throw new IllegalArgumentException("El precio no puede ser negativo");
                    if (cantidad == null || cantidad < 0)
                        throw new IllegalArgumentException("La cantidad no puede ser negativa");

                    TipoEntrada tipoEntrada = new TipoEntrada();
                    tipoEntrada.setEvento(eventoGuardado);
                    tipoEntrada.setNombre(nombre);
                    tipoEntrada.setDescripcion(tipoDescripcion != null ? tipoDescripcion.get(i) : "");
                    tipoEntrada.setPrecio(new java.math.BigDecimal(precio));
                    tipoEntrada.setCantidadDisponible(cantidad);
                    tipoEntrada.setActivo(true);

                    tipoEntradaRepository.save(tipoEntrada);
                }
            }

            redirectAttributes.addFlashAttribute("success", "Evento creado correctamente. Pendiente de aprobación.");
            return "redirect:/eventos/mis-eventos";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear evento: " + e.getMessage());
            return "redirect:/eventos/crear";
        }
    }

    /**
     * Eliminar evento
     */
    @PostMapping("/eliminar/{id}")
    public String eliminarEvento(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Usuario usuario = usuarioService.obtenerUsuarioActual().orElse(null);

        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debes iniciar sesión");
            return "redirect:/auth/login";
        }

        try {
            Evento evento = eventoService.obtenerEventoPorId(id).orElse(null);

            if (evento == null) {
                redirectAttributes.addFlashAttribute("error", "Evento no encontrado");
                return "redirect:/eventos/mis-eventos";
            }

            // Verificar que el usuario es el creador
            if (!evento.getCreadoPor().getId().equals(usuario.getId()) &&
                    usuario.getRol() != Usuario.Rol.ADMIN) {
                redirectAttributes.addFlashAttribute("error", "No tienes permiso para eliminar este evento");
                return "redirect:/eventos/mis-eventos";
            }

            eventoService.eliminarEvento(id);
            redirectAttributes.addFlashAttribute("success", "Evento eliminado correctamente");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar evento");
        }

        return "redirect:/eventos/mis-eventos";
    }

}
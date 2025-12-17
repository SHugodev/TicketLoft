package com.ticketloft.controller;

import com.ticketloft.model.TipoEntrada;
import com.ticketloft.service.TipoEntradaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/eventos/{eventoId}/tipos-entrada")
public class TipoEntradaController {

    @Autowired
    private TipoEntradaService tipoEntradaService;

    // --- C: CREAR TipoEntrada (Solo Creador del Evento/Admin) ---
    // Endpoint: POST /api/eventos/{eventoId}/tipos-entrada
    @PostMapping
    public ResponseEntity<?> crearTipoEntrada(
            @PathVariable Long eventoId, 
            @RequestBody TipoEntrada tipoEntrada) {
        
        // **IMPORTANTE**: Se debe verificar si el usuario autenticado es el creador del Evento o un ADMIN.
        try {
            TipoEntrada nuevoTipo = tipoEntradaService.crear(tipoEntrada, eventoId);
            return new ResponseEntity<>(nuevoTipo, HttpStatus.CREATED); // 201
        } catch (RuntimeException e) {
            String mensaje = e.getMessage();
            if (mensaje.contains("Evento no encontrado")) {
                return new ResponseEntity<>(mensaje, HttpStatus.NOT_FOUND); // 404
            }
            return new ResponseEntity<>(mensaje, HttpStatus.BAD_REQUEST); // 400
        }
    }

    // --- R: LEER Todos por Evento (Público/General) ---
    // Endpoint: GET /api/eventos/{eventoId}/tipos-entrada
    @GetMapping
    public ResponseEntity<?> obtenerTiposPorEvento(@PathVariable Long eventoId) {
        try {
            // El servicio usa el EventoRepository para validar que el evento exista
            List<TipoEntrada> tipos = tipoEntradaService.obtenerPorEvento(eventoId);
            return new ResponseEntity<>(tipos, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // 404
        }
    }

    // --- R: LEER por ID (Público/General) ---
    // Endpoint: GET /api/eventos/{eventoId}/tipos-entrada/{id}
    @GetMapping("/{id}")
    public ResponseEntity<TipoEntrada> obtenerTipoEntradaPorId(@PathVariable Long id) {
        return tipoEntradaService.obtenerPorId(id)
            .map(tipo -> new ResponseEntity<>(tipo, HttpStatus.OK))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tipo de entrada no encontrado"));
    }

    // --- U: ACTUALIZAR TipoEntrada (Solo Creador del Evento/Admin) ---
    // Endpoint: PUT /api/eventos/{eventoId}/tipos-entrada/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarTipoEntrada(@PathVariable Long id, @RequestBody TipoEntrada tipoEntradaActualizada) {
        // **IMPORTANTE**: Se debe verificar si el usuario autenticado tiene permisos sobre este TipoEntrada.
        try {
            TipoEntrada tipoActualizado = tipoEntradaService.actualizar(id, tipoEntradaActualizada);
            return new ResponseEntity<>(tipoActualizado, HttpStatus.OK); // 200
        } catch (RuntimeException e) {
            String mensaje = e.getMessage();
            if (mensaje.contains("no encontrado")) {
                return new ResponseEntity<>(mensaje, HttpStatus.NOT_FOUND); // 404
            }
            // Captura la restricción sobre la reducción de inventario
            if (mensaje.contains("solo puede ser aumentada")) {
                return new ResponseEntity<>(mensaje, HttpStatus.BAD_REQUEST); // 400
            }
            return new ResponseEntity<>(mensaje, HttpStatus.INTERNAL_SERVER_ERROR); // 500
        }
    }

    // --- D: DESACTIVAR (Soft Delete - Solo Creador del Evento/Admin) ---
    // Endpoint: DELETE /api/eventos/{eventoId}/tipos-entrada/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> desactivarTipoEntrada(@PathVariable Long id) {
        // **IMPORTANTE**: Se debe verificar si el usuario autenticado tiene permisos sobre este TipoEntrada.
        try {
            tipoEntradaService.desactivar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // 404
        }
    }
}
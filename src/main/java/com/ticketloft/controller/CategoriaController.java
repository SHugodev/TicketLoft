package com.ticketloft.controller;

import com.ticketloft.model.Categoria;
import com.ticketloft.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException; // Importar para manejar excepciones

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categorias") // Base URL para /api/categorias
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    // --- C: CREAR (POST) ---
    // Endpoint: POST /api/categorias
    @PostMapping
    public ResponseEntity<?> crearCategoria(@RequestBody Categoria categoria) {
        try {
            // Llama al método 'crear' del servicio, que incluye la validación de nombre único
            Categoria nuevaCategoria = categoriaService.crear(categoria);
            // Devuelve 201 (Created) con el objeto creado
            return new ResponseEntity<>(nuevaCategoria, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Si el servicio lanza una excepción (ej. "Ya existe una categoría con ese nombre")
            // Devuelve 409 Conflict o 400 Bad Request
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    // --- R: LEER TODOS (GET) ---
    // Endpoint: GET /api/categorias
    @GetMapping
    public ResponseEntity<List<Categoria>> obtenerTodasLasCategorias() {
        // Llama al método 'obtenerTodas' del servicio
        List<Categoria> categorias = categoriaService.obtenerTodos();
        // Devuelve 200 (OK) con la lista
        return new ResponseEntity<>(categorias, HttpStatus.OK);
    }

    // --- R: LEER POR ID (GET) ---
    // Endpoint: GET /api/categorias/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerCategoriaPorId(@PathVariable Long id) {
        // Llama al método 'obtenerPorId' del servicio
        Optional<Categoria> categoria = categoriaService.obtenerPorId(id);
        
        // Si está presente, devuelve 200 (OK)
        return categoria.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                // Si no está presente, devuelve 404 (Not Found)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    // --- U: ACTUALIZAR (PUT) ---
    // Endpoint: PUT /api/categorias/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCategoria(@PathVariable Long id, @RequestBody Categoria categoriaActualizada) {
        try {
            // Llama al método 'actualizar' del servicio, que maneja la búsqueda y la validación de nombre
            Categoria categoriaModificada = categoriaService.actualizar(id, categoriaActualizada);
            // Devuelve 200 (OK) con el objeto actualizado
            return new ResponseEntity<>(categoriaModificada, HttpStatus.OK);
        } catch (RuntimeException e) {
            // El servicio lanza excepciones si:
            // 1. La categoría no existe -> Mapea a 404 Not Found
            if (e.getMessage().contains("no encontrada")) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            }
            // 2. Ya existe otra categoría con el nuevo nombre -> Mapea a 409 Conflict
            if (e.getMessage().contains("Ya existe")) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
            }
            // Para otras excepciones, se puede devolver 500 (Internal Server Error)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // --- D: ELIMINAR (DELETE) ---
    // Endpoint: DELETE /api/categorias/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCategoria(@PathVariable Long id) {
        try {
            // Llama al método 'eliminar' del servicio, que valida si existe
            categoriaService.eliminar(id);
            // Devuelve 204 (No Content) para indicar éxito sin cuerpo de respuesta
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            // Si el servicio lanza la excepción "Categoría no encontrada"
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
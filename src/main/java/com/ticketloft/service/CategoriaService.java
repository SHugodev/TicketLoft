package com.ticketloft.service;

import com.ticketloft.model.Categoria;
import com.ticketloft.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Nota: Si estuvieras usando una interfaz llamada CategoriaService, 
// esta clase debería llamarse CategoriaServiceImpl para seguir las convenciones de Spring.
@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Obtener todas las categorías
    // CORREGIDO: Cambiado de obtenerTodas() a obtenerTodos() para coincidir con ViewController
    public List<Categoria> obtenerTodos() {
        return categoriaRepository.findAll();
    }

    // Obtener categoría por ID
    public Optional<Categoria> obtenerPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    // Obtener categoría por nombre
    public Optional<Categoria> obtenerPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }

    // Verificar si existe categoría por nombre
    public boolean existePorNombre(String nombre) {
        return categoriaRepository.existsByNombre(nombre);
    }

    // Crear categoría (solo admin)
    public Categoria crear(Categoria categoria) {
        // Validar que no exista una categoría con el mismo nombre
        if (categoriaRepository.existsByNombre(categoria.getNombre())) {
            throw new RuntimeException("Ya existe una categoría con ese nombre");
        }
        
        return categoriaRepository.save(categoria);
    }

    // Actualizar categoría
    public Categoria actualizar(Long id, Categoria categoriaActualizada) {
        Categoria categoria = categoriaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        
        // Verificar que no exista otra categoría con el nuevo nombre
        if (!categoria.getNombre().equals(categoriaActualizada.getNombre()) &&
            categoriaRepository.existsByNombre(categoriaActualizada.getNombre())) {
            throw new RuntimeException("Ya existe una categoría con ese nombre");
        }
        
        categoria.setNombre(categoriaActualizada.getNombre());
        categoria.setDescripcion(categoriaActualizada.getDescripcion());
        categoria.setIcono(categoriaActualizada.getIcono());
        
        return categoriaRepository.save(categoria);
    }

    // Eliminar categoría
    public void eliminar(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        
        // Aquí podrías agregar validación para no eliminar si tiene eventos asociados
        categoriaRepository.deleteById(id);
    }
}
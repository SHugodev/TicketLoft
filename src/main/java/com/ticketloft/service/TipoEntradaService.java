package com.ticketloft.service;

import com.ticketloft.model.TipoEntrada;
import com.ticketloft.model.Evento;
import com.ticketloft.repository.TipoEntradaRepository;
import com.ticketloft.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoEntradaService {

    @Autowired
    private TipoEntradaRepository tipoEntradaRepository;
    
    @Autowired
    private EventoRepository eventoRepository;

    // --- C: CREAR TipoEntrada ---
    public TipoEntrada crear(TipoEntrada tipoEntrada, Long eventoId) {
        // 1. Verificar que el Evento exista y esté activo
        Evento evento = eventoRepository.findById(eventoId)
            .orElseThrow(() -> new RuntimeException("Evento no encontrado para asignar el tipo de entrada"));
            
        tipoEntrada.setEvento(evento);
        tipoEntrada.setActivo(true);
        
        // 2. Se podría añadir validación de unicidad de nombre de tipo por evento
        
        return tipoEntradaRepository.save(tipoEntrada);
    }

    // --- R: LEER por Evento ---
    public List<TipoEntrada> obtenerPorEvento(Long eventoId) {
        Evento evento = eventoRepository.findById(eventoId)
            .orElseThrow(() -> new RuntimeException("Evento no encontrado"));
            
        return tipoEntradaRepository.findByEvento(evento);
    }

    // --- R: LEER por ID ---
    public Optional<TipoEntrada> obtenerPorId(Long id) {
        return tipoEntradaRepository.findById(id);
    }

    // --- U: ACTUALIZAR TipoEntrada ---
    public TipoEntrada actualizar(Long id, TipoEntrada tipoEntradaActualizada) {
        TipoEntrada tipoEntrada = tipoEntradaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tipo de entrada no encontrado"));
            
        // Actualizar campos
        tipoEntrada.setNombre(tipoEntradaActualizada.getNombre());
        tipoEntrada.setDescripcion(tipoEntradaActualizada.getDescripcion());
        tipoEntrada.setPrecio(tipoEntradaActualizada.getPrecio());
        
        // Permitir solo aumentar la cantidad disponible
        if (tipoEntradaActualizada.getCantidadDisponible() < tipoEntrada.getCantidadDisponible()) {
            // Nota: La reducción de inventario se hace en el ReservaService. 
            // Si quieres reducir manualmente, se necesita una lógica de seguridad adicional.
            throw new RuntimeException("La cantidad disponible solo puede ser aumentada o igualada, no disminuida directamente.");
        }
        tipoEntrada.setCantidadDisponible(tipoEntradaActualizada.getCantidadDisponible());
        
        tipoEntrada.setActivo(tipoEntradaActualizada.getActivo());

        return tipoEntradaRepository.save(tipoEntrada);
    }

    // --- D: ELIMINAR (Soft Delete) ---
    public void desactivar(Long id) {
        TipoEntrada tipoEntrada = tipoEntradaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tipo de entrada no encontrado"));
        
        tipoEntrada.setActivo(false);
        tipoEntradaRepository.save(tipoEntrada);
    }
}
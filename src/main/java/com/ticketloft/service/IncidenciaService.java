package com.ticketloft.service;

import com.ticketloft.model.Incidencia;
import com.ticketloft.model.Usuario;
import com.ticketloft.repository.IncidenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IncidenciaService {

    @Autowired
    private IncidenciaRepository incidenciaRepository;

    @Transactional
    public Incidencia crearIncidencia(Usuario usuario, String asunto, String descripcion) {
        Incidencia incidencia = new Incidencia(asunto, descripcion, usuario);
        return incidenciaRepository.save(incidencia);
    }

    public List<Incidencia> obtenerTodas() {
        return incidenciaRepository.findAll();
    }

    public java.util.Optional<Incidencia> obtenerPorId(Long id) {
        return incidenciaRepository.findById(id);
    }

    public List<Incidencia> obtenerPorUsuario(Usuario usuario) {
        return incidenciaRepository.findByUsuario(usuario);
    }

    public List<Incidencia> obtenerPendientes() {
        return incidenciaRepository.findByEstado(Incidencia.EstadoIncidencia.PENDIENTE);
    }

    @Transactional
    public void resolverIncidencia(Long id) {
        Incidencia incidencia = incidenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incidencia no encontrada"));
        incidencia.setEstado(Incidencia.EstadoIncidencia.RESUELTA);
        incidenciaRepository.save(incidencia);
    }
}

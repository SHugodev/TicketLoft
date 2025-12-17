package com.ticketloft.repository;

import com.ticketloft.model.Evento;
import com.ticketloft.model.Categoria;
import com.ticketloft.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    
    // Buscar eventos activos
    List<Evento> findByActivoTrue();
    
    // Buscar eventos por categoría
    List<Evento> findByCategoria(Categoria categoria);
    
    // Buscar eventos por ciudad
    List<Evento> findByCiudad(String ciudad);
    
    // Buscar eventos futuros (que aún no han pasado)
    List<Evento> findByFechaAfterAndActivoTrue(LocalDateTime fecha);
    
    // Buscar eventos por rango de fechas
    List<Evento> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
    
    // Buscar eventos con plazas disponibles (query personalizada)
    @Query("SELECT e FROM Evento e WHERE e.activo = true AND e.fecha > :ahora " +
           "AND (SELECT COUNT(r) FROM Reserva r WHERE r.evento = e AND r.estado = 'CONFIRMADA') < e.aforo")
    List<Evento> findEventosDisponibles(@Param("ahora") LocalDateTime ahora);
    
    // Buscar eventos por creador
    List<Evento> findByCreadoPor(Usuario usuario);
    
    // Buscar eventos por estado de aprobación
    List<Evento> findByEstadoAprobacion(Evento.EstadoAprobacion estadoAprobacion);
    
    // Buscar eventos aprobados y futuros
    @Query("SELECT e FROM Evento e WHERE e.estadoAprobacion = 'APROBADO' AND e.fecha > :fecha AND e.activo = true")
    List<Evento> findEventosAprobadosYFuturos(@Param("fecha") LocalDateTime fecha);
}
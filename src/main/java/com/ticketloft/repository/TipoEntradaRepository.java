package com.ticketloft.repository;

import com.ticketloft.model.TipoEntrada;
import com.ticketloft.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoEntradaRepository extends JpaRepository<TipoEntrada, Long> {
    
    // Buscar tipos de entrada por evento
    List<TipoEntrada> findByEvento(Evento evento);
    
    // Buscar tipos de entrada activos por evento
    List<TipoEntrada> findByEventoAndActivoTrue(Evento evento);
}

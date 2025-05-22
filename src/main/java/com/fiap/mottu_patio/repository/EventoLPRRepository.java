package com.fiap.mottu_patio.repository;

import com.fiap.mottu_patio.model.EventoLPR;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EventoLPRRepository extends JpaRepository<EventoLPR, Long>, JpaSpecificationExecutor<EventoLPR> {
    Optional<EventoLPR> findTopByPlacaOrderByDataHoraDesc(String placa);

}
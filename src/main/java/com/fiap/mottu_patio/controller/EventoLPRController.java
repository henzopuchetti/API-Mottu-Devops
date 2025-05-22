package com.fiap.mottu_patio.controller;

import com.fiap.mottu_patio.dto.EventoLPRRequestDTO;
import com.fiap.mottu_patio.dto.EventoLPRResponseDTO;
import com.fiap.mottu_patio.service.EventoLPRService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/eventosLPR")
public class EventoLPRController {

    @Autowired
    private EventoLPRService eventoLPRService;

    // Criar um novo EventoLPR
@PostMapping
public ResponseEntity<EventoLPRResponseDTO> criarEvento(@RequestBody @Valid EventoLPRRequestDTO dto) {
    EventoLPRResponseDTO response = eventoLPRService.createEvento(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
}

    // Atualizar um EventoLPR
    @PutMapping("/{id}")
    public EventoLPRResponseDTO updateEvento(@PathVariable Long id, @RequestBody EventoLPRRequestDTO eventoLPRRequestDTO) {
        return eventoLPRService.updateEvento(id, eventoLPRRequestDTO);
    }

    // Buscar todos os eventos com filtros e paginação (sem 'page' e 'size' no método)
    @GetMapping
    public Page<EventoLPRResponseDTO> getEventos(
            @RequestParam(value = "tipoEvento", required = false) String tipoEvento,
            @RequestParam(value = "placa", required = false) String placa,
            Pageable pageable) { // Recebe o Pageable automaticamente
        return eventoLPRService.getEventosWithFilters(tipoEvento, placa, pageable);
    }

    // Buscar um evento LPR por ID
    @GetMapping("/{id}")
    public EventoLPRResponseDTO getEventoById(@PathVariable Long id) {
        return eventoLPRService.getEventoById(id);
    }

    // Deletar um evento LPR por ID
    @DeleteMapping("/{id}")
    public void deleteEvento(@PathVariable Long id) {
        eventoLPRService.deleteEvento(id);
    }
}
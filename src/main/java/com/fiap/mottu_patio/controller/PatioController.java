package com.fiap.mottu_patio.controller;

import com.fiap.mottu_patio.dto.PatioRequestDTO;
import com.fiap.mottu_patio.dto.PatioResponseDTO;
import com.fiap.mottu_patio.service.PatioService;
import com.fiap.mottu_patio.mapper.PatioMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/patios")
public class PatioController {

    private final PatioService patioService;

    @Autowired
    public PatioController(PatioService patioService) {
        this.patioService = patioService;
    }

    // Criar Pátio
    @PostMapping
    public ResponseEntity<PatioResponseDTO> criarPatio(@Valid @RequestBody PatioRequestDTO patioRequestDTO) {
        var patio = PatioMapper.INSTANCE.toEntity(patioRequestDTO); 
        var patioCriado = patioService.criarPatio(patio);
        var patioResponse = PatioMapper.INSTANCE.toResponse(patioCriado); 
        return new ResponseEntity<>(patioResponse, HttpStatus.CREATED);
    }

    // Buscar todos os Pátios
    @GetMapping
    public ResponseEntity<List<PatioResponseDTO>> buscarTodosPatios() {
        List<PatioResponseDTO> patiosResponse = patioService.buscarTodosPatios(); // Alteração: utilizar o método que retorna DTOs
        return new ResponseEntity<>(patiosResponse, HttpStatus.OK);
    }

    // Buscar Pátio por ID
    @GetMapping("/{id}")
    public ResponseEntity<PatioResponseDTO> buscarPatioPorId(@PathVariable Long id) {
        var patio = patioService.buscarPatioPorId(id);
        var patioResponse = PatioMapper.INSTANCE.toResponse(patio); 
        return new ResponseEntity<>(patioResponse, HttpStatus.OK);
    }

    // Atualizar Pátio
    @PutMapping("/{id}")
    public ResponseEntity<PatioResponseDTO> atualizarPatio(
            @PathVariable Long id,
            @Valid @RequestBody PatioRequestDTO patioRequestDTO) {
        var patio = PatioMapper.INSTANCE.toEntity(patioRequestDTO); 
        var patioAtualizado = patioService.atualizarPatio(id, patio);
        var patioResponse = PatioMapper.INSTANCE.toResponse(patioAtualizado); 
        return new ResponseEntity<>(patioResponse, HttpStatus.OK);
    }

    // Excluir Pátio
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPatio(@PathVariable Long id) {
        patioService.excluirPatio(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
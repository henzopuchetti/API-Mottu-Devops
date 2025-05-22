package com.fiap.mottu_patio.controller;

import com.fiap.mottu_patio.dto.MotoRequestDTO;
import com.fiap.mottu_patio.dto.MotoResponseDTO;
import com.fiap.mottu_patio.service.MotoService;
import com.fiap.mottu_patio.mapper.MotoMapper;
import com.fiap.mottu_patio.repository.EventoLPRRepository;
import com.fiap.mottu_patio.model.Moto;
import com.fiap.mottu_patio.repository.PatioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/motos")
public class MotoController {

    private final MotoService motoService;
    private final MotoMapper motoMapper;
    private final PatioRepository patioRepository;
    private final EventoLPRRepository eventoLPRRepository;

    @Autowired
    public MotoController(MotoService motoService, MotoMapper motoMapper,
                          PatioRepository patioRepository,
                          EventoLPRRepository eventoLPRRepository) {
        this.motoService = motoService;
        this.motoMapper = motoMapper;
        this.patioRepository = patioRepository;
        this.eventoLPRRepository = eventoLPRRepository;
    }

    @PostMapping
    public ResponseEntity<MotoResponseDTO> create(@Valid @RequestBody MotoRequestDTO dto) {
        Moto moto = motoMapper.toEntity(dto, patioRepository);
        Moto saved = motoService.criarMoto(moto);
        return ResponseEntity.status(HttpStatus.CREATED).body(motoMapper.toResponseWithVagaAtual(saved, eventoLPRRepository));
    }

    @GetMapping
    public ResponseEntity<List<MotoResponseDTO>> buscarTodasMotos() {
        var motos = motoService.buscarTodasMotos();
        var motosResponse = motoMapper.toResponseList(motos, eventoLPRRepository);
        return new ResponseEntity<>(motosResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MotoResponseDTO> buscarMotoPorId(@PathVariable Long id) {
        var moto = motoService.buscarMotoPorId(id);
        var motoResponse = motoMapper.toResponseWithVagaAtual(moto, eventoLPRRepository);
        return new ResponseEntity<>(motoResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MotoResponseDTO> atualizarMoto(
            @PathVariable Long id,
            @Valid @RequestBody MotoRequestDTO motoRequestDTO) {
        var moto = motoMapper.toEntity(motoRequestDTO, patioRepository);
        var motoAtualizada = motoService.atualizarMoto(id, moto);
        var motoResponse = motoMapper.toResponseWithVagaAtual(motoAtualizada, eventoLPRRepository);
        return new ResponseEntity<>(motoResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirMoto(@PathVariable Long id) {
        motoService.excluirMoto(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
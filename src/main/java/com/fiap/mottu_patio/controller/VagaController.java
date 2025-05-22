package com.fiap.mottu_patio.controller;

import com.fiap.mottu_patio.dto.VagasPorPatioFormatadoDTO;
import com.fiap.mottu_patio.service.VagaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vagas")
public class VagaController {

    private final VagaService vagaService;

    @Autowired
    public VagaController(VagaService vagaService) {
        this.vagaService = vagaService;
    }

    @GetMapping("/patio/{id}/formatado")
    public ResponseEntity<VagasPorPatioFormatadoDTO> listarVagasFormatadasPorPatio(@PathVariable Long id) {
        VagasPorPatioFormatadoDTO response = vagaService.listarVagasFormatadasPorPatioId(id);
        return ResponseEntity.ok(response);
    }
}
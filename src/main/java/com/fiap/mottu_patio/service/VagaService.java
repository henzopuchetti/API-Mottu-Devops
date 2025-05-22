package com.fiap.mottu_patio.service;

import com.fiap.mottu_patio.dto.VagasPorPatioFormatadoDTO;
import com.fiap.mottu_patio.exception.ResourceNotFoundException;
import com.fiap.mottu_patio.model.Patio;
import com.fiap.mottu_patio.model.Vaga;
import com.fiap.mottu_patio.repository.PatioRepository;
import com.fiap.mottu_patio.repository.VagaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class VagaService {

    private final VagaRepository vagaRepository;
    private final PatioRepository patioRepository;

    @Autowired
    public VagaService(VagaRepository vagaRepository, PatioRepository patioRepository) {
        this.vagaRepository = vagaRepository;
        this.patioRepository = patioRepository;
    }

    public VagasPorPatioFormatadoDTO listarVagasFormatadasPorPatioId(Long patioId) {
        Patio patio = patioRepository.findById(patioId)
                .orElseThrow(() -> new ResourceNotFoundException("Pátio com ID " + patioId + " não encontrado."));

        List<Vaga> vagas = vagaRepository.findByPatioId(patioId);

        Map<String, List<String>> vagasPorFileira = vagas.stream()
                .collect(Collectors.groupingBy(
                        vaga -> vaga.getIdentificador().split(":")[0], // pega a letra da fileira
                        TreeMap::new, // ordena por chave (fileira)
                        Collectors.mapping(
                                vaga -> vaga.getIdentificador() + " - " + (vaga.isOcupada() ? "ocupada" : "livre"),
                                Collectors.toList()
                        )
                ));

        return new VagasPorPatioFormatadoDTO(patio.getNome(), vagasPorFileira);
    }

    // Outros métodos como salvar, ocupar/desocupar etc. virão depois
}
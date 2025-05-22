package com.fiap.mottu_patio.service;

import com.fiap.mottu_patio.dto.PatioResponseDTO;
import com.fiap.mottu_patio.exception.ResourceNotFoundException;
import com.fiap.mottu_patio.model.Patio;
import com.fiap.mottu_patio.model.Vaga;
import com.fiap.mottu_patio.repository.PatioRepository;
import com.fiap.mottu_patio.repository.VagaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatioService {

    private final PatioRepository patioRepository;
    private final VagaRepository vagaRepository;

    @Autowired
    public PatioService(PatioRepository patioRepository, VagaRepository vagaRepository) {
        this.patioRepository = patioRepository;
        this.vagaRepository = vagaRepository;
    }

    // Criar Pátio com geração automática de vagas
    public Patio criarPatio(Patio patio) {
        if (patio.getCapacidade() > 260) {
            throw new IllegalArgumentException("O número máximo de vagas permitido é 260.");
        }

        // Inicializa vagasDisponiveis com a capacidade total
        patio.setVagasDisponiveis(patio.getCapacidade());

        Patio savedPatio = patioRepository.save(patio);

        int capacidade = patio.getCapacidade();
        char fileira = 'A';
        int numeroVaga = 1;

        for (int i = 0; i < capacidade; i++) {
            String identificador = fileira + ":" + numeroVaga;

            Vaga vaga = Vaga.builder()
                    .identificador(identificador)
                    .codigo(identificador)
                    .ocupada(false)
                    .patio(savedPatio)
                    .build();

            vagaRepository.save(vaga);

            numeroVaga++;
            if (numeroVaga > 10) {
                numeroVaga = 1;
                fileira++;
            }
        }

        return savedPatio;
    }

    // Buscar pátios com filtros dinâmicos
    public List<PatioResponseDTO> buscarPatiosPorFiltro(Specification<Patio> spec, int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
        Page<Patio> patioPage = patioRepository.findAll(spec, pageable);

        return patioPage.getContent().stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    // Buscar todos os Pátios
    public List<PatioResponseDTO> buscarTodosPatios() {
        List<Patio> patios = patioRepository.findAll();
        return patios.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    // Buscar por ID
    public Patio buscarPatioPorId(Long id) {
        return patioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pátio não encontrado com id: " + id));
    }

// Atualizar Pátio (inclusive capacidade e gerar novas vagas)
public Patio atualizarPatio(Long id, Patio patioAtualizado) {
    Patio patioExistente = buscarPatioPorId(id);

    patioExistente.setNome(patioAtualizado.getNome());
    patioExistente.setEndereco(patioAtualizado.getEndereco());

    int capacidadeAntiga = patioExistente.getCapacidade();
    int novaCapacidade = patioAtualizado.getCapacidade();

    // Atualiza capacidade
    patioExistente.setCapacidade(novaCapacidade);

    // Atualiza vagasDisponiveis com base na nova capacidade
    int vagasOcupadas = vagaRepository.countByPatioAndOcupadaTrue(patioExistente);
    patioExistente.setVagasDisponiveis(novaCapacidade - vagasOcupadas);

    Patio patioSalvo = patioRepository.save(patioExistente);

    // Se a nova capacidade for maior, gera vagas adicionais
    if (novaCapacidade > capacidadeAntiga) {
        List<Vaga> vagasExistentes = vagaRepository.findByPatio(patioSalvo);
        int totalVagas = vagasExistentes.size();

        char fileira = 'A';
        int numeroVaga = 1;

        // Calcula última vaga criada para continuar a sequência
        if (!vagasExistentes.isEmpty()) {
            Vaga ultima = vagasExistentes.get(vagasExistentes.size() - 1);
            String[] partes = ultima.getIdentificador().split(":");
            fileira = partes[0].charAt(0);
            numeroVaga = Integer.parseInt(partes[1]) + 1;
            if (numeroVaga > 10) {
                numeroVaga = 1;
                fileira++;
            }
        }

        for (int i = totalVagas; i < novaCapacidade; i++) {
            String identificador = fileira + ":" + numeroVaga;

            Vaga novaVaga = Vaga.builder()
                    .identificador(identificador)
                    .codigo(identificador)
                    .ocupada(false)
                    .patio(patioSalvo)
                    .build();

            vagaRepository.save(novaVaga);

            numeroVaga++;
            if (numeroVaga > 10) {
                numeroVaga = 1;
                fileira++;
            }
        }
    }

    return patioSalvo;
}


    // Excluir Pátio
    public void excluirPatio(Long id) {
        Patio patio = buscarPatioPorId(id);
        patioRepository.delete(patio);
    }

    // Converte Patio para DTO e calcula vagas disponíveis
    private PatioResponseDTO mapToResponseDTO(Patio patio) {
        PatioResponseDTO responseDTO = new PatioResponseDTO();
        responseDTO.setId(patio.getId());
        responseDTO.setNome(patio.getNome());
        responseDTO.setEndereco(patio.getEndereco());
        responseDTO.setCapacidade(patio.getCapacidade());

        // Calcula dinamicamente vagas disponíveis
        int vagasOcupadas = vagaRepository.countByPatioAndOcupadaTrue(patio);
        int vagasDisponiveis = patio.getCapacidade() - vagasOcupadas;
        responseDTO.setVagasDisponiveis(vagasDisponiveis);

        return responseDTO;
    }
}
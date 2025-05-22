package com.fiap.mottu_patio.mapper;

import com.fiap.mottu_patio.dto.MotoRequestDTO;
import com.fiap.mottu_patio.dto.MotoResponseDTO;
import com.fiap.mottu_patio.model.Moto;
import com.fiap.mottu_patio.model.Patio;
import com.fiap.mottu_patio.repository.PatioRepository;
import com.fiap.mottu_patio.repository.EventoLPRRepository;

import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class MotoMapper {

    // Mapeia o DTO para entidade, com injeção de Patio via repository
    @Mapping(target = "patio", expression = "java(mapPatio(request.getPatioId(), patioRepository))")
    public abstract Moto toEntity(MotoRequestDTO request, @Context PatioRepository patioRepository);

    // Mapeia entidade Moto para DTO de resposta
    @Mapping(source = "patio.id", target = "idPatio")
    @Mapping(source = "patio.nome", target = "nomePatio")
    @Mapping(target = "vagaAtual", ignore = true) // será setado manualmente
    public abstract MotoResponseDTO toResponse(Moto moto);

    // Lista de Moto → Lista de MotoResponseDTO com vaga atual
    public List<MotoResponseDTO> toResponseList(List<Moto> motos, @Context EventoLPRRepository eventoLPRRepository) {
        return motos.stream().map(moto -> toResponseWithVagaAtual(moto, eventoLPRRepository)).toList();
    }

    // Mapeamento individual com lógica para vaga atual baseada na placa
    public MotoResponseDTO toResponseWithVagaAtual(Moto moto, @Context EventoLPRRepository eventoLPRRepository) {
        MotoResponseDTO dto = toResponse(moto);

        // Checa se a moto ainda está associada a uma vaga
        if (moto.getVaga() != null) {
            dto.setVagaAtual(moto.getVaga().getCodigo()); // ou getIdentificador()
        } else {
            dto.setVagaAtual(null);
        }

        return dto;
    }

    // Busca o pátio no banco para preencher o campo
    protected Patio mapPatio(Long patioId, @Context PatioRepository patioRepository) {
        return patioRepository.findById(patioId)
                .orElseThrow(() -> new RuntimeException("Pátio não encontrado com id: " + patioId));
    }
}
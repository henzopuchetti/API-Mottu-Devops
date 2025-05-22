package com.fiap.mottu_patio.mapper;

import com.fiap.mottu_patio.dto.EventoLPRRequestDTO;
import com.fiap.mottu_patio.dto.EventoLPRResponseDTO;
import com.fiap.mottu_patio.model.EventoLPR;
import com.fiap.mottu_patio.model.Patio;
import com.fiap.mottu_patio.model.Vaga;
import com.fiap.mottu_patio.model.enums.TipoEvento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventoLPRMapper {

    // --- MAPEAMENTO DE REQUEST (DTO) PARA ENTITY ---
    @Mapping(source = "tipoEvento", target = "tipoEvento", qualifiedByName = "stringToTipoEvento")
    @Mapping(source = "vaga", target = "vaga", qualifiedByName = "stringToVaga")
    EventoLPR toEntity(EventoLPRRequestDTO dto);
    
    // --- MAPEAMENTO DE ENTITY PARA RESPONSE (DTO) ---
    @Mapping(source = "tipoEvento", target = "tipoEvento", qualifiedByName = "tipoEventoToString")
    @Mapping(source = "vaga", target = "vaga", qualifiedByName = "vagaToCodigo")
    EventoLPRResponseDTO toResponse(EventoLPR entity);

    List<EventoLPRResponseDTO> toResponseList(List<EventoLPR> entities);

    // --- Enum mappings ---
    @Named("stringToTipoEvento")
    default TipoEvento stringToTipoEvento(String value) {
        if (value == null || value.isBlank()) return null;
        return TipoEvento.valueOf(value.toUpperCase());
    }

    @Named("tipoEventoToString")
    default String tipoEventoToString(TipoEvento tipoEvento) {
        return tipoEvento != null ? tipoEvento.name() : null;
    }

    // --- Patio mappings ---
    // No mapeamento do request, cria um Patio só com ID para relacionar
    @Named("stringToPatio")
    default Patio stringToPatio(String idStr) {
        if (idStr == null || idStr.isBlank()) return null;
        try {
            Long id = Long.parseLong(idStr);
            Patio patio = new Patio();
            patio.setId(id);
            return patio;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // No mapeamento para response, converte o Patio para o nome (String)
    @Named("patioToNome")
    default String patioToNome(Patio patio) {
        return patio != null ? patio.getNome() : null;
    }

    // --- Vaga mappings ---
    // Similar ao Patio, cria uma Vaga só com ID para relacionar no request
    @Named("stringToVaga")
    default Vaga stringToVaga(String idStr) {
        if (idStr == null || idStr.isBlank()) return null;
        try {
            Long id = Long.parseLong(idStr);
            Vaga vaga = new Vaga();
            vaga.setId(id);
            return vaga;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // Converte a Vaga para o código (String) na response
    @Named("vagaToCodigo")
    default String vagaToCodigo(Vaga vaga) {
        return vaga != null ? vaga.getCodigo() : null;
    }
}
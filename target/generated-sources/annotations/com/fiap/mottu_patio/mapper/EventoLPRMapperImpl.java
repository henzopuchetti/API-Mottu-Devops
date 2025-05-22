package com.fiap.mottu_patio.mapper;

import com.fiap.mottu_patio.dto.EventoLPRRequestDTO;
import com.fiap.mottu_patio.dto.EventoLPRResponseDTO;
import com.fiap.mottu_patio.model.EventoLPR;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-22T09:06:07-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class EventoLPRMapperImpl implements EventoLPRMapper {

    @Override
    public EventoLPR toEntity(EventoLPRRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        EventoLPR eventoLPR = new EventoLPR();

        eventoLPR.setTipoEvento( stringToTipoEvento( dto.getTipoEvento() ) );
        eventoLPR.setVaga( stringToVaga( dto.getVaga() ) );
        eventoLPR.setPlaca( dto.getPlaca() );

        return eventoLPR;
    }

    @Override
    public EventoLPRResponseDTO toResponse(EventoLPR entity) {
        if ( entity == null ) {
            return null;
        }

        EventoLPRResponseDTO eventoLPRResponseDTO = new EventoLPRResponseDTO();

        eventoLPRResponseDTO.setTipoEvento( tipoEventoToString( entity.getTipoEvento() ) );
        eventoLPRResponseDTO.setVaga( vagaToCodigo( entity.getVaga() ) );
        eventoLPRResponseDTO.setDataHora( entity.getDataHora() );
        eventoLPRResponseDTO.setPlaca( entity.getPlaca() );

        return eventoLPRResponseDTO;
    }

    @Override
    public List<EventoLPRResponseDTO> toResponseList(List<EventoLPR> entities) {
        if ( entities == null ) {
            return null;
        }

        List<EventoLPRResponseDTO> list = new ArrayList<EventoLPRResponseDTO>( entities.size() );
        for ( EventoLPR eventoLPR : entities ) {
            list.add( toResponse( eventoLPR ) );
        }

        return list;
    }
}

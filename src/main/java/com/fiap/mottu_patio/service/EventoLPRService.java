package com.fiap.mottu_patio.service;

import com.fiap.mottu_patio.dto.EventoLPRRequestDTO;
import com.fiap.mottu_patio.dto.EventoLPRResponseDTO;
import com.fiap.mottu_patio.mapper.EventoLPRMapper;
import com.fiap.mottu_patio.model.EventoLPR;
import com.fiap.mottu_patio.model.Moto;
import com.fiap.mottu_patio.model.Patio;
import com.fiap.mottu_patio.model.Vaga;
import com.fiap.mottu_patio.model.enums.TipoEvento;
import com.fiap.mottu_patio.repository.EventoLPRRepository;
import com.fiap.mottu_patio.repository.MotoRepository;
import com.fiap.mottu_patio.repository.PatioRepository;
import com.fiap.mottu_patio.repository.VagaRepository;
import com.fiap.mottu_patio.specification.EventoLPRSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EventoLPRService {

    @Autowired
    private EventoLPRRepository eventoLPRRepository;

    @Autowired
    private EventoLPRMapper eventoLPRMapper;

    @Autowired
    private MotoRepository motoRepository;

    @Autowired
    private PatioRepository patioRepository;

    @Autowired
    private VagaRepository vagaRepository;

    public EventoLPRResponseDTO createEvento(EventoLPRRequestDTO dto) {
        TipoEvento tipoEvento = TipoEvento.valueOf(dto.getTipoEvento().toUpperCase());
        String placa = dto.getPlaca();

        Moto moto = motoRepository.findByPlaca(placa)
                .orElseThrow(() -> new RuntimeException("Moto com placa " + placa + " não encontrada"));

        Patio patio = patioRepository.findById(moto.getPatio().getId())
                .orElseThrow(() -> new RuntimeException("Pátio vinculado à moto não encontrado"));

        EventoLPR evento = new EventoLPR();
        evento.setTipoEvento(tipoEvento);
        evento.setPlaca(placa);

        if (tipoEvento == TipoEvento.ENTRADA) {
            String codigoVaga = dto.getVaga();
            if (codigoVaga == null || codigoVaga.isBlank()) {
                throw new RuntimeException("Código da vaga é obrigatório para entrada");
            }

            Vaga vaga = vagaRepository.findByCodigoAndPatio(codigoVaga, patio)
                    .orElseThrow(() -> new RuntimeException("Vaga " + codigoVaga + " não encontrada no pátio " + patio.getNome()));

            if (vaga.isOcupada()) {
                throw new RuntimeException("Vaga " + codigoVaga + " já está ocupada");
            }

            // Ocupa a vaga e associa à moto
            vaga.setOcupada(true);
            moto.setVaga(vaga);
            vagaRepository.save(vaga);
            motoRepository.save(moto);
            evento.setVaga(vaga);

            // Atualiza o campo de vagas disponíveis
            patio.setVagasDisponiveis(patio.getVagasDisponiveis() - 1);
            patioRepository.save(patio);

        } else if (tipoEvento == TipoEvento.SAIDA) {
            Vaga vagaAtual = moto.getVaga();
            if (vagaAtual == null) {
                throw new RuntimeException("Moto com placa " + placa + " não está ocupando nenhuma vaga");
            }

            // Libera a vaga e desassocia da moto
            vagaAtual.setOcupada(false);
            moto.setVaga(null);
            vagaRepository.save(vagaAtual);
            motoRepository.save(moto);
            evento.setVaga(vagaAtual);

            // Atualiza o campo de vagas disponíveis
            patio.setVagasDisponiveis(patio.getVagasDisponiveis() + 1);
            patioRepository.save(patio);
        }

        eventoLPRRepository.save(evento);
        return eventoLPRMapper.toResponse(evento);
    }

    public EventoLPRResponseDTO updateEvento(Long id, EventoLPRRequestDTO dto) {
        return eventoLPRRepository.findById(id).map(evento -> {
            if (dto.getTipoEvento() != null) {
                TipoEvento tipoEvento = TipoEvento.valueOf(dto.getTipoEvento().toUpperCase());
                evento.setTipoEvento(tipoEvento);
            }
            if (dto.getPlaca() != null) {
                evento.setPlaca(dto.getPlaca());
            }
            return eventoLPRMapper.toResponse(eventoLPRRepository.save(evento));
        }).orElse(null);
    }

    public Page<EventoLPRResponseDTO> getEventosWithFilters(String tipoEvento, String placa, String dataInicio, String dataFim, Pageable pageable) {
        return eventoLPRRepository.findAll(
                EventoLPRSpecification.hasTipoEvento(tipoEvento)
                        .and(EventoLPRSpecification.hasPlaca(placa))
                        .and(EventoLPRSpecification.hasDataHoraBetween(dataInicio, dataFim)),
                pageable
        ).map(eventoLPRMapper::toResponse);
    }

    public Page<EventoLPRResponseDTO> getEventosWithFilters(String tipoEvento, String placa, Pageable pageable) {
        return getEventosWithFilters(tipoEvento, placa, null, null, pageable);
    }

    public EventoLPRResponseDTO getEventoById(Long id) {
        return eventoLPRRepository.findById(id).map(eventoLPRMapper::toResponse).orElse(null);
    }

    public void deleteEvento(Long id) {
        if (eventoLPRRepository.existsById(id)) {
            eventoLPRRepository.deleteById(id);
        }
    }
}
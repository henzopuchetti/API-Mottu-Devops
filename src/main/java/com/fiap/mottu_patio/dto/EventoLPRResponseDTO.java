package com.fiap.mottu_patio.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EventoLPRResponseDTO {
    private String tipoEvento;  // String também para enviar para o cliente
    private String placa;
    private String vaga;
    private LocalDateTime dataHora;
}
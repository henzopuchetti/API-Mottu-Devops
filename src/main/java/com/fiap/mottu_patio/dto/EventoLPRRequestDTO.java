package com.fiap.mottu_patio.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EventoLPRRequestDTO {

    @NotBlank
    private String tipoEvento;  // ENTRADA ou SAIDA

    @NotBlank
    private String placa;

    private String vaga;  // obrigatória para ENTRADA
}
package com.fiap.mottu_patio.specification;

import com.fiap.mottu_patio.model.EventoLPR;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventoLPRSpecification {

    public static Specification<EventoLPR> hasTipoEvento(String tipoEvento) {
        return (root, query, criteriaBuilder) -> {
            if (tipoEvento == null || tipoEvento.isEmpty()) return null;
            return criteriaBuilder.equal(root.get("tipoEvento"), tipoEvento);
        };
    }

    public static Specification<EventoLPR> hasPlaca(String placa) {
        return (root, query, criteriaBuilder) -> {
            if (placa == null || placa.isEmpty()) return null;
            return criteriaBuilder.like(root.get("placa"), "%" + placa + "%");
        };
    }

    // Filtro por data de evento, sem hora
    public static Specification<EventoLPR> hasDataHoraBetween(String dataInicio, String dataFim) {
        return (root, query, criteriaBuilder) -> {
            if (dataInicio == null || dataFim == null) return null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(dataInicio, formatter);
            LocalDate endDate = LocalDate.parse(dataFim, formatter);

            LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

            return criteriaBuilder.between(root.get("dataHora"), startDateTime, endDateTime);
        };
    }
}
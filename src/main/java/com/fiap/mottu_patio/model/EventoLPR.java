package com.fiap.mottu_patio.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.fiap.mottu_patio.model.enums.TipoEvento;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoLPR {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoEvento tipoEvento; // ENTRADA ou SAIDA

    @Column(nullable = false)
    private String placa;

    @ManyToOne
    @JoinColumn(name = "vaga_id")
    private Vaga vaga;

    private LocalDateTime dataHora;


    @PrePersist
    public void prePersist() {
        dataHora = LocalDateTime.now();
    }
}
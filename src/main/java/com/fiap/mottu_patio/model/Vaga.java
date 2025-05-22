package com.fiap.mottu_patio.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vaga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String identificador; // Exemplo: "A:1"

    private boolean ocupada = false;

    @Column(nullable = false)
    private String codigo; // Código único da vaga (ex: A1, B2, etc.)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patio_id", nullable = false)
    private Patio patio;
}
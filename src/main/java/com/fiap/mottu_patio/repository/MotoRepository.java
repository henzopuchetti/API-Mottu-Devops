package com.fiap.mottu_patio.repository;

import com.fiap.mottu_patio.model.Moto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface MotoRepository extends JpaRepository<Moto, Long>, JpaSpecificationExecutor<Moto> {
    int countByPatioId(Long patioId);

    Optional<Moto> findByPlaca(String placa);
}
package com.fiap.mottu_patio.service;

import com.fiap.mottu_patio.exception.BusinessException;
import com.fiap.mottu_patio.exception.ResourceNotFoundException;
import com.fiap.mottu_patio.model.Moto;
import com.fiap.mottu_patio.model.Patio;
import com.fiap.mottu_patio.repository.MotoRepository;
import com.fiap.mottu_patio.specification.MotoSpecification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MotoService {

    private final MotoRepository motoRepository;

    @Autowired
    public MotoService(MotoRepository motoRepository) {
        this.motoRepository = motoRepository;
    }

    // CRUD: Criar Moto
    public Moto criarMoto(Moto moto) {
        Long patioId = moto.getPatio().getId();

        int quantidadeDeMotos = motoRepository.countByPatioId(patioId);
        int capacidade = moto.getPatio().getCapacidade();

        if (quantidadeDeMotos >= capacidade) {
            throw new BusinessException("O pátio está lotado");
        }

        return motoRepository.save(moto);
    }

    // CRUD: Buscar todas as Motos
    public List<Moto> buscarTodasMotos() {
        return motoRepository.findAll();
    }

    // CRUD: Buscar Moto por ID
    public Moto buscarMotoPorId(Long id) {
        return motoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Moto não encontrada com id: " + id));
    }

    // CRUD: Atualizar Moto
    public Moto atualizarMoto(Long id, Moto motoAtualizada) {
        Moto motoExistente = buscarMotoPorId(id);
        motoExistente.setPlaca(motoAtualizada.getPlaca());
        motoExistente.setModelo(motoAtualizada.getModelo());
        motoExistente.setCor(motoAtualizada.getCor());
        motoExistente.setAno(motoAtualizada.getAno());
        motoExistente.setPatio(motoAtualizada.getPatio());
        return motoRepository.save(motoExistente);
    }

    // CRUD: Excluir Moto
    public void excluirMoto(Long id) {
        Moto moto = buscarMotoPorId(id);
        motoRepository.delete(moto);
    }

    // Buscar Motos com filtros
    public List<Moto> buscarMotosPorFiltro(String modelo, Integer ano, Patio patio) {
        Specification<Moto> specification = Specification.where(MotoSpecification.hasModelo(modelo))
                                                        .and(MotoSpecification.hasAno(ano))
                                                        .and(MotoSpecification.hasPatio(patio));
        return motoRepository.findAll(specification);
    }
}
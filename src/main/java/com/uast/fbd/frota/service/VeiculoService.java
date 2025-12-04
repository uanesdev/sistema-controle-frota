package com.uast.fbd.frota.service;

import com.uast.fbd.frota.entity.Veiculo;
import com.uast.fbd.frota.exception.RecursoNaoEncontradoException;
import com.uast.fbd.frota.repository.VeiculoRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class VeiculoService {

    private final VeiculoRepository repository;

    public VeiculoService(VeiculoRepository repository) {
        this.repository = repository;
    }

    public Veiculo criarVeiculo(Veiculo veiculo) {
        return repository.criarVeiculo(veiculo);
    }

    public List<Veiculo> buscarVeiculos(String placa, String modelo, Integer ano,
                                        String status, Boolean ativo) {
        return repository.buscarVeiculos(placa, modelo, ano, status, ativo);
    }

    public Veiculo buscarVeiculoPorId(Long id) {
        try {
            return repository.buscarVeiculoPorId(id);
        } catch (Exception e) {
            throw new RecursoNaoEncontradoException("Veículo não encontrado com id: " + id);
        }
    }

    public Veiculo desativarVeiculo(Long id) {
        try {
            return repository.desativarVeiculo(id);
        } catch (EmptyResultDataAccessException e) {
            throw new RecursoNaoEncontradoException("Não foi possível desativar. Veículo com ID " + id + " não encontrado");
        }
    }

    public Veiculo atualizarVeiculo(Long id, Map<String, Object> updates) {
        try {
            return repository.atualizarVeiculo(id, updates);
        } catch (RecursoNaoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar veículo", e);
        }
    }


}

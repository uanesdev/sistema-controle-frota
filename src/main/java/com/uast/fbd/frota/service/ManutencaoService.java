package com.uast.fbd.frota.service;

import com.uast.fbd.frota.entity.Manutencao;
import com.uast.fbd.frota.entity.Veiculo;
import com.uast.fbd.frota.exception.ValidacaoException;
import com.uast.fbd.frota.repository.ManutencaoRepository;
import com.uast.fbd.frota.repository.VeiculoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class ManutencaoService {

    private final ManutencaoRepository manutencaoRepository;
    private final VeiculoRepository veiculoRepository;

    public ManutencaoService(
            ManutencaoRepository manutencaoRepository,
            VeiculoRepository veiculoRepository
    ) {
        this.manutencaoRepository = manutencaoRepository;
        this.veiculoRepository = veiculoRepository;
    }

    public Manutencao criar(Manutencao m) {

        if (m.getVeiculoId() == null) {
            throw new ValidacaoException("O campo veiculoId é obrigatório.");
        }

        Veiculo veiculo = veiculoRepository.buscarVeiculoPorId(m.getVeiculoId());
        if (!veiculo.isAtivo()) {
            throw new ValidacaoException("O veículo informado está desativado.");
        }

        if (m.getDescricao() == null || m.getDescricao().isBlank()) {
            throw new ValidacaoException("A descrição da manutenção é obrigatória.");
        }

        if (m.getCusto() == null || m.getCusto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidacaoException("O custo deve ser maior que zero.");
        }

        if (m.getData() == null) {
            throw new ValidacaoException("A data da manutenção é obrigatória.");
        }

        if (m.getData().isAfter(LocalDateTime.now())) {
            throw new ValidacaoException("A data da manutenção não pode estar no futuro.");
        }

        if (m.getAtivo() == null) {
            m.setAtivo(true);
        }

        return manutencaoRepository.criarManutencao(m);
    }

    public Manutencao buscarPorId(Long id) {
        return manutencaoRepository.buscarPorId(id);
    }

    public java.util.List<Manutencao> buscar(Long veiculoId, String descricao, Boolean ativo) {
        return manutencaoRepository.buscarManutencoes(veiculoId, descricao, ativo);
    }

    public Manutencao desativar(Long id) {
        return manutencaoRepository.desativarManutencao(id);
    }

    public Manutencao atualizar(Long id, Manutencao m) {

        Manutencao atual = manutencaoRepository.buscarPorId(id);

        Map<String, Object> updates = new HashMap<>();

        if (m.getDescricao() != null) {
            if (m.getDescricao().isBlank()) {
                throw new ValidacaoException("A descrição não pode ser vazia.");
            }
            updates.put("descricao", m.getDescricao());
        }

        if (m.getCusto() != null) {
            if (m.getCusto().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ValidacaoException("O custo deve ser maior que zero.");
            }
            updates.put("custo", m.getCusto());
        }

        if (m.getData() != null) {
            if (m.getData().isAfter(LocalDateTime.now())) {
                throw new ValidacaoException("A data da manutenção não pode estar no futuro.");
            }
            updates.put("data", m.getData());
        }

        if (m.getAtivo() != null) {
            updates.put("ativo", m.getAtivo());
        }

        if (m.getVeiculoId() != null) {
            Veiculo v = veiculoRepository.buscarVeiculoPorId(m.getVeiculoId());
            if (!v.isAtivo()) {
                throw new ValidacaoException("O veículo informado está desativado.");
            }
            updates.put("veiculo_id", m.getVeiculoId());
        }

        return manutencaoRepository.atualizarManutencao(id, updates);
    }
}

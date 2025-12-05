package com.uast.fbd.frota.service;

import com.uast.fbd.frota.entity.Abastecimento;
import com.uast.fbd.frota.entity.Veiculo;
import com.uast.fbd.frota.exception.ValidacaoException;
import com.uast.fbd.frota.repository.AbastecimentoRepository;
import com.uast.fbd.frota.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AbastecimentoService {

    private final AbastecimentoRepository repository;
    private final VeiculoRepository veiculoRepository;

    public Abastecimento criar(Abastecimento a) {

        Veiculo veiculo = veiculoRepository.buscarVeiculoPorId(a.getVeiculoId());
        if (!veiculo.isAtivo()) {
            throw new ValidacaoException("O veículo está desativado.");
        }

        if (a.getLitros() == null || a.getLitros().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidacaoException("A quantidade de litros deve ser maior que zero.");
        }

        if (a.getValorTotal() == null || a.getValorTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidacaoException("O valor total deve ser maior que zero.");
        }

        if (a.getData() == null) {
            throw new ValidacaoException("A data do abastecimento é obrigatória.");
        }

        if (a.getData().isAfter(LocalDateTime.now())) {
            throw new ValidacaoException("A data do abastecimento não pode estar no futuro.");
        }

        a.setAtivo(true);

        return repository.criarAbastecimento(a);
    }

    public Abastecimento buscarPorId(Long id) {
        return repository.buscarPorId(id);
    }

    public List<Abastecimento> buscar(Long veiculoId, LocalDateTime data, Boolean ativo) {
        return repository.buscarAbastecimentos(veiculoId, data, ativo);
    }

    public Abastecimento atualizar(Long id, Abastecimento dados) {

        Abastecimento existente = buscarPorId(id);

        Map<String, Object> updates = new HashMap<>();

        if (dados.getLitros() != null) {
            if (dados.getLitros().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ValidacaoException("A quantidade de litros deve ser maior que zero.");
            }
            updates.put("litros", dados.getLitros());
        }

        if (dados.getValorTotal() != null) {
            if (dados.getValorTotal().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ValidacaoException("O valor total deve ser maior que zero.");
            }
            updates.put("valor_total", dados.getValorTotal());
        }

        if (dados.getData() != null) {
            if (dados.getData().isAfter(LocalDateTime.now())) {
                throw new ValidacaoException("A data do abastecimento não pode estar no futuro.");
            }
            updates.put("data", dados.getData());
        }

        if (updates.isEmpty()) {
            throw new ValidacaoException("Nenhum campo enviado para atualização.");
        }

        return repository.atualizarAbastecimento(id, updates);
    }

    public void desativar(Long id) {
        repository.desativarAbastecimento(id);
    }
}

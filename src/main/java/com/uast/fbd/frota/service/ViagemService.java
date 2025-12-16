package com.uast.fbd.frota.service;

import com.uast.fbd.frota.entity.Veiculo;
import com.uast.fbd.frota.entity.Viagem;
import com.uast.fbd.frota.exception.ValidacaoException;
import com.uast.fbd.frota.repository.VeiculoRepository;
import com.uast.fbd.frota.repository.ViagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ViagemService {

    private final ViagemRepository repository;
    private final VeiculoRepository veiculoRepository;

    public Viagem criar(Viagem v) {

        if (v.getVeiculoId() == null) {
            throw new ValidacaoException("O veículo é obrigatório.");
        }

        Veiculo veiculo = veiculoRepository.buscarVeiculoPorId(v.getVeiculoId());
        if (!veiculo.isAtivo()) {
            throw new ValidacaoException("O veículo está desativado.");
        }

        if (v.getMotoristaId() == null) {
            throw new ValidacaoException("O motorista é obrigatório.");
        }

        if (v.getDestino() == null || v.getDestino().isBlank()) {
            throw new ValidacaoException("O destino é obrigatório.");
        }

        if (v.getDataSaida() == null) {
            throw new ValidacaoException("A data de saída é obrigatória.");
        }

        if (v.getDataSaida().isAfter(LocalDateTime.now())) {
            throw new ValidacaoException("A data de saída não pode estar no futuro.");
        }

        v.setAtivo(true);
        v.setDataRetorno(null);

        return repository.criarViagem(v);
    }

    public Viagem buscarPorId(Long id) {
        return repository.buscarPorId(id);
    }

    public List<Viagem> buscar(
            Long veiculoId,
            Long motoristaId,
            String destino,
            LocalDateTime dataIni,
            LocalDateTime dataFim,
            Boolean emAndamento,
            Boolean ativo
    ) {
        return repository.buscarViagens(
                veiculoId,
                motoristaId,
                destino,
                dataIni,
                dataFim,
                emAndamento,
                ativo
        );
    }

    public Viagem atualizar(Long id, Viagem dados) {

        Viagem atual = buscarPorId(id);

        Map<String, Object> updates = new HashMap<>();

        if (dados.getDataRetorno() != null) {

            if (dados.getDataRetorno().isBefore(atual.getDataSaida())) {
                throw new ValidacaoException("A data de retorno não pode ser anterior à data de saída.");
            }

            if (dados.getDataRetorno().isAfter(LocalDateTime.now())) {
                throw new ValidacaoException("A data de retorno não pode estar no futuro.");
            }

            updates.put("data_retorno", dados.getDataRetorno());
        }

        if (updates.isEmpty()) {
            throw new ValidacaoException("Nenhum campo enviado para atualização.");
        }

        return repository.atualizarViagem(id, updates);
    }

    public void desativar(Long id) {
        repository.desativarViagem(id);
    }
}

package com.uast.fbd.frota.service;

import com.uast.fbd.frota.repository.RelatoriosRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RelatoriosService {

    private final RelatoriosRepository repository;

    public RelatoriosService(RelatoriosRepository repository) {
        this.repository = repository;
    }

    public List<Map<String, Object>> veiculosDisponiveis() {
        return repository.veiculosDisponiveis();
    }

    public Map<String, Object> viagens(
            String dataIni,
            String dataFim,
            Integer veiculoId,
            Integer motoristaId
    ) {
        var itens = repository.viagensPorPeriodo(dataIni, dataFim, veiculoId, motoristaId);

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("periodo", Map.of("ini", dataIni, "fim", dataFim));
        resposta.put("total", itens.size());
        resposta.put("itens", itens);

        return resposta;
    }

    public List<Map<String, Object>> custos(Integer veiculoId) {
        return repository.custosPorVeiculo(veiculoId);
    }

    public List<Map<String, Object>> cnhsAVencer(String ate) {
        return repository.cnhsAVencer(ate);
    }

    public Map<String, Object> abastecimentos(
            Integer veiculoId,
            String dataIni,
            String dataFim
    ) {
        var lista = repository.abastecimentosPorPeriodo(veiculoId, dataIni, dataFim);

        double totalLitros = 0;
        double totalGasto = 0;

        for (var l : lista) {
            totalLitros += ((Number) l.get("litros")).doubleValue();
            totalGasto += ((Number) l.get("valor_total")).doubleValue();
        }

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("veiculo_id", veiculoId);
        resposta.put("periodo", Map.of("ini", dataIni, "fim", dataFim));
        resposta.put("total_registros", lista.size());
        resposta.put("total_litros", totalLitros);
        resposta.put("total_gasto", totalGasto);
        resposta.put("itens", lista);

        return resposta;
    }

    public Map<String, Object> manutencoes(
            Integer veiculoId,
            String dataIni,
            String dataFim
    ) {
        var lista = repository.manutencoesPorPeriodo(veiculoId, dataIni, dataFim);

        double totalCusto = 0;

        for (var l : lista) {
            totalCusto += ((Number) l.get("custo")).doubleValue();
        }

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("veiculo_id", veiculoId);
        resposta.put("periodo", Map.of("ini", dataIni, "fim", dataFim));
        resposta.put("total_registros", lista.size());
        resposta.put("total_custo", totalCusto);
        resposta.put("itens", lista);

        return resposta;
    }

    public List<Map<String, Object>> viagensEmAndamento() {
        return repository.viagensEmAndamento();
    }
}

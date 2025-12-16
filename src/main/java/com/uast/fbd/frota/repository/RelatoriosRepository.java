package com.uast.fbd.frota.repository;

import com.uast.fbd.frota.sql.RelatoriosSQL;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class RelatoriosRepository {

    private final JdbcTemplate jdbcTemplate;

    public RelatoriosRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> veiculosDisponiveis() {
        return jdbcTemplate.queryForList(RelatoriosSQL.VEICULOS_DISPONIVEIS);
    }

    public List<Map<String, Object>> viagensPorPeriodo(
            String dataIni,
            String dataFim,
            Integer veiculoId,
            Integer motoristaId
    ) {
        return jdbcTemplate.queryForList(
                RelatoriosSQL.VIAGENS_POR_PERIODO,
                dataIni, dataIni,
                dataFim, dataFim,
                veiculoId, veiculoId,
                motoristaId, motoristaId
        );
    }

    public List<Map<String, Object>> custosPorVeiculo(
            Integer veiculoId
    ) {
        return jdbcTemplate.queryForList(
                RelatoriosSQL.CUSTOS_POR_VEICULO,
                veiculoId, veiculoId
        );
    }

    public List<Map<String, Object>> cnhsAVencer(String ate) {
        return jdbcTemplate.queryForList(
                RelatoriosSQL.CNHS_A_VENCER,
                ate
        );
    }

    public List<Map<String, Object>> abastecimentosPorPeriodo(
            Integer veiculoId, String dataIni, String dataFim
    ) {
        return jdbcTemplate.queryForList(
                RelatoriosSQL.ABASTECIMENTOS_PERIODO,
                veiculoId, veiculoId,
                dataIni, dataIni,
                dataFim, dataFim
        );
    }

    public List<Map<String, Object>> manutencoesPorPeriodo(
            Integer veiculoId, String dataIni, String dataFim
    ) {
        return jdbcTemplate.queryForList(
                RelatoriosSQL.MANUTENCOES_PERIODO,
                veiculoId, veiculoId,
                dataIni, dataIni,
                dataFim, dataFim
        );
    }

    public List<Map<String, Object>> viagensEmAndamento() {
        return jdbcTemplate.queryForList(RelatoriosSQL.VIAGENS_EM_ANDAMENTO);
    }
}

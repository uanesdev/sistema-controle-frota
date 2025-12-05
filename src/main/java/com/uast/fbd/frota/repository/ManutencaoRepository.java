package com.uast.fbd.frota.repository;

import com.uast.fbd.frota.entity.Manutencao;
import com.uast.fbd.frota.exception.RecursoNaoEncontradoException;
import com.uast.fbd.frota.sql.ManutencaoSQL;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ManutencaoRepository {

    private final JdbcTemplate jdbcTemplate;

    public ManutencaoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Manutencao criarManutencao(Manutencao m) {
        return jdbcTemplate.queryForObject(
                ManutencaoSQL.QUERY_CRIAR_MANUTENCAO,
                (rs, rowNum) -> new Manutencao(
                        rs.getLong("id"),
                        rs.getLong("veiculo_id"),
                        rs.getString("descricao"),
                        rs.getBigDecimal("custo"),
                        rs.getTimestamp("data").toLocalDateTime(),
                        rs.getBoolean("ativo")
                ),
                m.getVeiculoId(),
                m.getDescricao(),
                m.getCusto(),
                m.getData() != null ? Timestamp.valueOf(m.getData()) : null,
                m.getAtivo()
        );
    }

    public Manutencao buscarPorId(Long id) {

        List<Manutencao> lista = jdbcTemplate.query(
                ManutencaoSQL.QUERY_BUSCAR_MANUTENCAO_POR_ID,
                (rs, rowNum) -> new Manutencao(
                        rs.getLong("id"),
                        rs.getLong("veiculo_id"),
                        rs.getString("descricao"),
                        rs.getBigDecimal("custo"),
                        rs.getTimestamp("data").toLocalDateTime(),
                        rs.getBoolean("ativo")
                ),
                id
        );

        if (lista.isEmpty()) {
            throw new RecursoNaoEncontradoException("Manutenção não encontrada com id: " + id);
        }

        return lista.get(0);
    }

    public List<Manutencao> buscarManutencoes(
            Long veiculoId,
            String descricao,
            Boolean ativo
    ) {
        StringBuilder sql = new StringBuilder(ManutencaoSQL.QUERY_BUSCAR_BASE);
        List<Object> params = new ArrayList<>();

        if (veiculoId != null) {
            sql.append(" AND veiculo_id = ? ");
            params.add(veiculoId);
        }

        if (descricao != null) {
            sql.append(" AND descricao ILIKE ? ");
            params.add("%" + descricao + "%");
        }

        if (ativo != null) {
            sql.append(" AND ativo = ? ");
            params.add(ativo);
        }

        return jdbcTemplate.query(
                sql.toString(),
                params.toArray(),
                (rs, rowNum) -> new Manutencao(
                        rs.getLong("id"),
                        rs.getLong("veiculo_id"),
                        rs.getString("descricao"),
                        rs.getBigDecimal("custo"),
                        rs.getTimestamp("data").toLocalDateTime(),
                        rs.getBoolean("ativo")
                )
        );
    }

    public Manutencao desativarManutencao(Long id) {

        List<Manutencao> lista = jdbcTemplate.query(
                ManutencaoSQL.QUERY_DESATIVAR_MANUTENCAO,
                (rs, rowNum) -> new Manutencao(
                        rs.getLong("id"),
                        rs.getLong("veiculo_id"),
                        rs.getString("descricao"),
                        rs.getBigDecimal("custo"),
                        rs.getTimestamp("data").toLocalDateTime(),
                        rs.getBoolean("ativo")
                ),
                id
        );

        if (lista.isEmpty()) {
            throw new RecursoNaoEncontradoException("Manutenção não encontrada ou já desativada.");
        }

        return lista.get(0);
    }

    public Manutencao atualizarManutencao(Long id, Map<String, Object> updates) {

        if (updates.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhum campo enviado para atualização");
        }

        StringBuilder sql = new StringBuilder("UPDATE manutencao SET ");
        List<Object> params = new ArrayList<>();

        updates.forEach((key, value) -> {
            sql.append(key).append(" = ?, ");
            if (value instanceof LocalDateTime) {
                params.add(Timestamp.valueOf((LocalDateTime) value));
            } else {
                params.add(value);
            }
        });

        sql.setLength(sql.length() - 2);

        sql.append(" WHERE id = ? RETURNING id, veiculo_id, descricao, custo, data, ativo");
        params.add(id);

        List<Manutencao> lista = jdbcTemplate.query(
                sql.toString(),
                params.toArray(),
                (rs, rowNum) -> new Manutencao(
                        rs.getLong("id"),
                        rs.getLong("veiculo_id"),
                        rs.getString("descricao"),
                        rs.getBigDecimal("custo"),
                        rs.getTimestamp("data").toLocalDateTime(),
                        rs.getBoolean("ativo")
                )
        );

        if (lista.isEmpty()) {
            throw new RecursoNaoEncontradoException("Manutenção não encontrada com id: " + id);
        }

        return lista.get(0);
    }
}

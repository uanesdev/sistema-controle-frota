package com.uast.fbd.frota.repository;

import com.uast.fbd.frota.entity.Abastecimento;
import com.uast.fbd.frota.exception.RecursoNaoEncontradoException;
import com.uast.fbd.frota.sql.AbastecimentoSQL;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class AbastecimentoRepository {

    private final JdbcTemplate jdbcTemplate;

    public AbastecimentoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Abastecimento criarAbastecimento(Abastecimento a) {
        return jdbcTemplate.queryForObject(
                AbastecimentoSQL.QUERY_CRIAR_ABASTECIMENTO,
                (rs, rowNum) -> new Abastecimento(
                        rs.getLong("id"),
                        rs.getLong("veiculo_id"),
                        rs.getTimestamp("data").toLocalDateTime(),
                        rs.getBigDecimal("litros"),
                        rs.getBigDecimal("valor_total"),
                        rs.getBoolean("ativo")
                ),
                a.getVeiculoId(),
                a.getData() != null ? Timestamp.valueOf(a.getData()) : null,
                a.getLitros(),
                a.getValorTotal(),
                a.getAtivo()
        );
    }

    public Abastecimento buscarPorId(Long id) {

        List<Abastecimento> lista = jdbcTemplate.query(
                AbastecimentoSQL.QUERY_BUSCAR_ABASTECIMENTO_POR_ID,
                (rs, rowNum) -> new Abastecimento(
                        rs.getLong("id"),
                        rs.getLong("veiculo_id"),
                        rs.getTimestamp("data").toLocalDateTime(),
                        rs.getBigDecimal("litros"),
                        rs.getBigDecimal("valor_total"),
                        rs.getBoolean("ativo")
                ),
                id
        );

        if (lista.isEmpty()) {
            throw new RecursoNaoEncontradoException("Abastecimento não encontrado com id: " + id);
        }

        return lista.get(0);
    }

    public List<Abastecimento> buscarAbastecimentos(
            Long veiculoId,
            LocalDateTime data,
            Boolean ativo
    ) {
        StringBuilder sql = new StringBuilder(AbastecimentoSQL.QUERY_BUSCAR_BASE);
        List<Object> params = new ArrayList<>();

        if (veiculoId != null) {
            sql.append(" AND veiculo_id = ? ");
            params.add(veiculoId);
        }

        if (data != null) {
            sql.append(" AND data::date = ?::date ");
            params.add(data.toLocalDate());
        }

        if (ativo != null) {
            sql.append(" AND ativo = ? ");
            params.add(ativo);
        }

        return jdbcTemplate.query(
                sql.toString(),
                params.toArray(),
                (rs, rowNum) -> new Abastecimento(
                        rs.getLong("id"),
                        rs.getLong("veiculo_id"),
                        rs.getTimestamp("data").toLocalDateTime(),
                        rs.getBigDecimal("litros"),
                        rs.getBigDecimal("valor_total"),
                        rs.getBoolean("ativo")
                )
        );
    }

    public Abastecimento desativarAbastecimento(Long id) {

        List<Abastecimento> lista = jdbcTemplate.query(
                AbastecimentoSQL.QUERY_DESATIVAR_ABASTECIMENTO,
                (rs, rowNum) -> new Abastecimento(
                        rs.getLong("id"),
                        rs.getLong("veiculo_id"),
                        rs.getTimestamp("data").toLocalDateTime(),
                        rs.getBigDecimal("litros"),
                        rs.getBigDecimal("valor_total"),
                        rs.getBoolean("ativo")
                ),
                id
        );

        if (lista.isEmpty()) {
            throw new RecursoNaoEncontradoException("Abastecimento não encontrado ou já desativado.");
        }

        return lista.get(0);
    }

    public Abastecimento atualizarAbastecimento(Long id, Map<String, Object> updates) {

        if (updates.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhum campo enviado para atualização");
        }

        StringBuilder sql = new StringBuilder("UPDATE abastecimento SET ");
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

        sql.append(" WHERE id = ? RETURNING id, veiculo_id, data, litros, valor_total, ativo");
        params.add(id);

        List<Abastecimento> lista = jdbcTemplate.query(
                sql.toString(),
                params.toArray(),
                (rs, rowNum) -> new Abastecimento(
                        rs.getLong("id"),
                        rs.getLong("veiculo_id"),
                        rs.getTimestamp("data").toLocalDateTime(),
                        rs.getBigDecimal("litros"),
                        rs.getBigDecimal("valor_total"),
                        rs.getBoolean("ativo")
                )
        );

        if (lista.isEmpty()) {
            throw new RecursoNaoEncontradoException("Abastecimento não encontrado com id: " + id);
        }

        return lista.get(0);
    }
}

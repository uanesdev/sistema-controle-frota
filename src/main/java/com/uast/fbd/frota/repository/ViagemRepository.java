package com.uast.fbd.frota.repository;

import com.uast.fbd.frota.entity.Viagem;
import com.uast.fbd.frota.exception.RecursoNaoEncontradoException;
import com.uast.fbd.frota.exception.ValidacaoException;
import com.uast.fbd.frota.sql.ViagemSQL;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ViagemRepository {

    private final JdbcTemplate jdbcTemplate;

    public ViagemRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Viagem criarViagem(Viagem v) {

        List<Viagem> lista = jdbcTemplate.query(
                ViagemSQL.QUERY_CRIAR_VIAGEM_COM_VALIDACOES,
                (rs, rowNum) -> new Viagem(
                        rs.getLong("id"),
                        rs.getLong("veiculo_id"),
                        rs.getLong("motorista_id"),
                        rs.getString("destino"),
                        rs.getTimestamp("data_saida").toLocalDateTime(),
                        null,
                        rs.getBoolean("ativo")
                ),
                v.getVeiculoId(),
                v.getVeiculoId(),
                v.getMotoristaId(),
                v.getDestino(),
                Timestamp.valueOf(v.getDataSaida())
        );

        if (lista.isEmpty()) {
            throw new ValidacaoException(
                    "Veículo inexistente, inativo ou não disponível para viagem."
            );
        }

        return lista.get(0);
    }


    public Viagem buscarPorId(Long id) {

        List<Viagem> lista = jdbcTemplate.query(
                ViagemSQL.QUERY_BUSCAR_POR_ID,
                (rs, rowNum) -> new Viagem(
                        rs.getLong("id"),
                        rs.getLong("veiculo_id"),
                        rs.getLong("motorista_id"),
                        rs.getString("destino"),
                        rs.getTimestamp("data_saida").toLocalDateTime(),
                        rs.getTimestamp("data_retorno") != null
                                ? rs.getTimestamp("data_retorno").toLocalDateTime()
                                : null,
                        rs.getBoolean("ativo")
                ),
                id
        );

        if (lista.isEmpty()) {
            throw new RecursoNaoEncontradoException("Viagem não encontrada com id: " + id);
        }

        return lista.get(0);
    }

    public List<Viagem> buscarViagens(
            Long veiculoId,
            Long motoristaId,
            String destino,
            LocalDateTime dataIni,
            LocalDateTime dataFim,
            Boolean emAndamento,
            Boolean ativo
    ) {

        StringBuilder sql = new StringBuilder(ViagemSQL.QUERY_BUSCAR_BASE);
        List<Object> params = new ArrayList<>();

        if (veiculoId != null) {
            sql.append(" AND veiculo_id = ? ");
            params.add(veiculoId);
        }

        if (motoristaId != null) {
            sql.append(" AND motorista_id = ? ");
            params.add(motoristaId);
        }

        if (destino != null) {
            sql.append(" AND destino ILIKE ? ");
            params.add("%" + destino + "%");
        }

        if (dataIni != null) {
            sql.append(" AND data_saida >= ? ");
            params.add(Timestamp.valueOf(dataIni));
        }

        if (dataFim != null) {
            sql.append(" AND data_saida <= ? ");
            params.add(Timestamp.valueOf(dataFim));
        }

        if (emAndamento != null) {
            if (emAndamento) {
                sql.append(" AND data_retorno IS NULL ");
            } else {
                sql.append(" AND data_retorno IS NOT NULL ");
            }
        }

        if (ativo != null) {
            sql.append(" AND ativo = ? ");
            params.add(ativo);
        }

        return jdbcTemplate.query(
                sql.toString(),
                params.toArray(),
                (rs, rowNum) -> new Viagem(
                        rs.getLong("id"),
                        rs.getLong("veiculo_id"),
                        rs.getLong("motorista_id"),
                        rs.getString("destino"),
                        rs.getTimestamp("data_saida").toLocalDateTime(),
                        rs.getTimestamp("data_retorno") != null
                                ? rs.getTimestamp("data_retorno").toLocalDateTime()
                                : null,
                        rs.getBoolean("ativo")
                )
        );
    }

    public Viagem atualizarViagem(Long id, Map<String, Object> updates) {

        if (updates.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhum campo enviado para atualização");
        }

        StringBuilder sql = new StringBuilder("UPDATE viagem SET ");
        List<Object> params = new ArrayList<>();

        updates.forEach((campo, valor) -> {
            sql.append(campo).append(" = ?, ");

            if (valor instanceof LocalDateTime) {
                params.add(Timestamp.valueOf((LocalDateTime) valor));
            } else {
                params.add(valor);
            }
        });

        sql.setLength(sql.length() - 2);
        sql.append(" WHERE id = ? RETURNING id, veiculo_id, motorista_id, destino, data_saida, data_retorno, ativo");

        params.add(id);

        List<Viagem> lista = jdbcTemplate.query(
                sql.toString(),
                params.toArray(),
                (rs, rowNum) -> new Viagem(
                        rs.getLong("id"),
                        rs.getLong("veiculo_id"),
                        rs.getLong("motorista_id"),
                        rs.getString("destino"),
                        rs.getTimestamp("data_saida").toLocalDateTime(),
                        rs.getTimestamp("data_retorno") != null
                                ? rs.getTimestamp("data_retorno").toLocalDateTime()
                                : null,
                        rs.getBoolean("ativo")
                )
        );

        if (lista.isEmpty()) {
            throw new RecursoNaoEncontradoException("Viagem não encontrada com id: " + id);
        }

        return lista.get(0);
    }

    public Viagem desativarViagem(Long id) {

        List<Viagem> lista = jdbcTemplate.query(
                ViagemSQL.QUERY_DESATIVAR_VIAGEM,
                (rs, rowNum) -> new Viagem(
                        rs.getLong("id"),
                        rs.getLong("veiculo_id"),
                        rs.getLong("motorista_id"),
                        rs.getString("destino"),
                        rs.getTimestamp("data_saida").toLocalDateTime(),
                        rs.getTimestamp("data_retorno") != null
                                ? rs.getTimestamp("data_retorno").toLocalDateTime()
                                : null,
                        rs.getBoolean("ativo")
                ),
                id
        );

        if (lista.isEmpty()) {
            throw new RecursoNaoEncontradoException("Viagem não encontrada ou já desativada.");
        }

        return lista.get(0);
    }
}
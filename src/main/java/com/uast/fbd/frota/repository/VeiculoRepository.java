package com.uast.fbd.frota.repository;

import com.uast.fbd.frota.entity.Veiculo;
import com.uast.fbd.frota.exception.RecursoNaoEncontradoException;
import com.uast.fbd.frota.exception.ValidacaoException;
import com.uast.fbd.frota.sql.VeiculoSQL;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class VeiculoRepository {

    private final JdbcTemplate jdbcTemplate;

    public VeiculoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Veiculo criarVeiculo(Veiculo v) {
        boolean exists = !jdbcTemplate.queryForList(
                VeiculoSQL.QUERY_PLACA_VEICULO,
                v.getPlaca()
        ).isEmpty();

        if (exists) {
            throw new ValidacaoException(
                    "Placa " + v.getPlaca() + " já registrada!"
            );
        }

        return jdbcTemplate.queryForObject(
                VeiculoSQL.QUERY_CRIAR_VEICULO,
                (rs, rowNum) -> new Veiculo(
                        rs.getLong("id"),
                        rs.getString("placa"),
                        rs.getString("modelo"),
                        rs.getInt("ano"),
                        rs.getString("status"),
                        rs.getBoolean("ativo")
                ),
                v.getPlaca(),
                v.getModelo(),
                v.getAno(),
                v.getStatus(),
                v.isAtivo()
        );
    }

    public List<Veiculo> buscarVeiculos(String placa, String modelo, Integer ano,
                                        String status, Boolean ativo) {

        StringBuilder sql = new StringBuilder(
                "SELECT id, placa, modelo, ano, status, ativo FROM veiculo WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();

        if (placa != null) {
            sql.append(" AND placa = ? ");
            params.add(placa);
        }

        if (modelo != null) {
            sql.append(" AND modelo ILIKE ? ");
            params.add("%" + modelo + "%");
        }

        if (ano != null) {
            sql.append(" AND ano = ? ");
            params.add(ano);
        }

        if (status != null) {
            sql.append(" AND status = ? ");
            params.add(status);
        }

        if (ativo != null) {
            sql.append(" AND ativo = ? ");
            params.add(ativo);
        }

        return jdbcTemplate.query(
                sql.toString(),
                params.toArray(),
                (rs, rowNum) -> new Veiculo(
                        rs.getLong("id"),
                        rs.getString("placa"),
                        rs.getString("modelo"),
                        rs.getInt("ano"),
                        rs.getString("status"),
                        rs.getBoolean("ativo")
                )
        );
    }

    public Veiculo buscarVeiculoPorId(Long id) {
        return jdbcTemplate.queryForObject(
                VeiculoSQL.QUERY_BUSCAR_VEICULO_POR_ID,
                (rs, rowNum) -> new Veiculo(
                        rs.getLong("id"),
                        rs.getString("placa"),
                        rs.getString("modelo"),
                        rs.getInt("ano"),
                        rs.getString("status"),
                        rs.getBoolean("ativo")
                ),
                id
        );
    }

    public Veiculo desativarVeiculo(Long id) {
        List<Veiculo> veiculos = jdbcTemplate.query(
                VeiculoSQL.QUERY_DESATIVAR_VEICULO,
                (rs, rowNum) -> new Veiculo(
                        rs.getLong("id"),
                        rs.getString("placa"),
                        rs.getString("modelo"),
                        rs.getInt("ano"),
                        rs.getString("status"),
                        rs.getBoolean("ativo")
                ),
                id
        );

        if (veiculos.isEmpty()) {
            throw new RecursoNaoEncontradoException("Veículo não encontrado ou já desativado");
        }

        return veiculos.get(0);
    }

    public Veiculo atualizarVeiculo(Long id, Map<String, Object> updates) {
        if (updates.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhum campo enviado para atualização");
        }

        StringBuilder sql = new StringBuilder("UPDATE veiculo SET ");
        List<Object> params = new ArrayList<>();

        updates.forEach((key, value) -> {
            sql.append(key).append(" = ?, ");
            params.add(value);
        });

        sql.setLength(sql.length() - 2);

        sql.append(" WHERE id = ? RETURNING id, placa, modelo, ano, status, ativo");
        params.add(id);

        List<Veiculo> veiculos = jdbcTemplate.query(
                sql.toString(),
                params.toArray(),
                (rs, rowNum) -> new Veiculo(
                        rs.getLong("id"),
                        rs.getString("placa"),
                        rs.getString("modelo"),
                        rs.getInt("ano"),
                        rs.getString("status"),
                        rs.getBoolean("ativo")
                )
        );

        if (veiculos.isEmpty()) {
            throw new RecursoNaoEncontradoException("Veículo não encontrado com id: " + id);
        }

        return veiculos.get(0);
    }


}

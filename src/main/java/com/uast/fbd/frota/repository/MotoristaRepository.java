package com.uast.fbd.frota.repository;

import com.uast.fbd.frota.exception.RecursoNaoEncontradoException;
import com.uast.fbd.frota.exception.ValidacaoException;
import com.uast.fbd.frota.entity.Motorista;
import com.uast.fbd.frota.sql.MotoristaSQL;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class MotoristaRepository {

    private final JdbcTemplate jdbcTemplate;

    public MotoristaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Motorista criarMotorista(Motorista m) {

        boolean exists = !jdbcTemplate.queryForList(
                MotoristaSQL.QUERY_EXISTE_CNH,
                m.getCnh()
        ).isEmpty();

        if (exists) {
            throw new ValidacaoException("CNH " + m.getCnh() + " já registrada!");
        }

        return jdbcTemplate.queryForObject(
                MotoristaSQL.QUERY_CRIAR_MOTORISTA,
                (rs, rowNum) -> new Motorista(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cnh"),
                        rs.getObject("validade_cnh", LocalDate.class),
                        rs.getBoolean("ativo")
                ),
                m.getNome(),
                m.getCnh(),
                m.getValidadeCnh(),
                m.getAtivo()
        );
    }

    public List<Motorista> buscarMotoristas(String nome, String cnh,
                                            LocalDate validadeAte, Boolean ativo) {

        StringBuilder sql = new StringBuilder(
                "SELECT id, nome, cnh, validade_cnh, ativo FROM motorista WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();

        if (nome != null) {
            sql.append(" AND nome ILIKE ? ");
            params.add("%" + nome + "%");
        }

        if (cnh != null) {
            sql.append(" AND cnh = ? ");
            params.add(cnh);
        }

        if (validadeAte != null) {
            sql.append(" AND validade_cnh <= ? ");
            params.add(validadeAte);
        }

        if (ativo != null) {
            sql.append(" AND ativo = ? ");
            params.add(ativo);
        }

        return jdbcTemplate.query(
                sql.toString(),
                params.toArray(),
                (rs, rowNum) -> new Motorista(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cnh"),
                        rs.getObject("validade_cnh", LocalDate.class),
                        rs.getBoolean("ativo")
                )
        );
    }

    public Motorista buscarMotoristaPorId(Integer id) {
        List<Motorista> lista = jdbcTemplate.query(
                MotoristaSQL.QUERY_BUSCAR_MOTORISTA_POR_ID,
                (rs, rowNum) -> new Motorista(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cnh"),
                        rs.getObject("validade_cnh", LocalDate.class),
                        rs.getBoolean("ativo")
                ),
                id
        );

        if (lista.isEmpty()) {
            throw new RecursoNaoEncontradoException("Motorista não encontrado com id: " + id);
        }

        return lista.get(0);
    }

    public Motorista desativarMotorista(Integer id) {
        List<Motorista> lista = jdbcTemplate.query(
                MotoristaSQL.QUERY_DESATIVAR_MOTORISTA,
                (rs, rowNum) -> new Motorista(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cnh"),
                        rs.getObject("validade_cnh", LocalDate.class),
                        rs.getBoolean("ativo")
                ),
                id
        );

        if (lista.isEmpty()) {
            throw new RecursoNaoEncontradoException(
                    "Motorista não encontrado ou já desativado"
            );
        }

        return lista.get(0);
    }

    public Motorista atualizarMotorista(Integer id, Map<String, Object> updates) {

        if (updates.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhum campo enviado para atualização");
        }

        StringBuilder sql = new StringBuilder("UPDATE motorista SET ");
        List<Object> params = new ArrayList<>();

        updates.forEach((key, value) -> {
            sql.append(key).append(" = ?, ");
            params.add(value);
        });

        sql.setLength(sql.length() - 2); // remove última vírgula

        sql.append(" WHERE id = ? RETURNING id, nome, cnh, validade_cnh, ativo");
        params.add(id);

        List<Motorista> lista = jdbcTemplate.query(
                sql.toString(),
                params.toArray(),
                (rs, rowNum) -> new Motorista(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cnh"),
                        rs.getObject("validade_cnh", LocalDate.class),
                        rs.getBoolean("ativo")
                )
        );

        if (lista.isEmpty()) {
            throw new RecursoNaoEncontradoException("Motorista não encontrado com id: " + id);
        }

        return lista.get(0);
    }
}

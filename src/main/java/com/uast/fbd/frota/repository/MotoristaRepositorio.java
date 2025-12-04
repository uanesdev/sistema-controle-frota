package com.uast.fbd.frota.repository;

import com.uast.fbd.frota.entity.Motorista;
import com.uast.fbd.frota.sql.MotoristaSQL;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
public class MotoristaRepositorio {

    private final JdbcTemplate jdbcTemplate;

    public MotoristaRepositorio(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //parâmetros duplicados porque a consulta tem dois ? pra cada parâmetro!
    public List<Motorista> listarMotoristas(String nome, String cnh, String validadeCnhAte, Boolean ativo) {
        return jdbcTemplate.query(
                MotoristaSQL.QUERY_LISTAR_MOTORISTAS,
                (rs, rowNum) -> new Motorista(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("cnh"),
                        rs.getDate("validade_cnh").toLocalDate(),
                        rs.getBoolean("ativo")
                ),
                nome, nome,
                cnh, cnh,
                validadeCnhAte, validadeCnhAte,
                ativo, ativo
        );
    }


    public Motorista buscarMotoristaPorId(Long id) {
        return jdbcTemplate.queryForObject(
                MotoristaSQL.QUERY_BUSCAR_MOTORISTA_POR_ID,
                (rs, rowNum) -> new Motorista(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("cnh"),
                        rs.getDate("validade_cnh").toLocalDate(),
                        rs.getBoolean("ativo")
                ),
                id
        );
    }


    public Motorista criarMotorista(Motorista motorista) {
        return jdbcTemplate.queryForObject(
                MotoristaSQL.QUERY_CRIAR_MOTORISTA,
                (rs, rowNum) -> new Motorista(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("cnh"),
                        rs.getDate("validade_cnh").toLocalDate(),
                        rs.getBoolean("ativo")
                ),
                motorista.getNome(),
                motorista.getCnh(),
                motorista.getValidadeCnh().toString()
        );
    }


    public Motorista atualizarMotorista(Long id, Motorista motorista) {
        return jdbcTemplate.queryForObject(
                MotoristaSQL.QUERY_ATUALIZAR_MOTORISTA,
                (rs, rowNum) -> new Motorista(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("cnh"),
                        rs.getDate("validade_cnh").toLocalDate(),
                        rs.getBoolean("ativo")
                ),
                motorista.getNome(),
                motorista.getCnh(),
                motorista.getValidadeCnh().toString(),
                id
        );
    }


    public Motorista desativarMotorista(Long id) {
        return jdbcTemplate.queryForObject(
                MotoristaSQL.QUERY_DESATIVAR_MOTORISTA,
                (rs, rowNum) -> new Motorista(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("cnh"),
                        rs.getDate("validade_cnh").toLocalDate(),
                        rs.getBoolean("ativo")
                ),
                id
        );
    }


    public List<Map<String, Object>> listarCnhsAVencer(String ate) {
        return jdbcTemplate.queryForList(
                MotoristaSQL.QUERY_CNH_A_VENCER,
                ate
        );
    }
}

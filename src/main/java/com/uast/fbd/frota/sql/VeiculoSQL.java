package com.uast.fbd.frota.sql;

public class VeiculoSQL {

    public static final String QUERY_CRIAR_VEICULO = """
            INSERT INTO veiculo (placa, modelo, ano, status, ativo)
                VALUES (?, ?, ?, ?, ?)
                RETURNING *;
        """;

    public static final String QUERY_BUSCAR_VEICULO_POR_ID = """
            SELECT *
            FROM veiculo
            WHERE id = ?
        """;

    public static final String QUERY_DESATIVAR_VEICULO = """
            UPDATE veiculo
            SET ativo = FALSE
            WHERE id = ? AND ativo = TRUE
            RETURNING *;
        """;

    public static final String QUERY_PLACA_VEICULO = """
            SELECT id FROM veiculo WHERE placa = ?;
        """;
}

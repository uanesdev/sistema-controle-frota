package com.uast.fbd.frota.sql;

public class MotoristaSQL {

    public static final String QUERY_CRIAR_MOTORISTA = """
            INSERT INTO motorista (nome, cnh, validade_cnh)
                VALUES (?, ?, CAST(? AS DATE))
                RETURNING *;
        """;

    public static final String QUERY_LISTAR_MOTORISTAS = """
            SELECT *
            FROM motorista
            WHERE (? IS NULL OR nome ILIKE '%' || ? || '%')
              AND (? IS NULL OR cnh = ?)
              AND (? IS NULL OR validade_cnh <= CAST(? AS DATE))
              AND (? IS NULL OR ativo = ?);
        """;

    public static final String QUERY_BUSCAR_MOTORISTA_POR_ID = """
        SELECT *
        FROM motorista
        WHERE id = ?;
        """;

    public static final String QUERY_ATUALIZAR_MOTORISTA = """
            UPDATE motorista
            SET nome = ?,
                cnh = ?,
                validade_cnh = CAST(? AS DATE)
            WHERE id = ?
            RETURNING *;
        """;

    public static final String QUERY_DESATIVAR_MOTORISTA = """
        UPDATE motorista
        SET ativo = FALSE
        WHERE id = ?
        RETURNING *;
        """;

    public static final String QUERY_CNH_A_VENCER = """
        SELECT id AS motorista_id, nome, validade_cnh
        FROM motorista
        WHERE ativo = TRUE
          AND validade_cnh <= CAST(? AS DATE)
        ORDER BY validade_cnh ASC;
        """;
}

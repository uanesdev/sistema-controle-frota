package com.uast.fbd.frota.sql;

public class MotoristaSQL {

    public static final String QUERY_CRIAR_MOTORISTA = """
            INSERT INTO motorista (nome, cnh, validade_cnh)
                VALUES (:nome, :cnh, CAST(:validade_cnh AS DATE))
                RETURNING *;
        """;

    public static final String QUERY_LISTAR_MOTORISTAS = """
            SELECT *
            FROM motorista
            WHERE (:nome IS NULL OR nome ILIKE '%' || :nome || '%')
              AND (:cnh IS NULL OR cnh = :cnh)
              AND (:validade_cnh_ate IS NULL OR validade_cnh <= CAST(:validade_cnh_ate AS DATE))
              AND (:ativo IS NULL OR ativo = :ativo);
        """;

    public static final String QUERY_BUSCAR_MOTORISTA_POR_ID = """
        SELECT *
        FROM motorista
        WHERE id = :id;
        """;

    public static final String QUERY_ATUALIZAR_MOTORISTA = """
            UPDATE motorista
            SET nome = :nome,
                cnh = :cnh,
                validade_cnh = CAST(:validade_cnh AS DATE)
            WHERE id = :id
            RETURNING *;
        """;

    public static final String QUERY_DESATIVAR_MOTORISTA = """
        UPDATE motorista
        SET ativo = FALSE
        WHERE id = :id
        RETURNING *;
        """;

    public static final String QUERY_CNH_A_VENCER = """
    SELECT id AS motorista_id, nome, validade_cnh
    FROM motorista
    WHERE ativo = TRUE
      AND validade_cnh <= :ate
    ORDER BY validade_cnh ASC;
    """;

}

package com.uast.fbd.frota.sql;

public class MotoristaSQL {

    public static final String QUERY_EXISTE_CNH = """
        SELECT 1 FROM motorista WHERE cnh = ?
    """;

    public static final String QUERY_CRIAR_MOTORISTA = """
        INSERT INTO motorista (nome, cnh, validade_cnh, ativo)
        VALUES (?, ?, ?, ?)
        RETURNING id, nome, cnh, validade_cnh, ativo;
    """;

    public static final String QUERY_BUSCAR_MOTORISTA_POR_ID = """
        SELECT id, nome, cnh, validade_cnh, ativo
        FROM motorista
        WHERE id = ?;
    """;

    public static final String QUERY_DESATIVAR_MOTORISTA = """
        UPDATE motorista
        SET ativo = FALSE
        WHERE id = ?
        RETURNING id, nome, cnh, validade_cnh, ativo;
    """;
}

package com.uast.fbd.frota.sql;

public class ViagemSQL {

    public static final String QUERY_CRIAR_VIAGEM = """
        INSERT INTO viagem (veiculo_id, motorista_id, destino, data_saida, ativo)
        VALUES (?, ?, ?, ?, ?)
        RETURNING id, veiculo_id, motorista_id, destino, data_saida, data_retorno, ativo
    """;

    public static final String QUERY_BUSCAR_BASE = """
        SELECT id, veiculo_id, motorista_id, destino, data_saida, data_retorno, ativo
        FROM viagem
        WHERE 1=1
    """;

    public static final String QUERY_BUSCAR_POR_ID = """
        SELECT id, veiculo_id, motorista_id, destino, data_saida, data_retorno, ativo
        FROM viagem
        WHERE id = ?
    """;

    public static final String QUERY_DESATIVAR_VIAGEM = """
        UPDATE viagem
        SET ativo = false
        WHERE id = ? AND ativo = true
        RETURNING id, veiculo_id, motorista_id, destino, data_saida, data_retorno, ativo
    """;

    public static final String QUERY_CRIAR_VIAGEM_COM_VALIDACOES = """
        WITH veiculo_atualizado AS (
            UPDATE veiculo
            SET status = 'em_viagem'
            WHERE id = ?
              AND ativo = TRUE
              AND status = 'disponivel'
            RETURNING id
        )
        INSERT INTO viagem (
            veiculo_id,
            motorista_id,
            destino,
            data_saida,
            ativo
        )
        SELECT
            ?, ?, ?, ?, TRUE
        WHERE EXISTS (SELECT 1 FROM veiculo_atualizado)
        RETURNING id, veiculo_id, motorista_id, destino, data_saida, data_retorno, ativo
    """;
}

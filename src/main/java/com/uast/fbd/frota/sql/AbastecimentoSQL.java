package com.uast.fbd.frota.sql;

public class AbastecimentoSQL {

    public static final String QUERY_CRIAR_ABASTECIMENTO = """
        INSERT INTO abastecimento (veiculo_id, data, litros, valor_total, ativo)
        VALUES (?, ?, ?, ?, ?)
        RETURNING id, veiculo_id, data, litros, valor_total, ativo;
    """;

    public static final String QUERY_BUSCAR_ABASTECIMENTO_POR_ID = """
        SELECT id, veiculo_id, data, litros, valor_total, ativo
        FROM abastecimento
        WHERE id = ?;
    """;

    public static final String QUERY_DESATIVAR_ABASTECIMENTO = """
        UPDATE abastecimento
        SET ativo = FALSE
        WHERE id = ?
        RETURNING id, veiculo_id, data, litros, valor_total, ativo;
    """;

    public static final String QUERY_BUSCAR_BASE = """
        SELECT id, veiculo_id, data, litros, valor_total, ativo
        FROM abastecimento
        WHERE 1 = 1
    """;
}

package com.uast.fbd.frota.sql;

public class ManutencaoSQL {

    // Criar manutenção
    public static final String QUERY_CRIAR_MANUTENCAO = """
        INSERT INTO manutencao (veiculo_id, descricao, custo, data, ativo)
        VALUES (?, ?, ?, ?, ?)
        RETURNING id, veiculo_id, descricao, custo, data, ativo;
    """;

    // Buscar por ID
    public static final String QUERY_BUSCAR_MANUTENCAO_POR_ID = """
        SELECT id, veiculo_id, descricao, custo, data, ativo
        FROM manutencao
        WHERE id = ?;
    """;

    // Desativar (soft delete)
    public static final String QUERY_DESATIVAR_MANUTENCAO = """
        UPDATE manutencao
        SET ativo = FALSE
        WHERE id = ?
        RETURNING id, veiculo_id, descricao, custo, data, ativo;
    """;

    // Query base para filtros dinâmicos
    public static final String QUERY_BUSCAR_BASE = """
        SELECT id, veiculo_id, descricao, custo, data, ativo
        FROM manutencao
        WHERE 1 = 1
    """;
}

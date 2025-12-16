package com.uast.fbd.frota.sql;

public class RelatoriosSQL {

    public static final String VEICULOS_DISPONIVEIS = """
        SELECT v.id, v.placa, v.modelo, v.ano
        FROM veiculo v
        WHERE v.status = 'disponivel'
          AND v.ativo = true
          AND NOT EXISTS (
              SELECT 1 FROM viagem vi
              WHERE vi.veiculo_id = v.id
                AND vi.data_retorno IS NULL
                AND vi.ativo = true
          )
    """;

    public static final String VIAGENS_POR_PERIODO = """
        SELECT *
        FROM viagem v
        WHERE v.ativo = true
          AND (v.data_saida >= CAST(? AS DATE) OR CAST(? AS DATE) IS NULL)
          AND (v.data_saida <= CAST(? AS DATE) OR CAST(? AS DATE) IS NULL)
          AND (v.veiculo_id = CAST(? AS BIGINT) OR CAST(? AS BIGINT) IS NULL)
          AND (v.motorista_id = CAST(? AS BIGINT) OR CAST(? AS BIGINT) IS NULL)
""";

    //left join de total de subqueries para não excluir quando um deles for 0 (abastecimento e
    // manutenção)/COALESCE (retornar o valor se não for null ou 0 se for null)
    public static final String CUSTOS_POR_VEICULO = """
        SELECT
            v.id AS veiculo_id,
            COALESCE(a.total_abastecimento, 0) AS abastecimento_total,
            COALESCE(m.total_manutencao, 0) AS manutencao_total,
            COALESCE(a.total_abastecimento, 0) + COALESCE(m.total_manutencao, 0) AS custo_total
        FROM veiculo v
        LEFT JOIN (
            SELECT veiculo_id, SUM(valor_total) AS total_abastecimento
            FROM abastecimento
            GROUP BY veiculo_id
        ) a ON a.veiculo_id = v.id
        LEFT JOIN (
            SELECT veiculo_id, SUM(custo) AS total_manutencao
            FROM manutencao
            GROUP BY veiculo_id
        ) m ON m.veiculo_id = v.id
        WHERE (v.id = CAST(? AS BIGINT) OR CAST(? AS BIGINT) IS NULL)
""";

    /*
    Gera uma dependência entre as duas tabelas e dá um custo_total errado! (usando para 3 manutenções e 2 abastecimentos vai dar 6 linhas!)
    SELECT
            v.id AS veiculo_id,
            COALESCE(SUM(a.valor_total),0) AS abastecimento_total,
            COALESCE(SUM(m.custo),0) AS manutencao_total,
            (COALESCE(SUM(a.valor_total),0) + COALESCE(SUM(m.custo),0)) AS custo_total
        FROM veiculo v
        LEFT JOIN abastecimento a ON a.veiculo_id = v.id
        LEFT JOIN manutencao m ON m.veiculo_id = v.id
        WHERE (? IS NULL OR v.id = ?)
        GROUP BY v.id
     */


    public static final String CNHS_A_VENCER = """
        SELECT id AS motorista_id, nome, validade_cnh
        FROM motorista
        WHERE validade_cnh <= CAST(? AS DATE)
          AND ativo = true
    """;

    public static final String ABASTECIMENTOS_PERIODO = """
        SELECT *
        FROM abastecimento
        WHERE (? IS NULL OR veiculo_id = ?)
          AND (? IS NULL OR data >= CAST(? AS DATE))
          AND (? IS NULL OR data <= CAST(? AS DATE))
    """;

    public static final String MANUTENCOES_PERIODO = """
        SELECT *
        FROM manutencao
        WHERE (? IS NULL OR veiculo_id = ?)
          AND (? IS NULL OR data >= CAST(? AS DATE))
          AND (? IS NULL OR data <= CAST(? AS DATE))
    """;

    public static final String VIAGENS_EM_ANDAMENTO = """
        SELECT *
        FROM viagem
        WHERE data_retorno IS NULL
          AND ativo = true
    """;
}

package com.uast.fbd.frota.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Manutencao {
    private Long id;
    private Long veiculoId;
    private String descricao;
    private BigDecimal custo;
    private LocalDateTime data;
    private Boolean ativo;
}

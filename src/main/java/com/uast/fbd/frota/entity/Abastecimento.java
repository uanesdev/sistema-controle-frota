package com.uast.fbd.frota.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Abastecimento {
    private Long id;
    private Long veiculoId;
    private LocalDateTime data;
    private BigDecimal litros;
    private BigDecimal valorTotal;
    private Boolean ativo;
}

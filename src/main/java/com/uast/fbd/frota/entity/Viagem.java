package com.uast.fbd.frota.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Viagem {
    private Long id;
    private Long veiculoId;
    private Long motoristaId;
    private String destino;
    private LocalDateTime dataSaida;
    private LocalDateTime dataRetorno;
    private Boolean ativo = true;
}

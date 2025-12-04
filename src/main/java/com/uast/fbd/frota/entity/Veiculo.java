package com.uast.fbd.frota.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Veiculo {

    private Long id;
    private String placa;
    private String modelo;
    private int ano;
    private String status;
    private boolean ativo = true;
}


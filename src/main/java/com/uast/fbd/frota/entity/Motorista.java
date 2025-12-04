package com.uast.fbd.frota.entity;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Motorista {

    private Integer id;
    private String nome;
    private String cnh;
    private LocalDate validadeCnh;
    private Boolean ativo = true;

}

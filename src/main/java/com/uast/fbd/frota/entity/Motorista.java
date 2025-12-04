package com.uast.fbd.frota.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "motorista")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Motorista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String cnh;

    private LocalDate validadeCnh;

    @Column(nullable = false)
    private Boolean ativo = true;
}

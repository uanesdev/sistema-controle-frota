package com.uast.fbd.frota.controller;

import com.uast.fbd.frota.entity.Motorista;
import com.uast.fbd.frota.service.MotoristaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/motoristas")
@RequiredArgsConstructor
public class MotoristaController {

    private final MotoristaService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Motorista criar(@RequestBody Motorista motorista) {
        return service.criar(motorista);
    }

    @GetMapping
    public List<Motorista> buscar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cnh,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate validade_cnh_ate,

            @RequestParam(required = false) Boolean ativo
    ) {
        return service.buscar(nome, cnh, validade_cnh_ate, ativo);
    }

    @GetMapping("/{id}")
    public Motorista buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Motorista atualizar(
            @PathVariable Integer id,
            @RequestBody Motorista motorista
    ) {
        return service.atualizar(id, motorista);
    }

    @PatchMapping("/{id}/desativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(@PathVariable Integer id) {
        service.desativar(id);
    }
}

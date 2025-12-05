package com.uast.fbd.frota.controller;

import com.uast.fbd.frota.entity.Abastecimento;
import com.uast.fbd.frota.service.AbastecimentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/abastecimentos")
@RequiredArgsConstructor
public class AbastecimentoController {

    private final AbastecimentoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Abastecimento criar(@RequestBody Abastecimento abastecimento) {
        return service.criar(abastecimento);
    }

    @GetMapping
    public List<Abastecimento> buscar(
            @RequestParam(required = false) Long veiculoId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime data,
            @RequestParam(required = false) Boolean ativo
    ) {
        return service.buscar(veiculoId, data, ativo);
    }

    @GetMapping("/{id}")
    public Abastecimento buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Abastecimento atualizar(@PathVariable Long id, @RequestBody Abastecimento abastecimento) {
        return service.atualizar(id, abastecimento);
    }

    @PatchMapping("/{id}/desativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(@PathVariable Long id) {
        service.desativar(id);
    }
}

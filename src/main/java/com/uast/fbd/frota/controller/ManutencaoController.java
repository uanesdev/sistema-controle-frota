package com.uast.fbd.frota.controller;

import com.uast.fbd.frota.entity.Manutencao;
import com.uast.fbd.frota.service.ManutencaoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manutencoes")
public class ManutencaoController {

    private final ManutencaoService manutencaoService;

    public ManutencaoController(ManutencaoService manutencaoService) {
        this.manutencaoService = manutencaoService;
    }

    @PostMapping
    public Manutencao criar(@RequestBody Manutencao m) {
        return manutencaoService.criar(m);
    }

    @GetMapping
    public List<Manutencao> buscar(
            @RequestParam(required = false) Long veiculoId,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) Boolean ativo
    ) {
        return manutencaoService.buscar(veiculoId, descricao, ativo);
    }

    @GetMapping("/{id}")
    public Manutencao buscarPorId(@PathVariable Long id) {
        return manutencaoService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Manutencao atualizar(@PathVariable Long id, @RequestBody Manutencao m) {
        return manutencaoService.atualizar(id, m);
    }

    @PatchMapping("/{id}/desativar")
    public Manutencao desativar(@PathVariable Long id) {
        return manutencaoService.desativar(id);
    }
}

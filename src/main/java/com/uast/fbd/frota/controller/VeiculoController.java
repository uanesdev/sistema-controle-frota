package com.uast.fbd.frota.controller;

import com.uast.fbd.frota.entity.Veiculo;
import com.uast.fbd.frota.service.VeiculoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {

    private final VeiculoService service;

    public VeiculoController(VeiculoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Veiculo> criarVeiculo(@RequestBody Veiculo veiculo) {
        return ResponseEntity.status(201).body(service.criarVeiculo(veiculo));
    }

    @GetMapping
    public ResponseEntity<List<Veiculo>> buscarVeiculos(
            @RequestParam(required = false) String placa,
            @RequestParam(required = false) String modelo,
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Boolean ativo
    ) {
        List<Veiculo> veiculos = service.buscarVeiculos(placa, modelo, ano, status, ativo);
        return ResponseEntity.ok(veiculos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Veiculo> buscarVeiculoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarVeiculoPorId(id));
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Veiculo> desativarVeiculo(@PathVariable Long id) {
        return ResponseEntity.ok(service.desativarVeiculo(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Veiculo> atualizarVeiculoParcial(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates
    ) {
        return ResponseEntity.ok(service.atualizarVeiculo(id, updates));
    }


}

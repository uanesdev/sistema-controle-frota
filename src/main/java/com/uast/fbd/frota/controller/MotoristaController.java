package com.uast.fbd.frota.controller;

import com.uast.fbd.frota.entity.Motorista;
import com.uast.fbd.frota.service.MotoristaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/motoristas")
public class MotoristaController {

    private final MotoristaService service;

    public MotoristaController(MotoristaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Motorista> criar(@RequestBody Motorista motorista) {
        return ResponseEntity.status(201).body(service.criar(motorista));
    }

    @GetMapping
    public ResponseEntity<List<Motorista>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cnh,
            @RequestParam(name = "validade_cnh_ate", required = false) LocalDate validadeCnhAte,
            @RequestParam(required = false) Boolean ativo
    ) {
        return ResponseEntity.ok(service.listar(nome, cnh, validadeCnhAte, ativo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Motorista> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Motorista> atualizar(
            @PathVariable Long id,
            @RequestBody Motorista motorista
    ) {
        return ResponseEntity.ok(service.atualizar(id, motorista));
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        service.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/relatorios/cnhs-a-vencer")
    public ResponseEntity<List<Map<String, Object>>> cnhsAVencer(
            @RequestParam LocalDate ate
    ) {
        return ResponseEntity.ok(service.cnhsAVencer(ate));
    }
}

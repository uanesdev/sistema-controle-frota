package com.uast.fbd.frota.controller;

import com.uast.fbd.frota.service.RelatoriosService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/relatorios")
public class RelatoriosController {

    private final RelatoriosService service;

    public RelatoriosController(RelatoriosService service) {
        this.service = service;
    }

    @GetMapping("/veiculos-disponiveis")
    public List<Map<String, Object>> veiculosDisponiveis() {
        return service.veiculosDisponiveis();
    }

    @GetMapping("/viagens")
    public Map<String, Object> viagens(
            @RequestParam(name = "data_ini", required = false) String dataIni,
            @RequestParam(name = "data_fim", required = false) String dataFim,
            @RequestParam(name = "veiculo_id", required = false) Integer veiculoId,
            @RequestParam(name = "motorista_id", required = false) Integer motoristaId
    ) {
        return service.viagens(dataIni, dataFim, veiculoId, motoristaId);
    }

    @GetMapping("/custos-veiculo")
    public List<Map<String, Object>> custosVeiculo(
            @RequestParam(name = "veiculo_id", required = false) Integer veiculoId
    ) {
        return service.custos(veiculoId);
    }

    @GetMapping("/cnhs-a-vencer")
    public List<Map<String, Object>> cnhs(
            @RequestParam(name = "ate") String ate
    ) {
        return service.cnhsAVencer(ate);
    }

    @GetMapping("/abastecimentos")
    public Map<String, Object> abastecimentos(
            @RequestParam(name = "veiculo_id", required = false) Integer veiculoId,
            @RequestParam(name = "data_ini", required = false) String dataIni,
            @RequestParam(name = "data_fim", required = false) String dataFim
    ) {
        return service.abastecimentos(veiculoId, dataIni, dataFim);
    }

    @GetMapping("/manutencoes")
    public Map<String, Object> manutencoes(
            @RequestParam(name = "veiculo_id", required = false) Integer veiculoId,
            @RequestParam(name = "data_ini", required = false) String dataIni,
            @RequestParam(name = "data_fim", required = false) String dataFim
    ) {
        return service.manutencoes(veiculoId, dataIni, dataFim);
    }
}

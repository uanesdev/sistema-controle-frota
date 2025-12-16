package com.uast.fbd.frota.controller;

import com.uast.fbd.frota.entity.Viagem;
import com.uast.fbd.frota.service.RelatoriosService;
import com.uast.fbd.frota.service.ViagemService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/viagens")
@RequiredArgsConstructor
public class ViagemController {

    private final ViagemService service;
    private final RelatoriosService relatoriosService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Viagem criar(@RequestBody Viagem viagem) {
        return service.criar(viagem);
    }

    @GetMapping
    public List<Viagem> buscar(
            @RequestParam(required = false, name = "veiculo_id") Long veiculoId,
            @RequestParam(required = false, name = "motorista_id") Long motoristaId,
            @RequestParam(required = false) String destino,
            @RequestParam(required = false, name = "data_ini")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataIni,
            @RequestParam(required = false, name = "data_fim")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim,
            @RequestParam(required = false, name = "em_andamento") Boolean emAndamento,
            @RequestParam(required = false) Boolean ativo
    ) {
        return service.buscar(
                veiculoId,
                motoristaId,
                destino,
                dataIni,
                dataFim,
                emAndamento,
                ativo
        );
    }

    @GetMapping("/{id}")
    public Viagem buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Viagem atualizar(@PathVariable Long id, @RequestBody Viagem viagem) {
        return service.atualizar(id, viagem);
    }

    @PatchMapping("/{id}/desativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(@PathVariable Long id) {
        service.desativar(id);
    }

    @GetMapping("em-andamento")
    public List<Map<String, Object>> viagensEmAndamento() {
        return relatoriosService.viagensEmAndamento();
    }
}

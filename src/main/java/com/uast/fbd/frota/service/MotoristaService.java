package com.uast.fbd.frota.service;

import com.uast.fbd.frota.exception.RecursoNaoEncontradoException;
import com.uast.fbd.frota.exception.ValidacaoException;
import com.uast.fbd.frota.entity.Motorista;
import com.uast.fbd.frota.repository.MotoristaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MotoristaService {

    private final MotoristaRepository repository;

    public Motorista criar(Motorista motorista) {

        if (motorista.getNome() == null || motorista.getNome().isBlank()) {
            throw new ValidacaoException("Nome é obrigatório.");
        }

        if (motorista.getCnh() == null || motorista.getCnh().isBlank()) {
            throw new ValidacaoException("CNH é obrigatória.");
        }

        if (motorista.getValidadeCnh() == null) {
            throw new ValidacaoException("A validade da CNH é obrigatória.");
        }

        if (motorista.getValidadeCnh().isBefore(LocalDate.now())) {
            throw new ValidacaoException("A CNH está vencida.");
        }

        motorista.setAtivo(true);

        return repository.criarMotorista(motorista);
    }

    public Motorista buscarPorId(Integer id) {
        return repository.buscarMotoristaPorId(id);
    }


    public List<Motorista> buscar(String nome,
                                  String cnh,
                                  LocalDate validadeAte,
                                  Boolean ativo) {

        return repository.buscarMotoristas(nome, cnh, validadeAte, ativo);
    }


    public Motorista atualizar(Integer id, Motorista dados) {

        Motorista existente = buscarPorId(id);

        if (dados.getNome() != null && dados.getNome().isBlank()) {
            throw new ValidacaoException("Nome não pode ser vazio.");
        }

        if (dados.getCnh() != null && dados.getCnh().isBlank()) {
            throw new ValidacaoException("CNH não pode ser vazia.");
        }

        if (dados.getValidadeCnh() != null &&
                dados.getValidadeCnh().isBefore(LocalDate.now())) {
            throw new ValidacaoException("A validade da CNH é inválida.");
        }

        existente.setNome(dados.getNome() != null ? dados.getNome() : existente.getNome());
        existente.setCnh(dados.getCnh() != null ? dados.getCnh() : existente.getCnh());
        existente.setValidadeCnh(dados.getValidadeCnh() != null ? dados.getValidadeCnh() : existente.getValidadeCnh());

        Map<String, Object> updates = new HashMap<>();

        updates.put("nome", existente.getNome());
        updates.put("cnh", existente.getCnh());
        updates.put("validade_cnh", existente.getValidadeCnh());
        updates.put("ativo", existente.getAtivo());

        return repository.atualizarMotorista(id, updates);
    }

    public Motorista desativar(Integer id) {

        Motorista existente = buscarPorId(id);

        if (!existente.getAtivo()) {
            throw new ValidacaoException("O motorista já está desativado.");
        }

        return repository.desativarMotorista(id);
    }
}

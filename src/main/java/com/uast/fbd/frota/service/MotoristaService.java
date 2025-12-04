package com.uast.fbd.frota.service;

import com.uast.fbd.frota.entity.Motorista;
import com.uast.fbd.frota.exception.RecursoNaoEncontradoException;
import com.uast.fbd.frota.repository.MotoristaRepositorio;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class MotoristaService {

    private final MotoristaRepositorio repositorio;

    public MotoristaService(MotoristaRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public Motorista criar(Motorista motorista) {
        return repositorio.criarMotorista(motorista);
    }

    public List<Motorista> listar(String nome, String cnh, LocalDate validadeCnhAte, Boolean ativo) {
        return repositorio.listarMotoristas(
                nome,
                cnh,
                validadeCnhAte != null ? validadeCnhAte.toString() : null,
                ativo
        );
    }

    public Motorista buscarPorId(Long id) {
        try {
            return repositorio.buscarMotoristaPorId(id);
        } catch (EmptyResultDataAccessException e) {
            throw new RecursoNaoEncontradoException("Motorista com ID " + id + " não encontrado");
        }
    }

    public Motorista atualizar(Long id, Motorista motorista) {
        try {
            return repositorio.atualizarMotorista(id, motorista);
        } catch (EmptyResultDataAccessException e) {
            throw new RecursoNaoEncontradoException("Não foi possível atualizar. Motorista com ID " + id + " não encontrado");
        }
    }

    public void desativar(Long id) {
        try {
            repositorio.desativarMotorista(id);
        } catch (EmptyResultDataAccessException e) {
            throw new RecursoNaoEncontradoException("Não foi possível desativar. Motorista com ID " + id + " não encontrado");
        }
    }

    public List<Map<String, Object>> cnhsAVencer(LocalDate ate) {
        return repositorio.listarCnhsAVencer(ate.toString());
    }
}

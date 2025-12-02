package com.uast.fbd.frota.service;

import com.uast.fbd.frota.entity.Motorista;
import com.uast.fbd.frota.repository.MotoristaRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class MotoristaService {

    private final MotoristaRepositorio repositorio;

    public MotoristaService(MotoristaRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Transactional
    public Motorista criar(Motorista motorista) {
        return repositorio.criarMotorista(
                motorista.getNome(),
                motorista.getCnh(),
                motorista.getValidadeCnh().toString()
        );
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
        return repositorio.buscarMotoristaPorId(id);
    }

    @Transactional
    public Motorista atualizar(Long id, Motorista motorista) {
        return repositorio.atualizarMotorista(
                id,
                motorista.getNome(),
                motorista.getCnh(),
                motorista.getValidadeCnh() != null ? motorista.getValidadeCnh().toString() : null
        );
    }

    @Transactional
    public void desativar(Long id) {
        repositorio.desativarMotorista(id);
    }

    public List<Map<String, Object>> cnhsAVencer(LocalDate ate) {
        return repositorio.listarCnhsAVencer(ate.toString());
    }
}

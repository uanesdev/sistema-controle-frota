package com.uast.fbd.frota.repository;

import com.uast.fbd.frota.entity.Motorista;
import com.uast.fbd.frota.sql.MotoristaSQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface MotoristaRepositorio extends JpaRepository<Motorista, Long> {

    @Query(nativeQuery = true, value = MotoristaSQL.QUERY_LISTAR_MOTORISTAS)
    List<Motorista> listarMotoristas(
            @Param("nome") String nome,
            @Param("cnh") String cnh,
            @Param("validade_cnh_ate") String validadeCnhAte,
            @Param("ativo") Boolean ativo
    );

    @Query(nativeQuery = true, value = MotoristaSQL.QUERY_BUSCAR_MOTORISTA_POR_ID)
    Motorista buscarMotoristaPorId(@Param("id") Long id);

    @Query(nativeQuery = true, value = MotoristaSQL.QUERY_CRIAR_MOTORISTA)
    Motorista criarMotorista(
            @Param("nome") String nome,
            @Param("cnh") String cnh,
            @Param("validade_cnh") String validadeCnh
    );

    @Query(nativeQuery = true, value = MotoristaSQL.QUERY_ATUALIZAR_MOTORISTA)
    Motorista atualizarMotorista(
            @Param("id") Long id,
            @Param("nome") String nome,
            @Param("cnh") String cnh,
            @Param("validade_cnh") String validadeCnh
    );

    @Query(nativeQuery = true, value = MotoristaSQL.QUERY_DESATIVAR_MOTORISTA)
    Motorista desativarMotorista(@Param("id") Long id);

    @Query(nativeQuery = true, value = MotoristaSQL.QUERY_CNH_A_VENCER)
    List<Map<String, Object>> listarCnhsAVencer(@Param("ate") String ate);
}

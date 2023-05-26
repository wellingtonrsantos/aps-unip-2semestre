package com.unip.aps.repository;

import com.unip.aps.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Integer> {
    List<Servico> findByPrestadorIsNullAndConcluidoIsFalse();
    List<Servico> findByContratanteId(Integer contratanteId);
    List<Servico> findByPrestadorId(Integer prestadorId);

    @Modifying
    @Query("update Servico s set s.concluido = ?1 where s.id = ?2")
    int updateConcluidoById(boolean concluido, Integer id);
}
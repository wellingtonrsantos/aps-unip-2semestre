package com.unip.aps.service;

import com.unip.aps.model.Servico;
import com.unip.aps.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ServicoService {
    private final ServicoRepository servicoRepository;

    @Autowired
    public ServicoService(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    public Servico cadastrar(Servico servico) {
        return servicoRepository.save(servico);
    }

    public Servico buscarPorId(Integer servicoId) {
        return this.servicoRepository.findById(servicoId).orElse(null);
    }

    public List<Servico> buscarServicosDisponiveis() {
        return this.servicoRepository.findByPrestadorIsNullAndConcluidoIsFalse();
    }

    public List<Servico> obterServicosPorContratante(int id) {
        return this.servicoRepository.findByContratanteId(id);
    }

    public List<Servico> obterServicosPorPrestador(int id) {
        return this.servicoRepository.findByPrestadorId(id);
    }

    @Transactional
    public void atualizarStatusServico(boolean concluido, Integer servicoId) {
        this.servicoRepository.updateConcluidoById(concluido, servicoId);
    }
}
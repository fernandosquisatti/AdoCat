package com.adocat.controller;

import com.adocat.model.Assinatura;
import com.adocat.model.Cobranca;
import com.adocat.repository.AssinaturaRepository;
import com.adocat.repository.CobrancaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

/** Histórico e simulação de cobranças das assinaturas (somente admin). */
@RestController
@RequestMapping("/api/assinaturas")
public class CobrancaController {

    private final AssinaturaRepository assinaturaRepository;
    private final CobrancaRepository cobrancaRepository;

    public CobrancaController(AssinaturaRepository assinaturaRepository, CobrancaRepository cobrancaRepository) {
        this.assinaturaRepository = assinaturaRepository;
        this.cobrancaRepository = cobrancaRepository;
    }

    @GetMapping("/{id}/cobrancas")
    public List<Cobranca> listarCobrancas(@PathVariable Long id) {
        return cobrancaRepository.findByAssinaturaIdOrderByDataCobrancaDesc(id);
    }

    @PostMapping("/{id}/cobrancas/simular")
    @Transactional
    public Cobranca simularCobranca(@PathVariable Long id) {
        Assinatura assinatura = assinaturaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Assinatura não encontrada."));
        if (!"ATIVA".equals(assinatura.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A assinatura precisa estar ativa.");
        }

        Cobranca cobranca = new Cobranca();
        cobranca.setAssinatura(assinatura);
        cobranca.setValor(assinatura.getValorMensal());
        cobranca.setDataCobranca(LocalDate.now());
        cobranca.setStatus("APROVADA");
        Cobranca salva = cobrancaRepository.save(cobranca);

        LocalDate base = assinatura.getProximaCobranca() != null ? assinatura.getProximaCobranca() : LocalDate.now();
        assinatura.setProximaCobranca(base.plusMonths(1));
        assinaturaRepository.save(assinatura);
        return salva;
    }
}

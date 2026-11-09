
package com.adocat.controller;

import com.adocat.model.Assinatura;
import com.adocat.model.Cobranca;
import com.adocat.repository.AssinaturaRepository;
import com.adocat.repository.CobrancaRepository;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/assinaturas")
@CrossOrigin(origins = "*")
public class CobrancaController {

    private final AssinaturaRepository assinaturaRepository;
    private final CobrancaRepository cobrancaRepository;

    public CobrancaController(
            AssinaturaRepository assinaturaRepository,
            CobrancaRepository cobrancaRepository) {

        this.assinaturaRepository = assinaturaRepository;
        this.cobrancaRepository = cobrancaRepository;
    }

    // Consultar histórico de cobranças
    @GetMapping("/{id}/cobrancas")
    public List<Cobranca> listarCobrancas(@PathVariable Long id) {

        return cobrancaRepository
                .findByAssinaturaIdOrderByDataCobrancaDesc(id);
    }

    // Simular cobrança mensal antecipada
    @PostMapping("/{id}/cobrancas/simular")
    public Cobranca simularCobranca(@PathVariable Long id) {

        Assinatura assinatura = assinaturaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Assinatura não encontrada"));

        if (!"ATIVA".equals(assinatura.getStatus())) {
            throw new IllegalStateException(
                    "A assinatura precisa estar ativa.");
        }

        Cobranca cobranca = new Cobranca();
        cobranca.setAssinatura(assinatura);
        cobranca.setValor(assinatura.getValorMensal());
        cobranca.setDataCobranca(LocalDate.now());
        cobranca.setStatus("APROVADA");

        Cobranca salva = cobrancaRepository.save(cobranca);

        assinatura.setProximaCobranca(
                assinatura.getProximaCobranca().plusMonths(1));

        assinaturaRepository.save(assinatura);

        return salva;
    }
}

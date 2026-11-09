
package com.adocat.controller;

import com.adocat.model.Assinatura;
import com.adocat.repository.AssinaturaRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/assinaturas")
@CrossOrigin(origins = "*")
public class AssinaturaController {

    private final AssinaturaRepository assinaturaRepository;

    public AssinaturaController(AssinaturaRepository assinaturaRepository) {
        this.assinaturaRepository = assinaturaRepository;
    }

    @GetMapping
    public List<Assinatura> listar() {
        return assinaturaRepository.findAll();
    }

    @PostMapping
    public Assinatura criar(@RequestBody Assinatura assinatura) {

        if (!"CARTAO".equalsIgnoreCase(assinatura.getFormaPagamento())) {
            throw new IllegalArgumentException(
                    "Assinaturas mensais aceitam somente cartão de crédito."
            );
        }

        assinatura.setFormaPagamento("CARTAO");
        assinatura.setStatus("ATIVA");
        assinatura.setDataInicio(LocalDate.now());
        assinatura.setProximaCobranca(LocalDate.now().plusMonths(1));

        return assinaturaRepository.save(assinatura);
    }

    @PutMapping("/{id}/aprovar")
    public Assinatura aprovar(@PathVariable Long id) {
        Assinatura assinatura = assinaturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assinatura não encontrada"));

        assinatura.setStatus("ATIVA");
        return assinaturaRepository.save(assinatura);
    }

    @PutMapping("/{id}/recusar")
    public Assinatura recusar(@PathVariable Long id) {
        Assinatura assinatura = assinaturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assinatura não encontrada"));
        assinatura.setStatus("RECUSADA");
        return assinaturaRepository.save(assinatura);
    }

    @PutMapping("/{id}/cancelar")
    public Assinatura cancelar(@PathVariable Long id) {
        Assinatura assinatura = assinaturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assinatura não encontrada"));

        assinatura.setStatus("CANCELADA");
        return assinaturaRepository.save(assinatura);
    }
}

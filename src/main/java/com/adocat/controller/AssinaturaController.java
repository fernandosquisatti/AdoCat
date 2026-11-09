package com.adocat.controller;

import com.adocat.model.Assinatura;
import com.adocat.repository.AssinaturaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

/** Assinaturas mensais SIMULADAS (versão de teste): nenhum valor é cobrado. */
@RestController
@RequestMapping("/api/assinaturas")
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
    @ResponseStatus(HttpStatus.CREATED)
    public Assinatura criar(@RequestBody Assinatura recebida) {
        if (!"CARTAO".equalsIgnoreCase(recebida.getFormaPagamento())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assinaturas mensais aceitam somente cartão de crédito.");
        }
        String nome = recebida.getNomeApoiador() == null ? "" : recebida.getNomeApoiador().trim();
        if (nome.isEmpty() || nome.length() > 120) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe um nome válido.");
        }
        String email = recebida.getEmail() == null ? "" : recebida.getEmail().trim().toLowerCase(Locale.ROOT);
        if (email.length() > 255 || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe um e-mail válido.");
        }
        Double valor = recebida.getValorMensal();
        if (valor == null || valor.isNaN() || valor < 1 || valor > PagamentoController.VALOR_MAXIMO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe um valor mensal entre R$ 1,00 e R$ 10.000,00.");
        }

        // Objeto novo: o cliente não define status, datas nem id.
        Assinatura assinatura = new Assinatura();
        assinatura.setNomeApoiador(nome);
        assinatura.setEmail(email);
        assinatura.setValorMensal(Math.round(valor * 100) / 100.0);
        assinatura.setFormaPagamento("CARTAO");
        assinatura.setStatus("ATIVA");
        assinatura.setDataInicio(LocalDate.now());
        assinatura.setProximaCobranca(LocalDate.now().plusMonths(1));
        return assinaturaRepository.save(assinatura);
    }

    @PutMapping("/{id}/aprovar")
    public Assinatura aprovar(@PathVariable Long id) {
        return alterarStatus(id, "ATIVA");
    }

    @PutMapping("/{id}/recusar")
    public Assinatura recusar(@PathVariable Long id) {
        return alterarStatus(id, "RECUSADA");
    }

    @PutMapping("/{id}/cancelar")
    public Assinatura cancelar(@PathVariable Long id) {
        return alterarStatus(id, "CANCELADA");
    }

    private Assinatura alterarStatus(Long id, String status) {
        Assinatura assinatura = assinaturaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Assinatura não encontrada."));
        assinatura.setStatus(status);
        return assinaturaRepository.save(assinatura);
    }
}

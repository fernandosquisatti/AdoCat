package com.adocat.controller;

import com.adocat.model.Pagamento;
import com.adocat.repository.PagamentoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Pagamentos SIMULADOS (versão de teste): nenhum valor é cobrado.
 * O registro público entra como APROVADO para alimentar o painel, mas os
 * campos são validados e montados no servidor, sem confiar no JSON recebido.
 */
@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {

    static final double VALOR_MAXIMO = 10_000.0;
    private static final Set<String> FORMAS = Set.of("PIX", "CARTAO");

    private final PagamentoRepository pagamentoRepository;

    public PagamentoController(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    @GetMapping
    public List<Pagamento> listar() {
        return pagamentoRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pagamento criar(@RequestBody Pagamento recebido) {
        String nome = recebido.getNomePagador() == null ? "" : recebido.getNomePagador().trim();
        if (nome.isEmpty() || nome.length() > 120) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe um nome válido.");
        }
        Double valor = recebido.getValor();
        if (valor == null || valor.isNaN() || valor < 1 || valor > VALOR_MAXIMO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe um valor entre R$ 1,00 e R$ 10.000,00.");
        }
        String forma = recebido.getFormaPagamento() == null ? "" : recebido.getFormaPagamento().trim().toUpperCase(Locale.ROOT);
        if (!FORMAS.contains(forma)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Forma de pagamento inválida.");
        }

        // Objeto novo: impede que um "id" no JSON sobrescreva um pagamento existente.
        Pagamento pagamento = new Pagamento();
        pagamento.setNomePagador(nome);
        pagamento.setValor(Math.round(valor * 100) / 100.0);
        pagamento.setFormaPagamento(forma);
        pagamento.setStatus("APROVADO");
        pagamento.setDataPagamento(LocalDateTime.now());
        return pagamentoRepository.save(pagamento);
    }

    @PutMapping("/{id}/aprovar")
    public Pagamento aprovar(@PathVariable Long id) {
        Pagamento pagamento = buscar(id);
        pagamento.setStatus("APROVADO");
        return pagamentoRepository.save(pagamento);
    }

    @PutMapping("/{id}/recusar")
    public Pagamento recusar(@PathVariable Long id) {
        Pagamento pagamento = buscar(id);
        pagamento.setStatus("RECUSADO");
        return pagamentoRepository.save(pagamento);
    }

    private Pagamento buscar(Long id) {
        return pagamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pagamento não encontrado."));
    }
}

package com.adocat.controller;

import com.adocat.model.Pagamento;
import com.adocat.repository.PagamentoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagamentos")
@CrossOrigin(origins = "*")
public class PagamentoController {

    private final PagamentoRepository pagamentoRepository;

    public PagamentoController(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    @GetMapping
    public List<Pagamento> listar() {
        return pagamentoRepository.findAll();
    }

    @PostMapping
    public Pagamento criar(@RequestBody Pagamento pagamento) {
        pagamento.setStatus("APROVADO");
        return pagamentoRepository.save(pagamento);
    }

    @PutMapping("/{id}/aprovar")
    public Pagamento aprovar(@PathVariable Long id) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));

        pagamento.setStatus("APROVADO");
        return pagamentoRepository.save(pagamento);
    }

    @PutMapping("/{id}/recusar")
    public Pagamento recusar(@PathVariable Long id) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));

        pagamento.setStatus("RECUSADO");
        return pagamentoRepository.save(pagamento);
    }
}
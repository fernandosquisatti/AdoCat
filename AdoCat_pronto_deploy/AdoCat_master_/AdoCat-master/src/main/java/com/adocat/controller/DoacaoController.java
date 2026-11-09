package com.adocat.controller;

import com.adocat.model.Doacao;
import com.adocat.repository.DoacaoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doacoes")
public class DoacaoController {

    private final DoacaoRepository doacaoRepository;

    public DoacaoController(DoacaoRepository doacaoRepository) {
        this.doacaoRepository = doacaoRepository;
    }

    @GetMapping
    public List<Doacao> listarTodas() {
        return doacaoRepository.findAll();
    }

    @PostMapping
    public Doacao registrar(@RequestBody Doacao doacao) {
        return doacaoRepository.save(doacao);
    }
}
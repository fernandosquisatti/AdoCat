package com.adocat.controller;

import com.adocat.model.Gato;
import com.adocat.repository.GatoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/gatos")
public class GatoController {
    private final GatoRepository repository;

    public GatoController(GatoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Gato> listarGatos() {
        return repository.findAll();
    }

    // POST protegido pela SecurityConfig. Ignora qualquer ID enviado no JSON.
    @PostMapping
    public Gato cadastrarGato(@RequestBody Gato gato) {
        if (gato.getNome() == null || gato.getNome().isBlank() || gato.getNome().length() > 255
                || gato.getIdade() < 0 || gato.getIdade() > 40
                || gato.getStatus() == null || gato.getStatus().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados do gato inválidos");
        }
        gato.setId(null);
        gato.setNome(gato.getNome().trim());
        return repository.save(gato);
    }
}

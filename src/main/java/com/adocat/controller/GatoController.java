package com.adocat.controller;

import com.adocat.model.Gato;
import com.adocat.repository.GatoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gatos")
public class GatoController {

    private final GatoRepository gatoRepository;

    public GatoController(GatoRepository gatoRepository) {
        this.gatoRepository = gatoRepository;
    }

    /** Público: catálogo de gatos. */
    @GetMapping
    public List<Gato> listarGatos() {
        return gatoRepository.findAll();
    }

    /** Somente admin (protegido no SecurityConfig). */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Gato cadastrarGato(@RequestBody Gato gato) {
        GatoAdminController.validar(gato);
        gato.setId(null); // impede sobrescrever um gato existente
        gato.setNome(gato.getNome().trim());
        return gatoRepository.save(gato);
    }
}

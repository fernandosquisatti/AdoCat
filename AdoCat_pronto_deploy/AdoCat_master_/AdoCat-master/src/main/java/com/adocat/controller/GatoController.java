package com.adocat.controller;

import com.adocat.model.Gato;
import com.adocat.repository.GatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gatos")
@CrossOrigin(origins = "*")
public class GatoController {

    @Autowired
    private GatoRepository gatoRepository;

    @GetMapping
    public List<Gato> listarGatos() {
        return gatoRepository.findAll();
    }

    @PostMapping
    public Gato cadastrarGato(@RequestBody Gato gato) {
        return gatoRepository.save(gato);
    }
}
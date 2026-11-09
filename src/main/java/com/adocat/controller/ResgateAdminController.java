package com.adocat.controller;

import com.adocat.model.Resgate;
import com.adocat.repository.ResgateRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/admin/resgates")
public class ResgateAdminController {

    private final ResgateRepository repository;

    public ResgateAdminController(ResgateRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Resgate> listar() {
        return repository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Resgate criar(@RequestBody Resgate r) {
        validar(r);
        r.setId(null); // impede sobrescrever um resgate existente
        return repository.save(r);
    }

    @PutMapping("/{id}")
    public Resgate atualizar(@PathVariable Long id, @RequestBody Resgate r) {
        validar(r);
        Resgate a = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resgate não encontrado"));
        a.setNomeAnimal(r.getNomeAnimal());
        a.setDataResgate(r.getDataResgate());
        a.setLocalizacao(r.getLocalizacao());
        a.setDescricao(r.getDescricao());
        a.setCondicaoAnimal(r.getCondicaoAnimal());
        a.setResponsavel(r.getResponsavel());
        a.setStatus(r.getStatus());
        return repository.save(a);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resgate não encontrado");
        }
        repository.deleteById(id);
    }

    private void validar(Resgate r) {
        if (r.getNomeAnimal() == null || r.getNomeAnimal().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o animal");
        }
        if (r.getDataResgate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe a data");
        }
        if (r.getLocalizacao() == null || r.getLocalizacao().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe a localização");
        }
        if (r.getDescricao() != null && r.getDescricao().length() > 1600) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Descrição muito longa (máx. 1600 caracteres)");
        }
        if (r.getStatus() == null || r.getStatus().isBlank()) {
            r.setStatus("REGISTRADO");
        }
    }
}

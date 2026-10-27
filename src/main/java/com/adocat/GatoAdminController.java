package com.adocat.controller;

import com.adocat.model.Gato;
import com.adocat.repository.GatoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/admin/gatos")
public class GatoAdminController {
    private final GatoRepository repository;

    public GatoAdminController(GatoRepository repository) {
        this.repository = repository;
    }

    @PutMapping("/{id}")
    public Gato atualizar(@PathVariable Long id, @RequestBody Gato recebido) {
        validar(recebido);
        Gato gato = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gato não encontrado"));
        gato.setNome(recebido.getNome().trim());
        gato.setIdade(recebido.getIdade());
        gato.setSexo(recebido.getSexo());
        gato.setRacaOuCor(recebido.getRacaOuCor());
        gato.setCastrado(recebido.isCastrado());
        gato.setStatus(recebido.getStatus());
        return repository.save(gato);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gato não encontrado");
        }
        repository.deleteById(id);
    }

    private void validar(Gato gato) {
        if (gato.getNome() == null || gato.getNome().isBlank() || gato.getNome().length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe um nome válido");
        }
        if (gato.getIdade() < 0 || gato.getIdade() > 40) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe uma idade válida");
        }
        if (gato.getStatus() == null || gato.getStatus().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o status");
        }
    }
}

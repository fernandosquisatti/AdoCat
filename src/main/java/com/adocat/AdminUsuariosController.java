package com.adocat.controller;

import com.adocat.model.Usuario;
import com.adocat.repository.UsuarioRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/admin/usuarios")
public class AdminUsuariosController {
    private final UsuarioRepository repository;

    public AdminUsuariosController(UsuarioRepository repository) {
        this.repository = repository;
    }

    // Nunca devolve o campo 'senha' do modelo legado.
    @GetMapping
    public List<UsuarioResumo> listar() {
        return repository.findAll().stream()
                .map(u -> new UsuarioResumo(u.getId(), u.getNome(), u.getEmail(), u.getTipo()))
                .toList();
    }

    public record UsuarioResumo(Long id, String nome, String email, String tipo) {}
}

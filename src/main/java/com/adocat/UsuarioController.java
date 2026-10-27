package com.adocat.controller;

import com.adocat.model.Usuario;
import com.adocat.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;

    public UsuarioController(UsuarioRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @GetMapping
    public List<UsuarioResumo> listarTodos() {
        return repository.findAll().stream()
                .map(u -> new UsuarioResumo(u.getId(), u.getNome(), u.getEmail(), u.getTipo()))
                .toList();
    }

    @PostMapping
    public UsuarioResumo criar(@RequestBody Usuario usuario) {
        if (usuario.getEmail() == null || usuario.getEmail().isBlank()
                || usuario.getSenha() == null || usuario.getSenha().length() < 12
                || usuario.getNome() == null || usuario.getNome().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome, e-mail e senha de 12+ caracteres obrigatórios");
        }
        usuario.setId(null);
        usuario.setSenha(encoder.encode(usuario.getSenha()));
        // Evita que o cadastro aceite papéis administrativos arbitrários.
        usuario.setTipo("VOLUNTARIO");
        Usuario salvo = repository.save(usuario);
        return new UsuarioResumo(salvo.getId(), salvo.getNome(), salvo.getEmail(), salvo.getTipo());
    }

    public record UsuarioResumo(Long id, String nome, String email, String tipo) {}
}

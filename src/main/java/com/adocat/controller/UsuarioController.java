package com.adocat.controller;

import com.adocat.model.Usuario;
import com.adocat.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

/** Endpoint legado (somente admin). Nunca devolve senha nem hash de CPF. */
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
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResumo criar(@RequestBody Usuario recebido) {
        if (recebido.getEmail() == null || recebido.getEmail().isBlank()
                || recebido.getSenha() == null || recebido.getSenha().length() < 12
                || recebido.getNome() == null || recebido.getNome().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome, e-mail e senha de 12+ caracteres obrigatórios.");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(recebido.getNome().trim());
        usuario.setEmail(recebido.getEmail().trim().toLowerCase(Locale.ROOT));
        usuario.setSenha(encoder.encode(recebido.getSenha()));
        // Evita que o cadastro aceite papéis administrativos arbitrários.
        usuario.setTipo("VOLUNTARIO");
        usuario.setStatusCadastro("PENDENTE");
        Usuario salvo = repository.save(usuario);
        return new UsuarioResumo(salvo.getId(), salvo.getNome(), salvo.getEmail(), salvo.getTipo());
    }

    public record UsuarioResumo(Long id, String nome, String email, String tipo) {}
}

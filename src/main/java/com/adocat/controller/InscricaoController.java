package com.adocat.controller;

import com.adocat.model.Usuario;
import com.adocat.repository.UsuarioRepository;
import com.adocat.util.CpfUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;
import java.util.Set;

@RestController
@RequestMapping("/api/inscricoes")
public class InscricaoController {

    private static final Logger log = LoggerFactory.getLogger(InscricaoController.class);
    private static final Set<String> TIPOS = Set.of("VOLUNTARIO", "ADOTANTE", "LAR_TEMPORARIO");

    private final UsuarioRepository repository;
    private final String segredoCpf;

    public InscricaoController(UsuarioRepository repository,
                               @Value("${APP_CPF_SECRET:}") String segredoCpf) {
        this.repository = repository;
        this.segredoCpf = segredoCpf;
        if (segredoCpf.isBlank()) {
            log.warn("APP_CPF_SECRET não definido: CPFs serão protegidos apenas com SHA-256 simples. Defina a variável fora do ambiente de teste.");
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Resultado criar(@RequestBody Inscricao dados) {
        String tipo = normalizarTipo(dados.tipo());
        if (!TIPOS.contains(tipo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria de inscrição inválida.");
        }
        String cpf = CpfUtil.somenteDigitos(dados.cpf());
        if (!CpfUtil.valido(cpf)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF inválido. Confira os números digitados.");
        }
        if (dados.nome() == null || dados.nome().trim().length() < 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe seu nome completo.");
        }
        if (dados.email() == null || dados.email().length() > 255
                || !dados.email().trim().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe um e-mail válido.");
        }
        Integer capacidade = dados.capacidadeAcolhimento();
        if (capacidade != null && (capacidade < 0 || capacidade > 50)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Capacidade de acolhimento inválida.");
        }

        String hash = CpfUtil.hash(cpf, segredoCpf);
        if (repository.existsByCpfHashAndTipo(hash, tipo)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma inscrição nesta categoria para este CPF.");
        }

        Usuario u = new Usuario();
        u.setNome(limitar(dados.nome(), 255));
        u.setEmail(dados.email().trim().toLowerCase(Locale.ROOT));
        u.setTipo(tipo);
        u.setCpfHash(hash);
        u.setCpfFinal(cpf.substring(cpf.length() - 4));
        u.setTelefone(limitar(dados.telefone(), 40));
        u.setEndereco(limitar(dados.endereco(), 255));
        u.setCidade(limitar(dados.cidade(), 120));
        u.setDisponibilidade(limitar(dados.disponibilidade(), 255));
        u.setExperiencia(limitar(dados.experiencia(), 1200));
        u.setObservacoes(limitar(dados.observacoes(), 1200));
        u.setTipoResidencia(limitar(dados.tipoResidencia(), 80));
        u.setPossuiAnimais(dados.possuiAnimais());
        u.setCapacidadeAcolhimento(capacidade);
        u.setStatusCadastro("PENDENTE");
        try {
            repository.saveAndFlush(u);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma inscrição com estes dados.");
        }
        return new Resultado("Inscrição enviada com sucesso! Ela ficará pendente de análise pela equipe.",
                u.getId(), u.getStatusCadastro());
    }

    private String normalizarTipo(String s) {
        return s == null ? "" : s.trim().toUpperCase(Locale.ROOT).replace(' ', '_');
    }

    private String limitar(String s, int max) {
        if (s == null) return null;
        s = s.trim();
        return s.length() > max ? s.substring(0, max) : s;
    }

    public record Inscricao(String nome, String email, String cpf, String tipo, String telefone,
                            String endereco, String cidade, String disponibilidade, String experiencia,
                            String observacoes, String tipoResidencia, Boolean possuiAnimais,
                            Integer capacidadeAcolhimento) {}

    public record Resultado(String mensagem, Long id, String status) {}
}

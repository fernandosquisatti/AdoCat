package com.adocat.controller;

import com.adocat.model.Usuario;
import com.adocat.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Locale;

@RestController
@RequestMapping("/api/inscricoes")
public class InscricaoController {
    private final UsuarioRepository repository;
    public InscricaoController(UsuarioRepository repository){this.repository=repository;}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Resultado criar(@RequestBody Inscricao dados) {
        String tipo=normalizarTipo(dados.tipo());
        if (!tipo.equals("VOLUNTARIO") && !tipo.equals("ADOTANTE") && !tipo.equals("LAR_TEMPORARIO"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Categoria de inscrição inválida.");
        String cpf= dados.cpf()==null ? "" : dados.cpf().replaceAll("\\D", "");
        if (!cpfValido(cpf)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"CPF inválido. Confira os números digitados.");
        if (dados.nome()==null || dados.nome().trim().length()<3) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Informe seu nome completo.");
        if (dados.email()==null || !dados.email().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Informe um e-mail válido.");
        String hash=sha256(cpf);
        if(repository.existsByCpfHashAndTipo(hash,tipo)) throw new ResponseStatusException(HttpStatus.CONFLICT,"Já existe uma inscrição nesta categoria para este CPF.");
        Usuario u=new Usuario();
        u.setNome(dados.nome().trim()); u.setEmail(dados.email().trim().toLowerCase(Locale.ROOT));
        u.setTipo(tipo); u.setCpfHash(hash); u.setCpfFinal(cpf.substring(cpf.length()-4));
        u.setTelefone(limitar(dados.telefone(),40)); u.setEndereco(limitar(dados.endereco(),255));
        u.setCidade(limitar(dados.cidade(),120)); u.setDisponibilidade(limitar(dados.disponibilidade(),255));
        u.setExperiencia(limitar(dados.experiencia(),1200)); u.setObservacoes(limitar(dados.observacoes(),1200));
        u.setTipoResidencia(limitar(dados.tipoResidencia(),80)); u.setPossuiAnimais(dados.possuiAnimais());
        u.setCapacidadeAcolhimento(dados.capacidadeAcolhimento()); u.setStatusCadastro("PENDENTE");
        try { repository.saveAndFlush(u); }
        catch (org.springframework.dao.DataIntegrityViolationException ex) { throw new ResponseStatusException(HttpStatus.CONFLICT,"Já existe uma inscrição com estes dados."); }
        return new Resultado("Inscrição enviada com sucesso! Ela ficará pendente de análise pela equipe.",u.getId(),u.getStatusCadastro());
    }
    private String normalizarTipo(String s){return s==null?"":s.trim().toUpperCase(Locale.ROOT).replace(' ','_');}
    private String limitar(String s,int max){if(s==null)return null;s=s.trim();return s.length()>max?s.substring(0,max):s;}
    private String sha256(String value){try{return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8)));}catch(Exception e){throw new IllegalStateException(e);}}
    private boolean cpfValido(String cpf){if(cpf==null||!cpf.matches("\\d{11}")||cpf.chars().distinct().count()==1)return false;int sum=0;for(int i=0;i<9;i++)sum+=(cpf.charAt(i)-'0')*(10-i);int d1=11-(sum%11);if(d1>=10)d1=0;sum=0;for(int i=0;i<10;i++)sum+=(cpf.charAt(i)-'0')*(11-i);int d2=11-(sum%11);if(d2>=10)d2=0;return d1==cpf.charAt(9)-'0'&&d2==cpf.charAt(10)-'0';}
    public record Inscricao(String nome,String email,String cpf,String tipo,String telefone,String endereco,String cidade,String disponibilidade,String experiencia,String observacoes,String tipoResidencia,Boolean possuiAnimais,Integer capacidadeAcolhimento){}
    public record Resultado(String mensagem,Long id,String status){}
}

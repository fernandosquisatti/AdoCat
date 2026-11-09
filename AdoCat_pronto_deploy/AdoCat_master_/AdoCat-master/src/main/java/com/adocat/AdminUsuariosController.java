package com.adocat;

import com.adocat.model.Usuario;
import com.adocat.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/usuarios")
public class AdminUsuariosController {
    private final UsuarioRepository repository;
    public AdminUsuariosController(UsuarioRepository repository){this.repository=repository;}
    @GetMapping public List<UsuarioResumo> listar(){return repository.findAll().stream().filter(u -> u.getTipo() == null || !u.getTipo().trim().equalsIgnoreCase("ADMIN")).map(u->new UsuarioResumo(u.getId(),u.getNome(),u.getEmail(),u.getTipo(),u.getCpfFinal()==null?null:"***.***.***-"+u.getCpfFinal(),u.getTelefone(),u.getEndereco(),u.getCidade(),u.getDisponibilidade(),u.getExperiencia(),u.getObservacoes(),u.getTipoResidencia(),u.getPossuiAnimais(),u.getCapacidadeAcolhimento(),u.getStatusCadastro())).toList();}
    @PutMapping("/{id}/status") public UsuarioResumo atualizarStatus(@PathVariable Long id,@RequestBody StatusRequest req){if(req.status()==null||!List.of("PENDENTE","APROVADO","REPROVADO").contains(req.status().toUpperCase()))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Status inválido.");Usuario u=repository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Inscrição não encontrada."));u.setStatusCadastro(req.status().toUpperCase());repository.save(u);return new UsuarioResumo(u.getId(),u.getNome(),u.getEmail(),u.getTipo(),u.getCpfFinal()==null?null:"***.***.***-"+u.getCpfFinal(),u.getTelefone(),u.getEndereco(),u.getCidade(),u.getDisponibilidade(),u.getExperiencia(),u.getObservacoes(),u.getTipoResidencia(),u.getPossuiAnimais(),u.getCapacidadeAcolhimento(),u.getStatusCadastro());}
    public record StatusRequest(String status){}
    public record UsuarioResumo(Long id,String nome,String email,String tipo,String cpf,String telefone,String endereco,String cidade,String disponibilidade,String experiencia,String observacoes,String tipoResidencia,Boolean possuiAnimais,Integer capacidadeAcolhimento,String statusCadastro){}
}

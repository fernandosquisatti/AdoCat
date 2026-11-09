package com.adocat.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_usuarios", uniqueConstraints = {
        @UniqueConstraint(name = "uk_usuario_cpf_categoria", columnNames = {"cpf_hash", "tipo"})
})
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @Column(unique = true, nullable = false)
    private String email;
    // Mantido para compatibilidade com registros antigos; novos cadastros públicos não usam senha.
    private String senha;
    private String tipo;
    @Column(name = "cpf_hash", length = 64)
    private String cpfHash;
    @Column(name = "cpf_final", length = 4)
    private String cpfFinal;
    private String telefone;
    private String endereco;
    private String cidade;
    private String disponibilidade;
    @Column(length = 1200)
    private String experiencia;
    @Column(length = 1200)
    private String observacoes;
    private String tipoResidencia;
    private Boolean possuiAnimais;
    private Integer capacidadeAcolhimento;
    private String statusCadastro;

    public Usuario() {}
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getNome(){return nome;} public void setNome(String v){nome=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getSenha(){return senha;} public void setSenha(String v){senha=v;}
    public String getTipo(){return tipo;} public void setTipo(String v){tipo=v;}
    public String getCpfHash(){return cpfHash;} public void setCpfHash(String v){cpfHash=v;}
    public String getCpfFinal(){return cpfFinal;} public void setCpfFinal(String v){cpfFinal=v;}
    public String getTelefone(){return telefone;} public void setTelefone(String v){telefone=v;}
    public String getEndereco(){return endereco;} public void setEndereco(String v){endereco=v;}
    public String getCidade(){return cidade;} public void setCidade(String v){cidade=v;}
    public String getDisponibilidade(){return disponibilidade;} public void setDisponibilidade(String v){disponibilidade=v;}
    public String getExperiencia(){return experiencia;} public void setExperiencia(String v){experiencia=v;}
    public String getObservacoes(){return observacoes;} public void setObservacoes(String v){observacoes=v;}
    public String getTipoResidencia(){return tipoResidencia;} public void setTipoResidencia(String v){tipoResidencia=v;}
    public Boolean getPossuiAnimais(){return possuiAnimais;} public void setPossuiAnimais(Boolean v){possuiAnimais=v;}
    public Integer getCapacidadeAcolhimento(){return capacidadeAcolhimento;} public void setCapacidadeAcolhimento(Integer v){capacidadeAcolhimento=v;}
    public String getStatusCadastro(){return statusCadastro;} public void setStatusCadastro(String v){statusCadastro=v;}
}

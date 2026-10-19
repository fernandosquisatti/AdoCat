package com.adocat.model;

import jakarta.persistence.*;

@Entity
@Table(name = "gatos")
public class Gato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private int idade;
    private String sexo;
    private String racaOuCor;
    private boolean castrado;
    private String status;

    public Gato() {}

    public Gato(String nome, int idade, String sexo, String racaOuCor, boolean castrado, String status) {
        this.nome = nome;
        this.idade = idade;
        this.sexo = sexo;
        this.racaOuCor = racaOuCor;
        this.castrado = castrado;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public int getIdade() { return idade; }
    public void setIdade(int idade) { this.idade = idade; }
    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    public String getRacaOuCor() { return racaOuCor; }
    public void setRacaOuCor(String racaOuCor) { this.racaOuCor = racaOuCor; }
    public boolean isCastrado() { return castrado; }
    public void setCastrado(boolean castrado) { this.castrado = castrado; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
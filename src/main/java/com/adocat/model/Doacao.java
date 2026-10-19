package com.adocat.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tb_doacoes")
public class Doacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double valor;
    private String nomeDoador;
    private LocalDate dataDoacao = LocalDate.now();

    public Doacao() {}

    public Doacao(Long id, Double valor, String nomeDoador, LocalDate dataDoacao) {
        this.id = id;
        this.valor = valor;
        this.nomeDoador = nomeDoador;
        this.dataDoacao = dataDoacao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }
    public String getNomeDoador() { return nomeDoador; }
    public void setNomeDoador(String nomeDoador) { this.nomeDoador = nomeDoador; }
    public LocalDate getDataDoacao() { return dataDoacao; }
    public void setDataDoacao(LocalDate dataDoacao) { this.dataDoacao = dataDoacao; }
}
package com.adocat.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_pagamentos")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomePagador;
    private Double valor;
    private String formaPagamento;
    private String status;
    private LocalDateTime dataPagamento = LocalDateTime.now();

    public Pagamento() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomePagador() { return nomePagador; }
    public void setNomePagador(String nomePagador) {
        this.nomePagador = nomePagador;
    }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }

    public String getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }
}
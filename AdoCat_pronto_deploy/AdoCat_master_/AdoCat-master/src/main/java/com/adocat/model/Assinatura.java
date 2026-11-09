
package com.adocat.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tb_assinaturas")
public class Assinatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeApoiador;
    private String email;
    private Double valorMensal;
    private String formaPagamento;
    private String status;

    private LocalDate dataInicio;
    private LocalDate proximaCobranca;

    public Assinatura() {
    }

    public Long getId() {
        return id;
    }

    public String getNomeApoiador() {
        return nomeApoiador;
    }

    public void setNomeApoiador(String nomeApoiador) {
        this.nomeApoiador = nomeApoiador;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getValorMensal() {
        return valorMensal;
    }

    public void setValorMensal(Double valorMensal) {
        this.valorMensal = valorMensal;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getProximaCobranca() {
        return proximaCobranca;
    }

    public void setProximaCobranca(LocalDate proximaCobranca) {
        this.proximaCobranca = proximaCobranca;
    }
}

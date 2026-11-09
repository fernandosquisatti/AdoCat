
package com.adocat.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "tb_cobrancas")
public class Cobranca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "assinatura_id", nullable = false)
    private Assinatura assinatura;

    private Double valor;
    private LocalDate dataCobranca;
    private String status;

    public Cobranca() {
    }

    public Long getId() {
        return id;
    }

    public Assinatura getAssinatura() {
        return assinatura;
    }

    public void setAssinatura(Assinatura assinatura) {
        this.assinatura = assinatura;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public LocalDate getDataCobranca() {
        return dataCobranca;
    }

    public void setDataCobranca(LocalDate dataCobranca) {
        this.dataCobranca = dataCobranca;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

package com.adocat.model;
import jakarta.persistence.*;
import java.time.LocalDate;
@Entity @Table(name = "tb_resgates")
public class Resgate {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 private String nomeAnimal; private LocalDate dataResgate; private String localizacao;
 @Column(length=1600) private String descricao; private String condicaoAnimal; private String responsavel; private String status;
 public Long getId(){return id;} public void setId(Long v){id=v;}
 public String getNomeAnimal(){return nomeAnimal;} public void setNomeAnimal(String v){nomeAnimal=v;}
 public LocalDate getDataResgate(){return dataResgate;} public void setDataResgate(LocalDate v){dataResgate=v;}
 public String getLocalizacao(){return localizacao;} public void setLocalizacao(String v){localizacao=v;}
 public String getDescricao(){return descricao;} public void setDescricao(String v){descricao=v;}
 public String getCondicaoAnimal(){return condicaoAnimal;} public void setCondicaoAnimal(String v){condicaoAnimal=v;}
 public String getResponsavel(){return responsavel;} public void setResponsavel(String v){responsavel=v;}
 public String getStatus(){return status;} public void setStatus(String v){status=v;}
}

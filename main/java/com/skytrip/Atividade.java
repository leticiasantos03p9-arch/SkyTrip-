package com.skytrip;
import jakarta.persistence.*;
@Entity @Table(name="atividades")
public class Atividade {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @ManyToOne(optional=false) public Viagem viagem;
 @Column(nullable=false) public String descricao;
 public String data;
 public Atividade(){}
}
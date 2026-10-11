package com.skytrip;
import jakarta.persistence.*;
@Entity @Table(name="pessoas")
public class Pessoa {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(nullable=false) public String nome;
 @Column(nullable=false, unique=true) public String email;
 @Column(nullable=false) public String senhaHash;
 public Pessoa(){}
 public Pessoa(String nome,String email,String senhaHash){this.nome=nome;this.email=email;this.senhaHash=senhaHash;}
}
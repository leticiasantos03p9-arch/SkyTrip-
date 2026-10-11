package com.skytrip;
import jakarta.persistence.*;
import java.time.LocalDate;
@Entity @Table(name="viagens")
public class Viagem {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @ManyToOne(optional=false) public Pessoa pessoa;
 @Column(nullable=false) public String titulo;
 @Column(nullable=false) public String destino;
 public LocalDate dataInicio; public LocalDate dataFim;
 public Integer viajantes=1;
 public Viagem(){}
}
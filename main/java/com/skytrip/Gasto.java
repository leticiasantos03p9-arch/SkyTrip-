package com.skytrip;
import jakarta.persistence.*;
import java.math.BigDecimal;
@Entity @Table(name="gastos")
public class Gasto {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @ManyToOne(optional=false) public Viagem viagem;
 @Column(nullable=false) public String descricao;
 @Column(nullable=false, precision=12, scale=2) public BigDecimal valor;
 public Gasto(){}
}
package com.example.gestor.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Venda")
public class Venda {

    @ManyToOne
    @JoinColumn(name = "id_empresa")
    private Empresa empresa;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venda")
    private Long id_Venda;

    @Column(name = "data")
    private LocalDateTime data;

    @Column(name = "valor_total")
    private BigDecimal valorTotal;

    public Long getId_Venda() {
        return id_Venda;
    }

    public void setId_Venda(Long id_Venda) {
        this.id_Venda = id_Venda;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Empresa getEmpresa() {
    return empresa;
}

public void setEmpresa(Empresa empresa) {
    this.empresa = empresa;
}
}
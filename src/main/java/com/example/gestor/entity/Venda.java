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
    private Long idVenda;

    @Column(name = "data")
    private LocalDateTime data;

    @Column(name = "valor_total")
    private BigDecimal valorTotal;

    public Long getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(Long idVenda) {
        this.idVenda = idVenda;
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
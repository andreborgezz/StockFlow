package com.example.gestor.dto;

import java.math.BigDecimal;

public class ResumoVendasDto {

    private BigDecimal totalVendido;
    private long numeroVendas;
    private BigDecimal ticketMedio;

    public ResumoVendasDto(BigDecimal totalVendido, long numeroVendas, BigDecimal ticketMedio) {
        this.totalVendido = totalVendido;
        this.numeroVendas = numeroVendas;
        this.ticketMedio = ticketMedio;
    }

    public BigDecimal getTotalVendido() {
        return totalVendido;
    }

    public long getNumeroVendas() {
        return numeroVendas;
    }

    public BigDecimal getTicketMedio() {
        return ticketMedio;
    }
}

package com.example.gestor.dto;

import java.math.BigDecimal;

public class ItemVendaResumoDto {

    private Long idItemVenda;
    private Long idProduto;
    private String nomeProduto;
    private Integer quantidade;
    private BigDecimal precoUnitario;

    public ItemVendaResumoDto(Long idItemVenda,
                              Long idProduto,
                              String nomeProduto,
                              Integer quantidade,
                              BigDecimal precoUnitario) {
        this.idItemVenda = idItemVenda;
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public Long getIdItemVenda() {
        return idItemVenda;
    }

    public Long getIdProduto() {
        return idProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }
}

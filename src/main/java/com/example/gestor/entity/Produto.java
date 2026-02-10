package com.example.gestor.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Produto")
public class Produto {

    @ManyToOne
    @JoinColumn(name = "id_empresa")
    private Empresa empresa;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produto")
    private Long idProduto;

    @Column(name = "nome_produto")
    private String nomeProduto;

    @Column(name = "preco")
    private BigDecimal preco;

    @Column(name = "qtd_estoque")
    private int qtdEstoque;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Integer getQtdEstoque() {
        return qtdEstoque;
    }

    public void setQtdEstoque(int qtdEstoque) {
        this.qtdEstoque = qtdEstoque;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Empresa getEmpresa() {
    return empresa;
}

public void setEmpresa(Empresa empresa) {
    this.empresa = empresa;
}

}

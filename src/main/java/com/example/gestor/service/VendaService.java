package com.example.gestor.service;

import com.example.gestor.entity.ItemVenda;
import com.example.gestor.entity.Produto;
import com.example.gestor.entity.Venda;
import com.example.gestor.repository.ItemVendaRepository;
import com.example.gestor.repository.ProdutoRepository;
import com.example.gestor.repository.VendaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ItemVendaRepository itemVendaRepository;
    private final ProdutoRepository produtoRepository;

    public VendaService(VendaRepository vendaRepository,
                        ItemVendaRepository itemVendaRepository,
                        ProdutoRepository produtoRepository) {
        this.vendaRepository = vendaRepository;
        this.itemVendaRepository = itemVendaRepository;
        this.produtoRepository = produtoRepository;
    }

    public Venda realizarVenda(Long idEmpresa, List<ItemVenda> itens) {

        Venda venda = new Venda();
        venda.setData(LocalDateTime.now());
        venda.setValorTotal(BigDecimal.ZERO);
        venda.setEmpresa(itens.get(0).getProduto().getEmpresa());

        venda = vendaRepository.save(venda);

        BigDecimal total = BigDecimal.ZERO;

        for (ItemVenda item : itens) {

            Produto produto = produtoRepository.findById(
                    item.getProduto().getIdProduto()
            ).orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            if (!produto.getEmpresa().getIdEmpresa().equals(idEmpresa)) {
                throw new RuntimeException("Produto não pertence à empresa");
            }

            if (produto.getQtdEstoque() < item.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente");
            }

            produto.setQtdEstoque(
                    produto.getQtdEstoque() - item.getQuantidade()
            );
            produtoRepository.save(produto);

            item.setVenda(venda);
            item.setPrecoUnitario(produto.getPreco());

            BigDecimal subtotal = produto.getPreco()
                    .multiply(BigDecimal.valueOf(item.getQuantidade()));

            total = total.add(subtotal);

            itemVendaRepository.save(item);
        }

        venda.setValorTotal(total);
        return vendaRepository.save(venda);
    }

    public List<Venda> listarPorEmpresa(Long idEmpresa) {
        return vendaRepository.findByEmpresaIdEmpresa(idEmpresa);
    }
}

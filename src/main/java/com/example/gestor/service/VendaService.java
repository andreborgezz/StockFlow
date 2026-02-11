package com.example.gestor.service;

import com.example.gestor.entity.ItemVenda;
import com.example.gestor.entity.Produto;
import com.example.gestor.entity.Usuario;
import com.example.gestor.entity.Venda;
import com.example.gestor.repository.ItemVendaRepository;
import com.example.gestor.repository.ProdutoRepository;
import com.example.gestor.repository.VendaRepository;
import com.example.gestor.security.CurrentUserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.example.gestor.dto.ItemVendaResumoDto;
import com.example.gestor.dto.ResumoVendasDto;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ItemVendaRepository itemVendaRepository;
    private final ProdutoRepository produtoRepository;
    private final CurrentUserService currentUserService;

    public VendaService(VendaRepository vendaRepository,
                        ItemVendaRepository itemVendaRepository,
                        ProdutoRepository produtoRepository,
                        CurrentUserService currentUserService) {
        this.vendaRepository = vendaRepository;
        this.itemVendaRepository = itemVendaRepository;
        this.produtoRepository = produtoRepository;
        this.currentUserService = currentUserService;
    }

    public Venda realizarVenda(Long idEmpresa, List<ItemVenda> itens) {
        currentUserService.validarEmpresaAcesso(idEmpresa);
        Usuario usuario = currentUserService.getCurrentUser();

        Venda venda = new Venda();
        venda.setData(LocalDateTime.now());
        venda.setValorTotal(BigDecimal.ZERO);
        if (currentUserService.isAdmin(usuario)) {
            venda.setEmpresa(itens.get(0).getProduto().getEmpresa());
        } else {
            venda.setEmpresa(usuario.getEmpresa());
        }

        venda = vendaRepository.save(venda);

        BigDecimal total = BigDecimal.ZERO;

        for (ItemVenda item : itens) {

            Produto produto = produtoRepository.findById(
                    item.getProduto().getIdProduto()
            ).orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            if (produto.getEmpresa() == null || !produto.getEmpresa().getIdEmpresa().equals(idEmpresa)) {
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
        currentUserService.validarEmpresaAcesso(idEmpresa);
        return vendaRepository.findByEmpresaIdEmpresa(idEmpresa);
    }

    public List<Venda> listarPorEmpresa(Long idEmpresa,
                                        LocalDateTime inicio,
                                        LocalDateTime fim) {
        currentUserService.validarEmpresaAcesso(idEmpresa);
        if (inicio == null && fim == null) {
            return listarPorEmpresa(idEmpresa);
        }

        if (inicio != null && fim != null) {
            return vendaRepository.findByEmpresaIdEmpresaAndDataBetween(idEmpresa, inicio, fim);
        }

        if (inicio != null) {
            return vendaRepository.findByEmpresaIdEmpresaAndDataAfter(idEmpresa, inicio);
        }

        return vendaRepository.findByEmpresaIdEmpresaAndDataBefore(idEmpresa, fim);
    }

    public ResumoVendasDto resumirVendas(Long idEmpresa,
                                         LocalDateTime inicio,
                                         LocalDateTime fim) {
        currentUserService.validarEmpresaAcesso(idEmpresa);
        List<Venda> vendas = listarPorEmpresa(idEmpresa, inicio, fim);

        BigDecimal total = vendas.stream()
                .map(Venda::getValorTotal)
                .filter(valor -> valor != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long count = vendas.size();
        BigDecimal ticket = count == 0
                ? BigDecimal.ZERO
                : total.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);

        return new ResumoVendasDto(total, count, ticket);
    }

    public List<ItemVendaResumoDto> listarItensPorVenda(Long idVenda) {
        Usuario usuario = currentUserService.getCurrentUser();
        if (!currentUserService.isAdmin(usuario)) {
            Venda venda = vendaRepository.findById(idVenda)
                    .orElseThrow(() -> new RuntimeException("Venda não encontrada"));
            if (venda.getEmpresa() == null || usuario.getEmpresa() == null) {
                throw new RuntimeException("Usuario sem empresa");
            }
            if (!venda.getEmpresa().getIdEmpresa().equals(usuario.getEmpresa().getIdEmpresa())) {
                throw new RuntimeException("Acesso negado");
            }
        }
        return itemVendaRepository.findByVendaIdVenda(idVenda)
                .stream()
                .map(item -> new ItemVendaResumoDto(
                        item.getIdItemVenda(),
                        item.getProduto() != null ? item.getProduto().getIdProduto() : null,
                        item.getProduto() != null ? item.getProduto().getNomeProduto() : "-",
                        item.getQuantidade(),
                        item.getPrecoUnitario()
                ))
                .collect(Collectors.toList());
    }
}

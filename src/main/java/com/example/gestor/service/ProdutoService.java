package com.example.gestor.service;

import com.example.gestor.entity.Produto;
import com.example.gestor.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }

    public List<Produto> listarPorEmpresa(Long idEmpresa) {
        return produtoRepository.findByEmpresaIdEmpresa(idEmpresa);
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public Produto atualizar(Long id, Produto produtoAtualizado) {
        Produto produto = buscarPorId(id);

        produto.setNomeProduto(produtoAtualizado.getNomeProduto());
        produto.setPreco(produtoAtualizado.getPreco());
        produto.setQtdEstoque(produtoAtualizado.getQtdEstoque());
        produto.setCategoria(produtoAtualizado.getCategoria());
        produto.setEmpresa(produtoAtualizado.getEmpresa());

        return produtoRepository.save(produto);
    }

    public void deletar(Long id) {
        Produto produto = buscarPorId(id);
        produtoRepository.delete(produto);
    }
}

package com.example.gestor.service;

import com.example.gestor.entity.Produto;
import com.example.gestor.entity.Usuario;
import com.example.gestor.repository.ProdutoRepository;
import com.example.gestor.security.CurrentUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CurrentUserService currentUserService;

    public ProdutoService(ProdutoRepository produtoRepository,
                          CurrentUserService currentUserService) {
        this.produtoRepository = produtoRepository;
        this.currentUserService = currentUserService;
    }

    public Produto salvar(Produto produto) {
        Usuario usuario = currentUserService.getCurrentUser();
        if (!currentUserService.isAdmin(usuario)) {
            if (usuario.getEmpresa() == null) {
                throw new RuntimeException("Usuario sem empresa");
            }
            produto.setEmpresa(usuario.getEmpresa());
        }
        return produtoRepository.save(produto);
    }

    public List<Produto> listarPorEmpresa(Long idEmpresa) {
        currentUserService.validarEmpresaAcesso(idEmpresa);
        return produtoRepository.findByEmpresaIdEmpresa(idEmpresa);
    }

    public List<Produto> listarPorEmpresaComEstoqueBaixo(Long idEmpresa, int limite) {
        currentUserService.validarEmpresaAcesso(idEmpresa);
        return produtoRepository.findByEmpresaIdEmpresaAndQtdEstoqueLessThanEqual(idEmpresa, limite);
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public Produto atualizar(Long id, Produto produtoAtualizado) {
        Produto produto = buscarPorId(id);

        Usuario usuario = currentUserService.getCurrentUser();
        if (!currentUserService.isAdmin(usuario)) {
            if (produto.getEmpresa() == null || usuario.getEmpresa() == null) {
                throw new RuntimeException("Usuario sem empresa");
            }
            if (!produto.getEmpresa().getIdEmpresa().equals(usuario.getEmpresa().getIdEmpresa())) {
                throw new RuntimeException("Acesso negado");
            }
        }

        produto.setNomeProduto(produtoAtualizado.getNomeProduto());
        produto.setPreco(produtoAtualizado.getPreco());
        produto.setQtdEstoque(produtoAtualizado.getQtdEstoque());
        produto.setCategoria(produtoAtualizado.getCategoria());
        if (currentUserService.isAdmin(usuario)) {
            produto.setEmpresa(produtoAtualizado.getEmpresa());
        }

        return produtoRepository.save(produto);
    }

    public void deletar(Long id) {
        Produto produto = buscarPorId(id);
        Usuario usuario = currentUserService.getCurrentUser();
        if (!currentUserService.isAdmin(usuario)) {
            if (produto.getEmpresa() == null || usuario.getEmpresa() == null) {
                throw new RuntimeException("Usuario sem empresa");
            }
            if (!produto.getEmpresa().getIdEmpresa().equals(usuario.getEmpresa().getIdEmpresa())) {
                throw new RuntimeException("Acesso negado");
            }
        }
        produtoRepository.delete(produto);
    }
}

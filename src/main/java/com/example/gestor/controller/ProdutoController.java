package com.example.gestor.controller;

import com.example.gestor.entity.Produto;
import com.example.gestor.service.ProdutoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    // 🔹 Criar produto
    @PostMapping
    public Produto criar(@RequestBody Produto produto) {
        return produtoService.salvar(produto);
    }

    // 🔹 Listar por empresa
    @GetMapping("/empresa/{idEmpresa}")
    public List<Produto> listarPorEmpresa(@PathVariable Long idEmpresa) {
        return produtoService.listarPorEmpresa(idEmpresa);
    }

    // 🔹 Buscar por id
    @GetMapping("/{id}")
    public Produto buscar(@PathVariable Long id) {
        return produtoService.buscarPorId(id);
    }

    // 🔹 Atualizar
    @PutMapping("/{id}")
    public Produto atualizar(@PathVariable Long id,
                             @RequestBody Produto produto) {
        return produtoService.atualizar(id, produto);
    }

    // 🔹 Deletar
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        produtoService.deletar(id);
    }
}

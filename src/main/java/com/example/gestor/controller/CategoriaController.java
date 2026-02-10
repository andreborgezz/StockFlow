package com.example.gestor.controller;

import com.example.gestor.entity.Categoria;
import com.example.gestor.service.CategoriaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public Categoria criar(@RequestBody Categoria categoria) {
        return categoriaService.salvar(categoria);
    }

    @GetMapping
    public List<Categoria> listar() {
        return categoriaService.listarTodas();
    }

    @GetMapping("/{id}")
    public Categoria buscar(@PathVariable Long id) {
        return categoriaService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Categoria atualizar(@PathVariable Long id,
                               @RequestBody Categoria categoria) {
        return categoriaService.atualizar(id, categoria);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        categoriaService.deletar(id);
    }
}

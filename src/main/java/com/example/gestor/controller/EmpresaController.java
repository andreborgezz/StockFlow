package com.example.gestor.controller;

import com.example.gestor.entity.Empresa;
import com.example.gestor.service.EmpresaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @PostMapping
    public Empresa criar(@RequestBody Empresa empresa) {
        return empresaService.criar(empresa);
    }

    @GetMapping
    public List<Empresa> listar() {
        return empresaService.listarTodas();
    }

    @GetMapping("/{id}")
    public Empresa buscar(@PathVariable Long id) {
        return empresaService.buscarPorId(id);
    }

    @PutMapping("/{id}/status")
    public Empresa alterarStatus(@PathVariable Long id,
                                 @RequestParam Boolean ativo) {
        return empresaService.ativarDesativar(id, ativo);
    }
}

package com.example.gestor.controller;

import com.example.gestor.entity.ItemVenda;
import com.example.gestor.entity.Venda;
import com.example.gestor.service.VendaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendas")
public class VendaController {

    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    @PostMapping("/empresa/{idEmpresa}")
    public Venda realizarVenda(@PathVariable Long idEmpresa,
                               @RequestBody List<ItemVenda> itens) {
        return vendaService.realizarVenda(idEmpresa, itens);
    }

    @GetMapping("/empresa/{idEmpresa}")
    public List<Venda> listarPorEmpresa(@PathVariable Long idEmpresa) {
        return vendaService.listarPorEmpresa(idEmpresa);
    }
}

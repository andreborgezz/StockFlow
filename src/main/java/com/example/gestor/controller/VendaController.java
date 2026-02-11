package com.example.gestor.controller;

import com.example.gestor.dto.ItemVendaResumoDto;
import com.example.gestor.dto.ResumoVendasDto;
import com.example.gestor.entity.ItemVenda;
import com.example.gestor.entity.Venda;
import com.example.gestor.service.VendaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    public List<Venda> listarPorEmpresa(@PathVariable Long idEmpresa,
                                        @RequestParam(required = false)
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                        LocalDateTime inicio,
                                        @RequestParam(required = false)
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                        LocalDateTime fim) {
        return vendaService.listarPorEmpresa(idEmpresa, inicio, fim);
    }

    @GetMapping("/empresa/{idEmpresa}/resumo")
    public ResumoVendasDto resumirVendas(@PathVariable Long idEmpresa,
                                         @RequestParam(required = false)
                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                         LocalDateTime inicio,
                                         @RequestParam(required = false)
                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                         LocalDateTime fim) {
        return vendaService.resumirVendas(idEmpresa, inicio, fim);
    }

    @GetMapping("/{idVenda}/itens")
    public List<ItemVendaResumoDto> listarItens(@PathVariable Long idVenda) {
        return vendaService.listarItensPorVenda(idVenda);
    }
}

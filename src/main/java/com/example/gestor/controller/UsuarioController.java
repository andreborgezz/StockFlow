package com.example.gestor.controller;

import com.example.gestor.entity.Usuario;
import com.example.gestor.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public Usuario criar(@RequestBody Usuario usuario) {
        return usuarioService.salvar(usuario);
    }

    @GetMapping("/empresa/{idEmpresa}")
    public List<Usuario> listarPorEmpresa(@PathVariable Long idEmpresa) {
        return usuarioService.listarPorEmpresa(idEmpresa);
    }

    @GetMapping("/{id}")
    public Usuario buscar(@PathVariable Long id) {
        return usuarioService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Usuario atualizar(@PathVariable Long id,
                             @RequestBody Usuario usuario) {
        return usuarioService.atualizar(id, usuario);
    }
}

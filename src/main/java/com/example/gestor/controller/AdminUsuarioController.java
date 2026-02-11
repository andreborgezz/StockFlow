package com.example.gestor.controller;

import com.example.gestor.dto.RoleUpdateRequest;
import com.example.gestor.entity.Usuario;
import com.example.gestor.service.UsuarioService;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/usuarios")
public class AdminUsuarioController {

    private final UsuarioService usuarioService;

    public AdminUsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PatchMapping("/{id}/role")
    public Usuario atualizarRole(@PathVariable Long id, @RequestBody RoleUpdateRequest request) {
        return usuarioService.atualizarRole(id, request.getRole());
    }
}

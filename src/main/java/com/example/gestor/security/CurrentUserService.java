package com.example.gestor.security;

import com.example.gestor.entity.Usuario;
import com.example.gestor.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final UsuarioRepository usuarioRepository;

    public CurrentUserService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuario nao autenticado");
        }

        String email = authentication.getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));
    }

    public boolean isAdmin(Usuario usuario) {
        return usuario != null && "ADMIN".equalsIgnoreCase(usuario.getRole());
    }

    public void validarEmpresaAcesso(Long idEmpresa) {
        Usuario usuario = getCurrentUser();
        if (isAdmin(usuario)) {
            return;
        }

        if (usuario.getEmpresa() == null || usuario.getEmpresa().getIdEmpresa() == null) {
            throw new RuntimeException("Usuario sem empresa");
        }

        if (!usuario.getEmpresa().getIdEmpresa().equals(idEmpresa)) {
            throw new RuntimeException("Acesso negado");
        }
    }
}

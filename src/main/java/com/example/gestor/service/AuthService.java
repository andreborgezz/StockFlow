package com.example.gestor.service;

import com.example.gestor.dto.LoginRequest;
import com.example.gestor.dto.LoginResponse;
import com.example.gestor.entity.Usuario;
import com.example.gestor.repository.UsuarioRepository;
import com.example.gestor.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager,
                       UsuarioRepository usuarioRepository,
                       JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        if (request == null || request.getEmail() == null || request.getSenha() == null) {
            throw new RuntimeException("Email e senha sao obrigatorios");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));

        String token = jwtService.generateToken(usuario);
        Long empresaId = usuario.getEmpresa() != null ? usuario.getEmpresa().getIdEmpresa() : null;

        return new LoginResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole(),
                empresaId,
                token
        );
    }
}

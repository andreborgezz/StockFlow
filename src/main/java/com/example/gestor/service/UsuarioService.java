package com.example.gestor.service;

import com.example.gestor.entity.Empresa;
import com.example.gestor.entity.Usuario;
import com.example.gestor.repository.EmpresaRepository;
import com.example.gestor.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, EmpresaRepository empresaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.empresaRepository = empresaRepository;
    }

    public Usuario salvar(Usuario usuario) {
        Empresa empresa = resolveEmpresa(usuario.getEmpresa());
        usuario.setEmpresa(empresa);
        usuario.setRole(normalizarRole(usuario.getRole()));
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarPorEmpresa(Long idEmpresa) {
        return usuarioRepository.findByEmpresaIdEmpresa(idEmpresa);
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public Usuario atualizar(Long id, Usuario usuarioAtualizado) {
        Usuario usuario = buscarPorId(id);

        usuario.setNome(usuarioAtualizado.getNome());
        usuario.setEmail(usuarioAtualizado.getEmail());
        usuario.setRole(normalizarRole(usuarioAtualizado.getRole()));
        usuario.setEmpresa(resolveEmpresa(usuarioAtualizado.getEmpresa()));

        return usuarioRepository.save(usuario);
    }

    public Usuario atualizarRole(Long id, String role) {
        Usuario usuario = buscarPorId(id);
        usuario.setRole(normalizarRole(role));
        return usuarioRepository.save(usuario);
    }

    private Empresa resolveEmpresa(Empresa empresa) {
        if (empresa == null || empresa.getIdEmpresa() == null) {
            throw new RuntimeException("Empresa obrigatória");
        }

        return empresaRepository.findById(empresa.getIdEmpresa())
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
    }

    private String normalizarRole(String role) {
        if (role == null) {
            throw new RuntimeException("Role obrigatoria");
        }

        String normalized = role.trim().toUpperCase();
        if (!normalized.equals("ADMIN") && !normalized.equals("GERENTE") && !normalized.equals("FUNCIONARIO")) {
            throw new RuntimeException("Role invalida");
        }

        return normalized;
    }
}

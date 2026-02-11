package com.example.gestor.repository;

import com.example.gestor.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    List<Usuario> findByEmpresaIdEmpresa(Long idEmpresa);

    Optional<Usuario> findByEmail(String email);
}

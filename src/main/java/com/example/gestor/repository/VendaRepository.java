package com.example.gestor.repository;

import com.example.gestor.entity.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    List<Venda> findByEmpresaIdEmpresa(Long idEmpresa);
}

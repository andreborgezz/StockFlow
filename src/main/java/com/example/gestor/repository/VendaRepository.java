package com.example.gestor.repository;

import com.example.gestor.entity.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    List<Venda> findByEmpresaIdEmpresa(Long idEmpresa);

    List<Venda> findByEmpresaIdEmpresaAndDataBetween(Long idEmpresa,
                                                     LocalDateTime inicio,
                                                     LocalDateTime fim);

    List<Venda> findByEmpresaIdEmpresaAndDataAfter(Long idEmpresa,
                                                   LocalDateTime inicio);

    List<Venda> findByEmpresaIdEmpresaAndDataBefore(Long idEmpresa,
                                                    LocalDateTime fim);
}

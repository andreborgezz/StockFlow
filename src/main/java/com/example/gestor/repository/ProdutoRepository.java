package com.example.gestor.repository;

import com.example.gestor.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByEmpresaIdEmpresa(Long idEmpresa);

    List<Produto> findByEmpresaIdEmpresaAndQtdEstoqueLessThanEqual(Long idEmpresa,
                                                                   int qtdEstoque);
}

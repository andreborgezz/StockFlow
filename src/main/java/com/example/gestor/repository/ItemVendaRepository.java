package com.example.gestor.repository;

import com.example.gestor.entity.ItemVenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {

	List<ItemVenda> findByVendaIdVenda(Long idVenda);
}

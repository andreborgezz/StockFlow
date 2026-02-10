package com.example.gestor.service;

import com.example.gestor.entity.Empresa;
import com.example.gestor.repository.EmpresaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    public EmpresaService(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    // 🔥 ESSE MÉTODO ESTAVA FALTANDO
    public Empresa criar(Empresa empresa) {
        empresa.setAtivo(true);
        return empresaRepository.save(empresa);
    }

    public List<Empresa> listarTodas() {
        return empresaRepository.findAll();
    }

    public Empresa buscarPorId(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
    }

    public Empresa ativarDesativar(Long id, Boolean ativo) {
        Empresa empresa = buscarPorId(id);
        empresa.setAtivo(ativo);
        return empresaRepository.save(empresa);
    }
}

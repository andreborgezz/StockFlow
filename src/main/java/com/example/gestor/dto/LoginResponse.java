package com.example.gestor.dto;

public class LoginResponse {

    private Long id;
    private String nome;
    private String email;
    private String role;
    private Long empresaId;
    private String token;

    public LoginResponse(Long id, String nome, String email, String role, Long empresaId, String token) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.role = role;
        this.empresaId = empresaId;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public String getToken() {
        return token;
    }
}

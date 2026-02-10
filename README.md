# Gestor API

Base URL: http://localhost:8080

Swagger UI (com a aplicacao rodando): http://localhost:8080/swagger-ui
OpenAPI JSON: http://localhost:8080/v3/api-docs
Spec local: openapi.yaml

## Empresas (admin)

- POST /admin/empresas

```json
{
  "nome": "Empresa X",
  "documento": "12345678910",
  "ativo": true
}
```

- GET /admin/empresas
- GET /admin/empresas/{id}
- PUT /admin/empresas/{id}/status?ativo=true

## Categorias

- POST /categorias

```json
{
  "nomeCategoria": "Bebidas"
}
```

- GET /categorias
- GET /categorias/{id}

## Usuarios

- POST /usuarios

```json
{
  "nome": "Admin",
  "email": "admin@email.com",
  "senha": "123456",
  "role": "ADMIN",
  "empresa": { "id": 3 }
}
```

- GET /usuarios/empresa/{idEmpresa}
- GET /usuarios/{id}

- PUT /usuarios/{id}

```json
{
  "nome": "Admin Atualizado",
  "email": "admin2@email.com",
  "senha": "123456",
  "role": "ADMIN",
  "empresa": { "id": 3 }
}
```

## Produtos

- POST /produtos

```json
{
  "nomeProduto": "Coca 2L",
  "preco": 9.99,
  "qtdEstoque": 50,
  "empresa": { "id": 3 },
  "categoria": { "idCategoria": 2 }
}
```

- GET /produtos/empresa/{idEmpresa}
- GET /produtos/{id}

- PUT /produtos/{id}

```json
{
  "nomeProduto": "Coca 2L Zero",
  "preco": 10.5,
  "qtdEstoque": 45,
  "empresa": { "id": 3 },
  "categoria": { "idCategoria": 2 }
}
```

- DELETE /produtos/{id}

## Vendas

- POST /vendas/empresa/{idEmpresa}

```json
[
  {
    "produto": { "idProduto": 1, "empresa": { "id": 3 } },
    "quantidade": 2
  }
]
```

- GET /vendas/empresa/{idEmpresa}

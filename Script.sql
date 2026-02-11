create database GestaoComercial;

use GestaoComercial;

create table categoria
(
    id_categoria   bigint auto_increment
        primary key,
    nome_categoria varchar(255) null
);

create table empresa
(
    id_empresa bigint auto_increment
        primary key,
    nome       varchar(255)         null,
    documento  varchar(255)         null,
    ativo      tinyint(1) default 1 null
);

create table produto
(
    id_produto   bigint auto_increment
        primary key,
    nome_produto varchar(255)   null,
    preco        decimal(38, 2) null,
    qtd_estoque  int            null,
    id_categoria bigint         null,
    id_empresa   bigint         null,
    constraint fk_produto_categoria
        foreign key (id_categoria) references categoria (id_categoria),
    constraint fk_produto_empresa
        foreign key (id_empresa) references empresa (id_empresa)
);

create table usuario
(
    id_usuario    bigint auto_increment
        primary key,
    role          varchar(255) null,
    id_empresa    bigint       null,
    email_usuario varchar(255) null,
    nome_usuario  varchar(255) null,
    senha_usuario varchar(255) null,
    constraint fk_usuario_empresa
        foreign key (id_empresa) references empresa (id_empresa)
);

create table venda
(
    id_venda    bigint auto_increment
        primary key,
    data        datetime       null,
    valor_total decimal(38, 2) null,
    id_empresa  bigint         null,
    constraint fk_venda_empresa
        foreign key (id_empresa) references empresa (id_empresa)
);

create table itemvenda
(
    id_itemvenda   int auto_increment
        primary key,
    id_venda       bigint         null,
    id_produto     bigint         null,
    quantidade     int            null,
    preco_unitario decimal(10, 2) null,
    constraint fk_itemvenda_produto
        foreign key (id_produto) references produto (id_produto),
    constraint fk_itemvenda_venda
        foreign key (id_venda) references venda (id_venda)
);

-- Limpar todos os dados
-- Use com cuidado: isso apaga tudo do schema GestaoComercial.
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE itemvenda;
TRUNCATE TABLE venda;
TRUNCATE TABLE usuario;
TRUNCATE TABLE produto;
TRUNCATE TABLE categoria;
TRUNCATE TABLE empresa;
SET FOREIGN_KEY_CHECKS = 1;

-- Seed de teste: empresa + usuario ADMIN
INSERT INTO empresa (nome, documento, ativo)
VALUES ('Empresa Teste', '00000000000191', 1);

SET @empresa_id = LAST_INSERT_ID();

INSERT INTO usuario (role, id_empresa, email_usuario, nome_usuario, senha_usuario)
VALUES ('ADMIN', @empresa_id, 'andre@admin.com', 'Admin', '191213');
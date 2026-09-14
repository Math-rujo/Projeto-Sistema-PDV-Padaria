/*Criação de um banco de dados para um Sistema de ponto de venda*/

Create table funcionario(
	id_funcionario			serial primary key,
	nome					varchar(100) not null,
	cargo					varchar(100) not null,
	usuario					varchar(30) unique not null,
	senha					varchar(255) not null
);

Create table telefone(
	id_telefone				serial primary key,
	id_funcionario			int not null,
	telefone				varchar(11) not null,

	Foreign key (id_funcionario) references funcionario(id_funcionario)
);

Create table produto(
	id_produto				serial primary key,
	nome					varchar(100) not null,
	descricao				varchar(200),
	preco					numeric(10,2) not null check(preco >= 0),
	estoque					int not null check(estoque >=0)
);

Create table venda(
	id_venda				serial primary key,
	data_venda				timestamp not null,
	id_funcionario			int not null,
	total					numeric(10,2) not null,

	foreign key (id_funcionario) references funcionario(id_funcionario)
);

Create table item_venda(
	id_item_venda			serial primary key,
	id_venda				int not null,
	id_produto				int not null,
	quantidade				int not null check(quantidade > 0),
	preco_unitario			numeric(10,2) not null,
	subtotal				numeric(10,2) not null,

	foreign key (id_venda) references venda(id_venda),
	foreign key (id_produto) references produto(id_produto)
);
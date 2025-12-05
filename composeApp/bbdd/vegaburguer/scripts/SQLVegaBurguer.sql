CREATE TABLE dependientes (
	id CHAR(64) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    imagePath VARCHAR(100),
    enabled TINYINT(1) DEFAULT 1,
    isAdmin TINYINT(1) DEFAULT 0
);

CREATE TABLE categorias (
	id CHAR(64) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(250),
    imagePath VARCHAR(100),
    enabled TINYINT(1) DEFAULT 1
);

CREATE TABLE productos (
	id CHAR(64) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    imagePath VARCHAR(100),
    price DECIMAL(10,2) NOT NULL,
    description VARCHAR(250),
    enabled TINYINT(1) DEFAULT 1,
    categoriaId CHAR(64),
    FOREIGN KEY (categoriaId) REFERENCES categorias(id)
);

CREATE TABLE pedidos (
	id CHAR(64) PRIMARY KEY,
    clienteName VARCHAR(100) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    fecha VARCHAR(100) NOT NULL,
    dependienteId CHAR(64),
    FOREIGN KEY (dependienteId) REFERENCES dependientes(id)
);

CREATE TABLE linea_pedido (
	id CHAR(64) PRIMARY KEY,
    cantidad INT NOT NULL,
    precioUnitario DECIMAL(10,2) NOT NULL,
    entregado TINYINT(1) DEFAULT 0,
    pedidoId CHAR(64),
    productoId CHAR(64),
    FOREIGN KEY (pedidoId) REFERENCES pedidos(id),
    FOREIGN KEY (productoId) REFERENCES productos(id)
);
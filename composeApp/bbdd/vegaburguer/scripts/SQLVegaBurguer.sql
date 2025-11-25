CREATE TABLE dependientes (
	id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    imagePath VARCHAR(100),
    enabled TINYINT(1) DEFAULT 1,
    isAdmin TINYINT(1) DEFAULT 0
);

CREATE TABLE categorias (
	id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(250),
    imagePath VARCHAR(100),
    enabled TINYINT(1) DEFAULT 1
);

CREATE TABLE productos (
	id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    imagePath VARCHAR(100),
    price DECIMAL(10,2) NOT NULL,
    description VARCHAR(250),
    enabled TINYINT(1) DEFAULT 1,
    categoriaId VARCHAR(10),
    FOREIGN KEY (categoriaId) REFERENCES categorias(id)
);

CREATE TABLE pedidos (
	id VARCHAR(10) PRIMARY KEY,
    clienteName VARCHAR(100) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    fecha VARCHAR(100) NOT NULL,
    dependienteId VARCHAR(10),
    FOREIGN KEY (dependienteId) REFERENCES dependientes(id)
);

CREATE TABLE linea_pedido (
	id VARCHAR(10) PRIMARY KEY,
    cantidad INT NOT NULL,
    precioUnitario DECIMAL(10,2) NOT NULL,
    entregado TINYINT(1) DEFAULT 0,
    pedidoId VARCHAR(10),
    productoId VARCHAR(10),
    FOREIGN KEY (pedidoId) REFERENCES pedidos(id),
    FOREIGN KEY (productoId) REFERENCES productos(id)
)
CREATE TABLE dependientes (
	id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    image_path VARCHAR(100),
    enabled TINYINT(1) DEFAULT 1,
    is_admin TINYINT(1) DEFAULT 0
);

CREATE TABLE categorias (
	id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(250),
    image_path VARCHAR(100),
    enabled TINYINT(1) DEFAULT 1
);

CREATE TABLE productos (
	id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    image_path VARCHAR(100),
    price DECIMAL(10,2) NOT NULL,
    description VARCHAR(250),
    enabled TINYINT(1) DEFAULT 1,
    categoria_id INT,
    FOREIGN KEY (categoria_id) REFERENCES categorias(id)
);

CREATE TABLE pedidos (
	id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_name VARCHAR(100) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    fecha DATETIME NOT NULL,
    dependiente_id INT,
    FOREIGN KEY (dependiente_id) REFERENCES dependientes(id)
);

CREATE TABLE linea_pedido (
	id INT AUTO_INCREMENT PRIMARY KEY,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    entregado TINYINT(1) DEFAULT 0,
    pedido_id INT,
    producto_id INT,
    FOREIGN KEY (pedido_id) REFERENCES pedidos(id),
    FOREIGN KEY (producto_id) REFERENCES productos(id)
)
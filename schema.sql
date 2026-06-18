CREATE TABLE categoria (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           nombre VARCHAR(100) NOT NULL UNIQUE,
                           descripcion VARCHAR(255),
                           eliminado BOOLEAN DEFAULT FALSE,
                           created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE usuario (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         nombre VARCHAR(100) NOT NULL,
                         apellido VARCHAR(100) NOT NULL,
                         mail VARCHAR(150) NOT NULL UNIQUE,
                         celular VARCHAR(20),
                         contrasena VARCHAR(100),
                         rol ENUM('ADMIN', 'USUARIO') DEFAULT 'USUARIO',
                         eliminado BOOLEAN DEFAULT FALSE,
                         created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE producto (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL,
                          descripcion VARCHAR(255),
                          precio DOUBLE NOT NULL,
                          stock INT NOT NULL,
                          imagen VARCHAR(255),
                          disponible BOOLEAN DEFAULT TRUE,
                          categoria_id BIGINT NOT NULL,
                          eliminado BOOLEAN DEFAULT FALSE,
                          created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (categoria_id) REFERENCES categoria(id)
);

CREATE TABLE pedido (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        fecha DATE NOT NULL,
                        estado ENUM('PENDIENTE', 'CONFIRMADO', 'TERMINADO', 'CANCELADO') DEFAULT 'PENDIENTE',
                        total DOUBLE DEFAULT 0,
                        forma_pago ENUM('TARJETA', 'TRANSFERENCIA', 'EFECTIVO'),
                        usuario_id BIGINT NOT NULL,
                        eliminado BOOLEAN DEFAULT FALSE,
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

CREATE TABLE detalle_pedido (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                cantidad INT NOT NULL,
                                subtotal DOUBLE NOT NULL,
                                pedido_id BIGINT NOT NULL,
                                producto_id BIGINT NOT NULL,
                                eliminado BOOLEAN DEFAULT FALSE,
                                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                FOREIGN KEY (pedido_id) REFERENCES pedido(id),
                                FOREIGN KEY (producto_id) REFERENCES producto(id)
);CREATE TABLE categoria (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             nombre VARCHAR(100) NOT NULL UNIQUE,
                             descripcion VARCHAR(255),
                             eliminado BOOLEAN DEFAULT FALSE,
                             created_at DATETIME DEFAULT CURRENT_TIMESTAMP
  );

CREATE TABLE usuario (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         nombre VARCHAR(100) NOT NULL,
                         apellido VARCHAR(100) NOT NULL,
                         mail VARCHAR(150) NOT NULL UNIQUE,
                         celular VARCHAR(20),
                         contrasena VARCHAR(100),
                         rol ENUM('ADMIN', 'USUARIO') DEFAULT 'USUARIO',
                         eliminado BOOLEAN DEFAULT FALSE,
                         created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE producto (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL,
                          descripcion VARCHAR(255),
                          precio DOUBLE NOT NULL,
                          stock INT NOT NULL,
                          imagen VARCHAR(255),
                          disponible BOOLEAN DEFAULT TRUE,
                          categoria_id BIGINT NOT NULL,
                          eliminado BOOLEAN DEFAULT FALSE,
                          created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (categoria_id) REFERENCES categoria(id)
);

CREATE TABLE pedido (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        fecha DATE NOT NULL,
                        estado ENUM('PENDIENTE', 'CONFIRMADO', 'TERMINADO', 'CANCELADO') DEFAULT 'PENDIENTE',
                        total DOUBLE DEFAULT 0,
                        forma_pago ENUM('TARJETA', 'TRANSFERENCIA', 'EFECTIVO'),
                        usuario_id BIGINT NOT NULL,
                        eliminado BOOLEAN DEFAULT FALSE,
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

CREATE TABLE detalle_pedido (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                cantidad INT NOT NULL,
                                subtotal DOUBLE NOT NULL,
                                pedido_id BIGINT NOT NULL,
                                producto_id BIGINT NOT NULL,
                                eliminado BOOLEAN DEFAULT FALSE,
                                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                FOREIGN KEY (pedido_id) REFERENCES pedido(id),
                                FOREIGN KEY (producto_id) REFERENCES producto(id)
);
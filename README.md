# Food Store - Sistema de Gestion de Pedidos (Consola + JDBC)

Trabajo Practico Integrador - Programacion 2
Tecnicatura Universitaria en Programacion - UTN

## Descripcion

Sistema de consola para la gestion de un negocio de comidas. Permite administrar categorias, productos, usuarios y pedidos mediante operaciones CRUD completas, utilizando Programacion Orientada a Objetos y persistencia con JDBC sobre una base de datos MySQL.

## Tecnologias utilizadas

- Java 25
- MySQL
- JDBC (mysql-connector-j)
- XAMPP (servidor local de MySQL)

## Requisitos previos

- XAMPP instalado, con los modulos Apache y MySQL
- IntelliJ IDEA (o cualquier IDE compatible con Java)
- Driver JDBC de MySQL (mysql-connector-j), descargado desde https://dev.mysql.com/downloads/connector/j/

## Como crear la base de datos

1. Abrir el panel de control de XAMPP e iniciar los modulos Apache y MySQL.
2. Ir a `http://localhost/phpmyadmin` desde el navegador.
3. Crear una base de datos nueva llamada `pedidos_db`.
4. Entrar a `pedidos_db`, ir a la pestana SQL, y ejecutar el contenido del archivo `schema.sql` incluido en este repositorio.

Esto crea las tablas `categoria`, `producto`, `usuario`, `pedido` y `detalle_pedido`, todas con soft delete mediante el campo `eliminado`.

## Como configurar la conexion

La configuracion de conexion se encuentra en la clase `ConexionDB` (paquete `config`):

```java
private static final String URL = "jdbc:mysql://localhost:3306/pedidos_db";
private static final String USER = "root";
private static final String PASSWORD = "";
```

Por defecto, XAMPP usa el usuario `root` sin contrasena. Si la configuracion de MySQL en tu maquina es distinta, editar estos tres valores antes de ejecutar el proyecto.

## Como agregar el driver JDBC

1. Descargar el conector desde https://dev.mysql.com/downloads/connector/j/ (version Platform Independent).
2. Extraer el archivo `.jar` del ZIP descargado.
3. En IntelliJ: File -> Project Structure -> Libraries -> + -> Java, y seleccionar el `.jar` extraido.

## Como ejecutar el proyecto

1. Verificar que XAMPP (Apache + MySQL) este corriendo.
2. Verificar que la base `pedidos_db` ya tenga las tablas creadas (ver seccion anterior).
3. Abrir el proyecto en IntelliJ y ejecutar la clase `Main`.
4. Se mostrara el menu principal en consola:

```
=== SISTEMA DE PEDIDOS (FOOD STORE) ===
1. Categorias
2. Productos
3. Usuarios
4. Pedidos
0. Salir
```

5. Navegar por los submenus para realizar operaciones de alta, baja, modificacion y consulta sobre cada entidad.

## Estructura del proyecto

```
src/
├── Main.java
├── config/      (ConexionDB)
├── entities/    (Base, Categoria, Producto, Usuario, Pedido, DetallePedido, Calculable)
├── enums/       (Rol, Estado, FormaPago)
├── dao/         (DAO por entidad + IBaseDAO)
├── service/     (logica de negocio y validaciones)
├── menu/        (interaccion por consola)
└── exception/   (EntityNotFoundException)
```

## Reglas de negocio implementadas

- No se permite crear un producto con precio o stock negativo.
- No se permite crear un pedido sin usuario.
- No se permite agregar un detalle de pedido con cantidad menor o igual a cero.
- No se permite agregar a un pedido una cantidad de producto mayor al stock disponible.
- El mail de usuario es unico.
- Todas las bajas son logicas (campo `eliminado`), nunca se borra un registro fisicamente.
- La creacion de un pedido con sus detalles se realiza dentro de una transaccion JDBC: si falla la insercion de un detalle, se revierte el pedido completo (rollback).

## Enlace a repositorio de GitHub

https://github.com/aresito22/TPI-Programacion2-2026
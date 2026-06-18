# Food Store - Estado del proyecto

Persona A completo su parte. Esto es lo que ya esta hecho y subido al repo:

## Base del proyecto
- Estructura de paquetes completa (config, entities, enums, dao, service, menu, exception)
- Conexion a la base de datos (ConexionDB)
- Clase abstracta Base, enums (Rol, Estado, FormaPago), interfaz Calculable, interfaz IBaseDAO
- Excepcion EntityNotFoundException
- Schema SQL con las 5 tablas ya ejecutado (pedidos_db)

## Categorias y Productos
- Slices completos: entidad, DAO, service y menu para Categoria y Producto
- Main.java con el menu principal ya armado, con los lugares 3 y 4 como placeholders para Usuario y Pedidos

---

## Lo que falta

**Persona B - Usuario:** crear `Usuario.java` en entities, `UsuarioDAO` en dao, `UsuarioService` en service, `UsuarioMenu` en menu. Seguir el mismo patron que Categoria como plantilla. Cuando este listo, reemplazar en `Main.java` la linea que dice `"Modulo de usuarios pendiente"` por `new UsuarioMenu(scanner).mostrar()`.

**Persona C - Pedido y DetallePedido:** crear ambas entidades en entities, `PedidoDAO` en dao (con transaccion), `PedidoService` en service, `PedidoMenu` en menu. Pueden usar datos hardcodeados de Usuario y Producto para probar mientras no este lo de B y A mergeado. Cuando este listo, reemplazar en `Main.java` la linea que dice `"Modulo de pedidos pendiente"` por `new PedidoMenu(scanner).mostrar()`.

Usen Categoria como plantilla para todo: mismo patron de DAO, mismo patron de service, mismo patron de menu.

---

## Setup inicial antes de arrancar

### XAMPP
1. Descargar e instalar XAMPP
2. Abrir el panel de control de XAMPP y hacer click en Start en Apache y MySQL
3. Abrir el navegador e ir a `http://localhost/phpmyadmin`
4. En el panel izquierdo hacer click en New, crear una base de datos llamada `pedidos_db`
5. Entrar a `pedidos_db`, ir a la pestaña SQL, pegar el contenido del archivo `schema.sql` del repo y ejecutarlo

### Driver MySQL (JAR)
1. Entrar a https://dev.mysql.com/downloads/connector/j/
2. Seleccionar Platform Independent, descargar el ZIP y extraerlo
3. En IntelliJ ir a File -> Project Structure -> Libraries -> + -> Java
4. Buscar el archivo `.jar` que extrajeron y agregarlo

Despues de eso el proyecto deberia conectarse sin problemas. La contrasena de MySQL en XAMPP por defecto es vacia, si la tienen configurada diferente editar la linea `PASSWORD` en `ConexionDB.java`.

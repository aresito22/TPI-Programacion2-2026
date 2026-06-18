package menu;

import entities.Producto;
import exception.EntityNotFoundException;
import service.ProductoService;

import java.util.List;
import java.util.Scanner;

public class ProductoMenu {

    private final ProductoService productoService = new ProductoService();
    private final Scanner scanner;

    public ProductoMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void mostrar() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n=== PRODUCTOS ===");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Editar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Opcion invalida.");
                continue;
            }

            switch (opcion) {
                case 1 -> listar();
                case 2 -> crear();
                case 3 -> editar();
                case 4 -> eliminar();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opcion invalida.");
            }
        }
    }

    private void listar() {
        List<Producto> lista = productoService.listarProductos();
        if (lista.isEmpty()) {
            System.out.println("No hay productos cargados.");
        } else {
            lista.forEach(System.out::println);
        }
    }

    private void crear() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Descripcion: ");
        String descripcion = scanner.nextLine().trim();
        System.out.print("Precio: ");
        double precio;
        try {
            precio = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Precio invalido.");
            return;
        }
        System.out.print("Stock: ");
        int stock;
        try {
            stock = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Stock invalido.");
            return;
        }
        System.out.print("Imagen (URL o nombre, Enter para omitir): ");
        String imagen = scanner.nextLine().trim();
        System.out.print("Disponible (S/N): ");
        boolean disponible = scanner.nextLine().trim().equalsIgnoreCase("S");
        System.out.print("ID de categoria: ");
        Long categoriaId;
        try {
            categoriaId = Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("ID de categoria invalido.");
            return;
        }
        try {
            productoService.crearProducto(nombre, descripcion, precio, stock, imagen, disponible, categoriaId);
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Error al crear producto: " + e.getMessage());
        }
    }

    private void editar() {
        listar();
        System.out.print("ID a editar: ");
        Long id;
        try {
            id = Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("ID invalido.");
            return;
        }
        System.out.print("Nuevo nombre (Enter para mantener): ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Nueva descripcion (Enter para mantener): ");
        String descripcion = scanner.nextLine().trim();
        System.out.print("Nuevo precio (Enter para mantener): ");
        String precioStr = scanner.nextLine().trim();
        System.out.print("Nuevo stock (Enter para mantener): ");
        String stockStr = scanner.nextLine().trim();
        System.out.print("Nueva imagen (Enter para mantener): ");
        String imagen = scanner.nextLine().trim();
        System.out.print("Disponible (S/N, Enter para mantener): ");
        String dispStr = scanner.nextLine().trim();
        System.out.print("Nueva categoria ID (Enter para mantener): ");
        String catStr = scanner.nextLine().trim();

        try {
            productoService.editarProducto(
                    id,
                    nombre.isBlank() ? null : nombre,
                    descripcion.isBlank() ? null : descripcion,
                    precioStr.isBlank() ? null : Double.parseDouble(precioStr),
                    stockStr.isBlank() ? null : Integer.parseInt(stockStr),
                    imagen.isBlank() ? null : imagen,
                    dispStr.isBlank() ? null : dispStr.equalsIgnoreCase("S"),
                    catStr.isBlank() ? null : Long.parseLong(catStr)
            );
        } catch (NumberFormatException e) {
            System.out.println("Valor invalido ingresado.");
        } catch (EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void eliminar() {
        listar();
        System.out.print("ID a eliminar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            System.out.print("Confirma? (S/N): ");
            String confirmacion = scanner.nextLine().trim();
            if (confirmacion.equalsIgnoreCase("S")) {
                productoService.eliminarProducto(id);
            } else {
                System.out.println("Operacion cancelada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID invalido.");
        } catch (EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
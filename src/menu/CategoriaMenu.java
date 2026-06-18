package menu;

import entities.Categoria;
import exception.EntityNotFoundException;
import service.CategoriaService;

import java.util.List;
import java.util.Scanner;

public class CategoriaMenu {

    private final CategoriaService categoriaService = new CategoriaService();
    private final Scanner scanner;

    public CategoriaMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void mostrar() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n=== CATEGORÍAS ===");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Editar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");
            System.out.print("Seleccione: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida.");
                continue;
            }

            switch (opcion) {
                case 1 -> listar();
                case 2 -> crear();
                case 3 -> editar();
                case 4 -> eliminar();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    private void listar() {
        List<Categoria> lista = categoriaService.listarCategorias();
        if (lista.isEmpty()) {
            System.out.println("No hay categorías cargadas.");
        } else {
            lista.forEach(System.out::println);
        }
    }

    private void crear() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Descripción: ");
        String descripcion = scanner.nextLine().trim();
        try {
            categoriaService.crearCategoria(nombre, descripcion);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Error al crear categoría: " + e.getMessage());
        }
    }

    private void editar() {
        listar();
        System.out.print("ID a editar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            System.out.print("Nuevo nombre (Enter para mantener): ");
            String nombre = scanner.nextLine().trim();
            System.out.print("Nueva descripción (Enter para mantener): ");
            String descripcion = scanner.nextLine().trim();
            categoriaService.editarCategoria(
                    id,
                    nombre.isBlank() ? null : nombre,
                    descripcion.isBlank() ? null : descripcion
            );
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
        } catch (EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void eliminar() {
        listar();
        System.out.print("ID a eliminar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            System.out.print("¿Confirma? (S/N): ");
            String confirmacion = scanner.nextLine().trim();
            if (confirmacion.equalsIgnoreCase("S")) {
                categoriaService.eliminarCategoria(id);
            } else {
                System.out.println("Operación cancelada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
        } catch (EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
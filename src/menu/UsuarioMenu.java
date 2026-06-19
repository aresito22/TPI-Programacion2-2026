package menu;

import entities.Usuario;
import enums.Rol;
import exception.EntityNotFoundException;
import service.UsuarioService;

import java.util.List;
import java.util.Scanner;

public class UsuarioMenu {

    private final UsuarioService usuarioService = new UsuarioService();
    private final Scanner scanner;

    public UsuarioMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void mostrar() {

        int opcion = -1;

        while (opcion != 0) {

            System.out.println("\n=== USUARIOS ===");
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

        List<Usuario> lista = usuarioService.listarUsuarios();

        if (lista.isEmpty()) {
            System.out.println("No hay usuarios cargados.");
        } else {
            lista.forEach(System.out::println);
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
                usuarioService.eliminarUsuario(id);

            } else {
                System.out.println("Operación cancelada.");
            }

        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");

        } catch (EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void crear() {

        try {

            System.out.print("Nombre: ");
            String nombre = scanner.nextLine().trim();

            System.out.print("Apellido: ");
            String apellido = scanner.nextLine().trim();

            System.out.print("Mail: ");
            String mail = scanner.nextLine().trim();

            System.out.print("Celular: ");
            String celular = scanner.nextLine().trim();

            System.out.print("Contraseña: ");
            String contrasena = scanner.nextLine().trim();

            System.out.print("Rol (ADMIN/USUARIO): ");
            Rol rol = Rol.valueOf(scanner.nextLine().trim().toUpperCase());

            usuarioService.crearUsuario(nombre, apellido, mail, celular, contrasena, rol
            );

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());

        } catch (RuntimeException e) {
            System.out.println("Error al crear usuario: " + e.getMessage());
        }
    }

    private void editar() {

        listar();

        System.out.print("ID a editar: ");

        try {

            Long id = Long.parseLong(scanner.nextLine().trim());

            System.out.print("Nombre (Enter para mantener): ");
            String nombre = scanner.nextLine().trim();

            System.out.print("Apellido (Enter para mantener): ");
            String apellido = scanner.nextLine().trim();

            System.out.print("Mail (Enter para mantener): ");
            String mail = scanner.nextLine().trim();

            System.out.print("Celular (Enter para mantener): ");
            String celular = scanner.nextLine().trim();

            System.out.print("Contraseña (Enter para mantener): ");
            String contrasena = scanner.nextLine().trim();

            System.out.print("Rol (ADMIN/USUARIO, Enter para mantener): ");
            String rolTexto = scanner.nextLine().trim();

            Rol rol = null;

            if (!rolTexto.isBlank()) {
                rol = Rol.valueOf(rolTexto.toUpperCase());
            }

            usuarioService.editarUsuario(
                    id,
                    nombre.isBlank() ? null : nombre,
                    apellido.isBlank() ? null : apellido,
                    mail.isBlank() ? null : mail,
                    celular.isBlank() ? null : celular,
                    contrasena.isBlank() ? null : contrasena,
                    rol
            );

        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");

        } catch (EntityNotFoundException e) {
            System.out.println("Error: " + e.getMessage());

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
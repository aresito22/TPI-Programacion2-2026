import menu.CategoriaMenu;
import menu.ProductoMenu;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n=== SISTEMA DE PEDIDOS (FOOD STORE) ===");
            System.out.println("1. Categorias");
            System.out.println("2. Productos");
            System.out.println("3. Usuarios");
            System.out.println("4. Pedidos");
            System.out.println("0. Salir");
            System.out.print("Seleccione: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Opcion invalida.");
                continue;
            }

            switch (opcion) {
                case 1 -> new CategoriaMenu(scanner).mostrar();
                case 2 -> new ProductoMenu(scanner).mostrar();
                case 3 -> System.out.println("Modulo de usuarios pendiente.");
                case 4 -> System.out.println("Modulo de pedidos pendiente.");
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opcion invalida.");
            }
        }
    }
}
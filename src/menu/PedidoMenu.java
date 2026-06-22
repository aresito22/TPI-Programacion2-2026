package menu;

import entities.DetallePedido;
import entities.Pedido;
import entities.Producto;
import entities.Usuario;
import enums.Estado;
import enums.FormaPago;
import service.PedidoService;
import service.ProductoService;
import service.UsuarioService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class PedidoMenu {

    private final PedidoService pedidoService = new PedidoService();
    private final UsuarioService usuarioService = new UsuarioService();
    private final ProductoService productoService = new ProductoService();
    private final Scanner scanner;

    public PedidoMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void mostrar() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n=== PEDIDOS ===");
            System.out.println("1. Listar");
            System.out.println("2. Crear Pedido");
            System.out.println("3. Editar Estado");
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
        List<Pedido> lista = pedidoService.listarPedidos();
        if (lista.isEmpty()) {
            System.out.println("No hay pedidos cargados.");
        } else {
            for (Pedido p : lista) {
                System.out.println("ID: " + p.getId() + " | Fecha: " + p.getFecha() + " | Estado: " + p.getEstado() + " | Total: $" + p.getTotal());
            }
        }
    }

    private void crear() {
        System.out.println("\n--- Usuarios disponibles ---");
        usuarioService.listarUsuarios().forEach(System.out::println);
        System.out.print("ID de usuario: ");
        Long usuarioId;
        try {
            usuarioId = Long.parseLong(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("ID invalido.");
            return;
        }

        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(usuarioId);
        if (usuarioOpt.isEmpty()) {
            System.out.println("Usuario no encontrado.");
            return;
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuarioOpt.get());

        System.out.print("Forma de pago (TARJETA/TRANSFERENCIA/EFECTIVO): ");
        try {
            pedido.setFormaPago(FormaPago.valueOf(scanner.nextLine().trim().toUpperCase()));
        } catch (IllegalArgumentException e) {
            System.out.println("Forma de pago invalida.");
            return;
        }

        boolean agregarMas = true;
        while (agregarMas) {
            System.out.println("\n--- Productos disponibles ---");
            productoService.listarProductos().forEach(System.out::println);
            System.out.print("ID de producto: ");
            Long productoId;
            try {
                productoId = Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("ID invalido.");
                continue;
            }

            Optional<Producto> productoOpt = productoService.buscarPorId(productoId);
            if (productoOpt.isEmpty()) {
                System.out.println("Producto no encontrado.");
                continue;
            }

            System.out.print("Cantidad: ");
            int cantidad;
            try {
                cantidad = Integer.parseInt(scanner.nextLine().trim());
                if (cantidad <= 0) {
                    System.out.println("La cantidad debe ser mayor a 0.");
                    continue;
                }
                if (cantidad > productoOpt.get().getStock()) {
                    System.out.println("Stock insuficiente. Stock disponible: " + productoOpt.get().getStock());
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Cantidad invalida.");
                continue;
            }

            DetallePedido detalle = new DetallePedido(cantidad, productoOpt.get());
            pedido.addDetallePedido(detalle);

            System.out.print("Agregar otro producto (S/N): ");
            agregarMas = scanner.nextLine().trim().equalsIgnoreCase("S");
        }

        try {
            pedidoService.crearPedido(pedido);
            System.out.println("Pedido registrado con exito.");
            System.out.println("Total calculado: $" + pedido.getTotal());
        } catch (RuntimeException e) {
            System.out.println("Error al crear pedido: " + e.getMessage());
        }
    }

    private void editar() {
        listar();
        System.out.print("ID a editar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());

            pedidoService.buscarPedidoPorId(id).ifPresentOrElse(p -> {
                System.out.println("Estado actual: " + p.getEstado());
                System.out.print("Nuevo Estado (PENDIENTE/CONFIRMADO/TERMINADO/CANCELADO): ");
                String estadoTexto = scanner.nextLine().trim().toUpperCase();

                if (!estadoTexto.isBlank()) {
                    p.setEstado(Estado.valueOf(estadoTexto));
                    pedidoService.editarPedido(p);
                    System.out.println("Estado actualizado con exito.");
                }
            }, () -> System.out.println("Pedido no encontrado."));

        } catch (NumberFormatException e) {
            System.out.println("ID invalido.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Estado invalido.");
        }
    }

    private void eliminar() {
        listar();
        System.out.print("ID a eliminar: ");
        try {
            Long id = Long.parseLong(scanner.nextLine().trim());
            System.out.print("Confirma (S/N): ");
            String confirmacion = scanner.nextLine().trim();
            if (confirmacion.equalsIgnoreCase("S")) {
                pedidoService.eliminarPedido(id);
                System.out.println("Pedido dado de baja de manera logica.");
            } else {
                System.out.println("Operacion cancelada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID invalido.");
        }
    }
}
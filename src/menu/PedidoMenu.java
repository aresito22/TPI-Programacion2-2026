package menu;

import entities.DetallePedido;
import entities.Pedido;
import entities.Producto;
import entities.Usuario;
import enums.Estado;
import enums.FormaPago;
import service.PedidoService;

import java.util.List;
import java.util.Scanner;

public class PedidoMenu {

    private final PedidoService pedidoService = new PedidoService();
    private final Scanner scanner;

    private Usuario usuarioFake;
    private Producto productoFake1;
    private Producto productoFake2;

    public PedidoMenu(Scanner scanner) {
        this.scanner = scanner;
        inicializarDatosFake();
    }

    private void inicializarDatosFake() {
        usuarioFake = new Usuario();
        usuarioFake.setId(1L);
        usuarioFake.setNombre("Gabriel Dennis");

        this.productoFake1 = new Producto();
        productoFake1.setId(1L);
        productoFake1.setNombre("Dennis burger");
        this.productoFake1.setPrecio(200.0);

        this.productoFake2 = new Producto();
        productoFake2.setId(2L);
        productoFake2.setNombre("Dennis pizza");
        this.productoFake2.setPrecio(300.0);
    }

    public void mostrar() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n=== PEDIDOS ===");
            System.out.println("1. Listar");
            System.out.println("2. Crear Pedido Simulado");
            System.out.println("3. Editar Estado");
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
                case 2 -> crearSimulado();
                case 3 -> editar();
                case 4 -> eliminar();
                case 0 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida.");
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

    private void crearSimulado() {
        System.out.println("\n--- Creando Pedido de Prueba Hardcodeado ---");
        try {
            Pedido p = new Pedido();
            p.setUsuario(usuarioFake);
            p.setFormaPago(FormaPago.EFECTIVO);

            DetallePedido d1 = new DetallePedido(2, productoFake1);
            DetallePedido d2 = new DetallePedido(1, productoFake2);

            p.addDetallePedido(d1);
            p.addDetallePedido(d2);

            pedidoService.crearPedido(p);
            System.out.println("¡Pedido registrado con éxito en MySQL!");
            System.out.println("Total calculado automáticamente: $" + p.getTotal());

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
                    System.out.println("¡Estado actualizado con éxito!");
                }
            }, () -> System.out.println("Pedido no encontrado."));

        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Estado inválido.");
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
                pedidoService.eliminarPedido(id);
                System.out.println("Pedido dado de baja de manera lógica.");
            } else {
                System.out.println("Operación cancelada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
        }
    }
}
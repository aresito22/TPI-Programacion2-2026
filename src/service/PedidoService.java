package service;

import dao.PedidoDAO;
import entities.Pedido;

import java.util.List;
import java.util.Optional;

public class PedidoService {

    private final PedidoDAO pedidoDAO = new PedidoDAO();

    public void crearPedido(Pedido pedido) {
        if (pedido.getDetalles().isEmpty()) {
            throw new RuntimeException("No se puede guardar un pedido sin productos.");
        }
        pedidoDAO.save(pedido);
    }

    public Optional<Pedido> buscarPedidoPorId(Long id) {
        return pedidoDAO.findById(id);
    }

    public List<Pedido> listarPedidos() {
        return pedidoDAO.findAll();
    }

    public void editarPedido(Pedido pedido) {
        pedidoDAO.update(pedido);
    }

    public void eliminarPedido(Long id) {
        pedidoDAO.delete(id);
    }
}

package entities;

import enums.Estado;
import enums.FormaPago;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private Long id;
    private LocalDate fecha = LocalDate.now();
    private Estado estado = Estado.PENDIENTE;
    private double total;
    private FormaPago formaPago;
    private Usuario usuario;
    private boolean eliminado = false;
    private List<DetallePedido> detalles = new ArrayList<>();


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
    public double getTotal() {
        calcularTotal();
        return total;
    }
    public void setTotal(double total) { this.total = total; }

    public FormaPago getFormaPago() { return formaPago; }
    public void setFormaPago(FormaPago formaPago) { this.formaPago = formaPago; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public boolean isEliminado() { return eliminado; }
    public void setEliminado(boolean eliminado) { this.eliminado = eliminado; }

    public List<DetallePedido> getDetalles() { return detalles; }


    public void addDetallePedido(DetallePedido detalle) {
        this.detalles.add(detalle);
        calcularTotal();
    }

    public DetallePedido findDetallePedidoByProducto(Long productoId) {
        return this.detalles.stream()
                .filter(d -> d.getProducto().getId().equals(productoId))
                .findFirst()
                .orElse(null);
    }

    public void deleteDetallePedidoByProducto(Long productoId) {
        this.detalles.removeIf(d -> d.getProducto().getId().equals(productoId));
        calcularTotal(); // Recalcula al sacar un producto
    }

    public void calcularTotal() {
        this.total = this.detalles.stream()
                .mapToDouble(DetallePedido::getSubtotal)
                .sum();
    }
}

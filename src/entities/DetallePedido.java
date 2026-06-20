package entities;

import java.time.LocalDateTime;

public class DetallePedido {
    private Long id;
    private int cantidad;
    private double subtotal;
    private Long pedidoId;
    private Producto producto;
    private boolean eliminado;
    private LocalDateTime createdAt;

    public DetallePedido() {
    }

    public DetallePedido(int cantidad, Producto producto) {
        this.cantidad = cantidad;
        this.producto = producto;
        this.subtotal =producto.getPrecio() * cantidad;
        this.eliminado = false;
    }

    public Long getId() {return  id;}
    public void setId(Long id) {this.id = id;}

    public int getCantidad() {return cantidad;}
    public void setCantidad(int cantidad) {this.cantidad = cantidad;}

    public double getSubtotal() {return subtotal;}
    public void setSubtotal(double subtotal) {this.subtotal = subtotal;}

    public Long getPedidoId() {return pedidoId;}
    public void setPedidoId(Long pedidoId) {this.pedidoId = pedidoId;}

    public Producto getProducto() {return producto;}
    public void setProducto(Producto producto) {this.producto = producto;}

    public boolean isEliminado() {return eliminado;}
    public void setEliminado(boolean eliminado) {this.eliminado = eliminado;}

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

package service;

import dao.CategoriaDAO;
import dao.ProductoDAO;
import entities.Categoria;
import entities.Producto;
import exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public class ProductoService {

    private final ProductoDAO productoDAO = new ProductoDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    public void crearProducto(String nombre, String descripcion, double precio, int stock, String imagen, boolean disponible, Long categoriaId) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio.");
        }
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        Optional<Categoria> categoria = categoriaDAO.findById(categoriaId);
        if (categoria.isEmpty()) {
            throw new EntityNotFoundException("No se encontro categoria con ID: " + categoriaId);
        }
        Producto producto = new Producto(nombre, precio, descripcion, stock, imagen, disponible, categoria.get());
        productoDAO.save(producto);
        System.out.println("Producto creado con ID: " + producto.getId());
    }

    public List<Producto> listarProductos() {
        return productoDAO.findAll();
    }

    public List<Producto> listarPorCategoria(Long categoriaId) {
        return productoDAO.findByCategoria(categoriaId);
    }

    public Optional<Producto> buscarPorId(Long id) {
        return productoDAO.findById(id);
    }

    public void editarProducto(Long id, String nombre, String descripcion, Double precio, Integer stock, String imagen, Boolean disponible, Long categoriaId) {
        Optional<Producto> optional = productoDAO.findById(id);
        if (optional.isEmpty()) {
            throw new EntityNotFoundException("No se encontro producto con ID: " + id);
        }
        Producto producto = optional.get();
        if (nombre != null && !nombre.isBlank()) producto.setNombre(nombre);
        if (descripcion != null && !descripcion.isBlank()) producto.setDescripcion(descripcion);
        if (precio != null) {
            if (precio < 0) throw new IllegalArgumentException("El precio no puede ser negativo.");
            producto.setPrecio(precio);
        }
        if (stock != null) {
            if (stock < 0) throw new IllegalArgumentException("El stock no puede ser negativo.");
            producto.setStock(stock);
        }
        if (imagen != null && !imagen.isBlank()) producto.setImagen(imagen);
        if (disponible != null) producto.setDisponible(disponible);
        if (categoriaId != null) {
            Optional<Categoria> categoria = categoriaDAO.findById(categoriaId);
            if (categoria.isEmpty()) {
                throw new EntityNotFoundException("No se encontro categoria con ID: " + categoriaId);
            }
            producto.setCategoria(categoria.get());
        }
        productoDAO.update(producto);
        System.out.println("Producto actualizado correctamente.");
    }

    public void eliminarProducto(Long id) {
        Optional<Producto> optional = productoDAO.findById(id);
        if (optional.isEmpty()) {
            throw new EntityNotFoundException("No se encontro producto con ID: " + id);
        }
        productoDAO.delete(id);
        System.out.println("Producto eliminado correctamente.");
    }
}
package service;

import dao.CategoriaDAO;
import entities.Categoria;
import exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public class CategoriaService {

    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    public void crearCategoria(String nombre, String descripcion) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía.");
        }
        Categoria categoria = new Categoria(nombre, descripcion);
        categoriaDAO.save(categoria);
        System.out.println("Categoría creada con ID: " + categoria.getId());
    }

    public List<Categoria> listarCategorias() {
        return categoriaDAO.findAll();
    }

    public void editarCategoria(Long id, String nuevoNombre, String nuevaDescripcion) {
        Optional<Categoria> optional = categoriaDAO.findById(id);
        if (optional.isEmpty()) {
            throw new EntityNotFoundException("No se encontró categoría con ID: " + id);
        }
        Categoria categoria = optional.get();
        if (nuevoNombre != null && !nuevoNombre.isBlank()) {
            categoria.setNombre(nuevoNombre);
        }
        if (nuevaDescripcion != null && !nuevaDescripcion.isBlank()) {
            categoria.setDescripcion(nuevaDescripcion);
        }
        categoriaDAO.update(categoria);
        System.out.println("Categoría actualizada correctamente.");
    }

    public void eliminarCategoria(Long id) {
        Optional<Categoria> optional = categoriaDAO.findById(id);
        if (optional.isEmpty()) {
            throw new EntityNotFoundException("No se encontró categoría con ID: " + id);
        }
        categoriaDAO.delete(id);
        System.out.println("Categoría eliminada correctamente.");
    }
}
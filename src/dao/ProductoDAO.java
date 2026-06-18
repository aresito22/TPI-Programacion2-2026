package dao;

import config.ConexionDB;
import entities.Categoria;
import entities.Producto;
import exception.EntityNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductoDAO implements IBaseDAO<Producto> {

    private Producto mapRow(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getLong("id"));
        p.setNombre(rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setPrecio(rs.getDouble("precio"));
        p.setStock(rs.getInt("stock"));
        p.setImagen(rs.getString("imagen"));
        p.setDisponible(rs.getBoolean("disponible"));
        p.setEliminado(rs.getBoolean("eliminado"));
        p.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        Categoria categoria = new Categoria();
        categoria.setId(rs.getLong("categoria_id"));
        p.setCategoria(categoria);

        return p;
    }

    @Override
    public void save(Producto producto) {
        String sql = "INSERT INTO producto (nombre, descripcion, precio, stock, imagen, disponible, categoria_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setDouble(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());
            ps.setString(5, producto.getImagen());
            ps.setBoolean(6, producto.isDisponible());
            ps.setLong(7, producto.getCategoria().getId());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                producto.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar producto: " + e.getMessage());
        }
    }

    @Override
    public Optional<Producto> findById(Long id) {
        String sql = "SELECT * FROM producto WHERE id = ? AND eliminado = false";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar producto: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Producto> findAll() {
        String sql = "SELECT * FROM producto WHERE eliminado = false";
        List<Producto> lista = new ArrayList<>();
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar productos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void update(Producto producto) {
        String sql = "UPDATE producto SET nombre = ?, descripcion = ?, precio = ?, stock = ?, imagen = ?, disponible = ?, categoria_id = ? WHERE id = ? AND eliminado = false";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setDouble(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());
            ps.setString(5, producto.getImagen());
            ps.setBoolean(6, producto.isDisponible());
            ps.setLong(7, producto.getCategoria().getId());
            ps.setLong(8, producto.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar producto: " + e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "UPDATE producto SET eliminado = true WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar producto: " + e.getMessage());
        }
    }

    public List<Producto> findByCategoria(Long categoriaId) {
        String sql = "SELECT * FROM producto WHERE categoria_id = ? AND eliminado = false";
        List<Producto> lista = new ArrayList<>();
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, categoriaId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar productos por categoria: " + e.getMessage());
        }
        return lista;
    }
}
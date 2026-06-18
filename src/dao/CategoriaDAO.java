package dao;

import config.ConexionDB;
import entities.Categoria;
import exception.EntityNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoriaDAO implements IBaseDAO<Categoria> {

    private Categoria mapRow(ResultSet rs) throws SQLException {
        Categoria c = new Categoria();
        c.setId(rs.getLong("id"));
        c.setNombre(rs.getString("nombre"));
        c.setDescripcion(rs.getString("descripcion"));
        c.setEliminado(rs.getBoolean("eliminado"));
        c.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return c;
    }

    @Override
    public void save(Categoria categoria) {
        String sql = "INSERT INTO categoria (nombre, descripcion) VALUES (?, ?)";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                categoria.setId(keys.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar categoría: " + e.getMessage());
        }
    }

    @Override
    public Optional<Categoria> findById(Long id) {
        String sql = "SELECT * FROM categoria WHERE id = ? AND eliminado = false";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar categoría: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Categoria> findAll() {
        String sql = "SELECT * FROM categoria WHERE eliminado = false";
        List<Categoria> lista = new ArrayList<>();
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar categorías: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void update(Categoria categoria) {
        String sql = "UPDATE categoria SET nombre = ?, descripcion = ? WHERE id = ? AND eliminado = false";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion());
            ps.setLong(3, categoria.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar categoría: " + e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "UPDATE categoria SET eliminado = true WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar categoría: " + e.getMessage());
        }
    }
}
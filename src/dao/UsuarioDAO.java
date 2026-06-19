package dao;

import config.ConexionDB;
import entities.Usuario;
import enums.Rol;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioDAO implements IBaseDAO<Usuario> {

    private Usuario mapRow(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();

        u.setId(rs.getLong("id"));
        u.setNombre(rs.getString("nombre"));
        u.setApellido(rs.getString("apellido"));
        u.setMail(rs.getString("mail"));
        u.setCelular(rs.getString("celular"));
        u.setContrasena(rs.getString("contrasena"));

        u.setRol(Rol.valueOf(rs.getString("rol")));

        u.setEliminado(rs.getBoolean("eliminado"));
        u.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime()

        );

        return u;
    }

    @Override
    public void save(Usuario usuario) {

        String sql = "INSERT INTO usuario " + "(nombre, apellido, mail, celular, contrasena, rol) " + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getMail());
            ps.setString(4, usuario.getCelular());
            ps.setString(5, usuario.getContrasena());
            ps.setString(6, usuario.getRol().name());

            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();

            if (keys.next()) {
                usuario.setId(keys.getLong(1));
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error al guardar usuario: "
                            + e.getMessage()
            );
        }
    }

    @Override
    public Optional<Usuario> findById(Long id) {

        String sql = "SELECT * FROM usuario " + "WHERE id = ? AND eliminado = false";

        try (Connection con = ConexionDB.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario: " + e.getMessage());
        }

        return Optional.empty();
    }

    @Override
    public List<Usuario> findAll() {

        String sql = "SELECT * FROM usuario " + "WHERE eliminado = false";

        List<Usuario> lista = new ArrayList<>();

        try (Connection con = ConexionDB.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void update(Usuario usuario) {

        String sql = "UPDATE usuario " + "SET nombre = ?, apellido = ?, mail = ?, " + "celular = ?, contrasena = ?, rol = ? " + "WHERE id = ? AND eliminado = false";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getMail());
            ps.setString(4, usuario.getCelular());
            ps.setString(5, usuario.getContrasena());
            ps.setString(6, usuario.getRol().name());
            ps.setLong(7, usuario.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar usuario: " + e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {

        String sql = "UPDATE usuario " + "SET eliminado = true " + "WHERE id = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar usuario: " + e.getMessage());
        }
    }


    // Metodo creado para buscar mail y validar que el mail sea unico

    public Optional<Usuario> findByMail(String mail) {

        String sql =
                "SELECT * FROM usuario " +
                        "WHERE mail = ? AND eliminado = false";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, mail);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Error al buscar usuario por mail: "
                            + e.getMessage()
            );
        }

        return Optional.empty();
    }
}

package dao;

import config.ConexionDB;
import entities.Pedido;
import entities.Usuario;
import enums.Estado;
import enums.FormaPago;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PedidoDAO implements IBaseDAO<Pedido> {
    @Override
    public void save(Pedido pedido) {
        String sqlPedido = "INSERT INTO pedido (fecha, estado, total, forma_pago, usuario_id) VALUES (?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_pedido (cantidad, subtotal, pedido_id, producto_id) VALUES (?, ?, ?, ?)";

        Connection con = null;
        try {
            con = ConexionDB.getConexion();

            con.setAutoCommit(false);

            try (PreparedStatement psPedido = con.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                psPedido.setDate(1, Date.valueOf(pedido.getFecha()));
                psPedido.setString(2, pedido.getEstado().name());
                psPedido.setDouble(3, pedido.getTotal());
                psPedido.setString(4, pedido.getFormaPago().name());
                psPedido.setLong(5, pedido.getUsuario().getId());

                psPedido.executeUpdate();

                ResultSet keys = psPedido.getGeneratedKeys();
                if (keys.next()) {
                    pedido.setId(keys.getLong(1));
                }
            }

            try (PreparedStatement psDetalle = con.prepareStatement(sqlDetalle)) {
                for (var detalle : pedido.getDetalles()) {
                    psDetalle.setInt(1, detalle.getCantidad());
                    psDetalle.setDouble(2, detalle.getSubtotal());
                    psDetalle.setLong(3, pedido.getId());
                    psDetalle.setLong(4, detalle.getProducto().getId());
                    psDetalle.addBatch();
                }
                psDetalle.executeBatch();
            }

            con.commit();

        } catch (SQLException e) {

            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    System.out.println("Error en rollback: " + ex.getMessage());
                }
            }
            throw new RuntimeException("Error transaccional al guardar el pedido: " + e.getMessage());
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    System.out.println("Error al cerrar conexion: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public Optional<Pedido> findById(Long id) {
        String sql = "SELECT * FROM pedido WHERE id = ? AND eliminado = false";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Pedido p = new Pedido();
                p.setId(rs.getLong("id"));
                p.setFecha(rs.getDate("fecha").toLocalDate());
                p.setEstado(Estado.valueOf(rs.getString("estado")));
                p.setTotal(rs.getDouble("total"));
                p.setFormaPago(FormaPago.valueOf(rs.getString("forma_pago")));
                p.setEliminado(rs.getBoolean("eliminado"));

                Usuario usuario = new Usuario();
                usuario.setId(rs.getLong("usuario_id"));
                p.setUsuario(usuario);

                return Optional.of(p);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar pedido: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Pedido> findAll() {
        String sql = "SELECT * FROM pedido WHERE eliminado = false";
        List<Pedido> lista = new ArrayList<>();
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Pedido p = new Pedido();
                p.setId(rs.getLong("id"));
                p.setFecha(rs.getDate("fecha").toLocalDate());
                p.setEstado(Estado.valueOf(rs.getString("estado")));
                p.setTotal(rs.getDouble("total"));
                p.setFormaPago(FormaPago.valueOf(rs.getString("forma_pago")));
                p.setEliminado(rs.getBoolean("eliminado"));

                Usuario usuario = new Usuario();
                usuario.setId(rs.getLong("usuario_id"));
                p.setUsuario(usuario);

                lista.add(p);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar pedidos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void update(Pedido pedido) {
        String sql = "UPDATE pedido SET estado = ?, forma_pago = ?, total = ? WHERE id = ? AND eliminado = false";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, pedido.getEstado().name());
            ps.setString(2, pedido.getFormaPago().name());
            ps.setDouble(3, pedido.getTotal());
            ps.setLong(4, pedido.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al modificar pedido: " + e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "UPDATE pedido SET eliminado = true WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al dar de baja logica al pedido: " + e.getMessage());
        }
    }
}
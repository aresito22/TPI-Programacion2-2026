package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    public static final String URL = "jdbc:mysql://localhost:3306/pedidos_db";
    public static final String USER = "root";
    public static final String PASSWORD = "";

    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
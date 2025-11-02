package com.example.sancaranservlet.model;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private final Dotenv dotenv = Dotenv.load();

    public Connection conectar() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");

            String url = dotenv.get("dbUrl");   // cuidado com o nome da variável no .env
            String user = dotenv.get("dbUser");
            String senha = dotenv.get("dbSenha");

            conn = DriverManager.getConnection(url, user, senha); // FALTAVA ISSO

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public void desconectar(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

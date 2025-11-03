package com.example.sancaranservlet.dao;

import com.example.sancaranservlet.model.Setor;
import com.example.sancaranservlet.model.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SetorDAO {

    // Lista todos os setores
    public List<Setor> listarTodos() {
        List<Setor> setores = new ArrayList<>();

        try (Connection conn = new Conexao().conectar()) {
            String sql = "SELECT * FROM Setor ORDER BY id";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Setor setor = new Setor();
                setor.setId(rs.getInt("id"));
                setor.setNome(rs.getString("nome"));
                setor.setDescricao(rs.getString("descricao"));
                setores.add(setor);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return setores;
    }

    // Exclui um setor pelo ID
    public boolean excluir(int id) {
        try (Connection conn = new Conexao().conectar()) {
            String sql = "DELETE FROM Setor WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Insere um novo setor
    public boolean inserir(Setor setor) {
        try (Connection conn = new Conexao().conectar()) {
            String sql = "INSERT INTO Setor (nome, descricao) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, setor.getNome());
            stmt.setString(2, setor.getDescricao());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Busca um setor pelo ID
    public Setor buscarPorId(int id) {
        Setor setor = null;
        try (Connection conn = new Conexao().conectar()) {
            String sql = "SELECT * FROM Setor WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                setor = new Setor();
                setor.setId(rs.getInt("id"));
                setor.setNome(rs.getString("nome"));
                setor.setDescricao(rs.getString("descricao"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return setor;
    }

    // Atualiza um setor existente
    public boolean atualizar(Setor setor) {
        try (Connection conn = new Conexao().conectar()) {
            String sql = "UPDATE Setor SET nome = ?, descricao = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, setor.getNome());
            stmt.setString(2, setor.getDescricao());
            stmt.setInt(3, setor.getId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

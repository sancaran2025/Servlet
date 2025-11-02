package com.example.sancaranservlet.dao;

import com.example.sancaranservlet.model.Setor;
import com.example.sancaranservlet.model.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SetorDAO {

    public List<Setor> listarTodos() {
        List<Setor> setores = new ArrayList<>();

        System.out.println("=== DEBUG SetorDAO.listarTodos() ===");

        try (Connection conn = new Conexao().conectar()) {
            String sql = "SELECT * FROM Setor ORDER BY id";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            int count = 0;
            while (rs.next()) {
                count++;
                Setor setor = new Setor();
                setor.setId(rs.getInt("id"));
                setor.setNome(rs.getString("nome"));
                setor.setDescricao(rs.getString("descricao"));
                setores.add(setor);

                System.out.println("Setor " + count + ": ID=" + setor.getId() + ", Nome=" + setor.getNome());
            }

            System.out.println("Total de setores: " + count);

        } catch (SQLException e) {
            System.out.println("ERRO ao listar setores: " + e.getMessage());
            e.printStackTrace();
        }

        return setores;
    }

    public boolean excluir(int id) {
        System.out.println("=== DEBUG SetorDAO.excluir() ===");
        System.out.println("Excluindo setor ID=" + id);

        try (Connection conn = new Conexao().conectar()) {
            String sql = "DELETE FROM Setor WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            boolean sucesso = rowsAffected > 0;
            System.out.println("Setor excluído: " + sucesso);
            return sucesso;

        } catch (SQLException e) {
            System.out.println("ERRO ao excluir setor: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean inserir(Setor setor) {
        System.out.println("=== DEBUG SetorDAO.inserir() ===");
        System.out.println("Inserindo setor: Nome=" + setor.getNome() + ", Descrição=" + setor.getDescricao());

        try (Connection conn = new Conexao().conectar()) {
            String sql = "INSERT INTO Setor (nome, descricao) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, setor.getNome());
            stmt.setString(2, setor.getDescricao());
            int rowsAffected = stmt.executeUpdate();

            boolean sucesso = rowsAffected > 0;
            System.out.println("Setor inserido: " + sucesso);
            return sucesso;

        } catch (SQLException e) {
            System.out.println("ERRO ao inserir setor: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Setor buscarPorId(int id) {
        System.out.println("=== DEBUG SetorDAO.buscarPorId() ===");
        System.out.println("Buscando setor ID=" + id);

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
                System.out.println("Setor encontrado: " + setor.getNome());
            } else {
                System.out.println("Setor não encontrado para ID=" + id);
            }
        } catch (SQLException e) {
            System.out.println("ERRO ao buscar setor: " + e.getMessage());
            e.printStackTrace();
        }
        return setor;
    }

    public boolean atualizar(Setor setor) {
        System.out.println("=== DEBUG SetorDAO.atualizar() ===");
        System.out.println("Atualizando setor ID=" + setor.getId() + ": Nome=" + setor.getNome());

        try (Connection conn = new Conexao().conectar()) {
            String sql = "UPDATE Setor SET nome = ?, descricao = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, setor.getNome());
            stmt.setString(2, setor.getDescricao());
            stmt.setInt(3, setor.getId());
            int rowsAffected = stmt.executeUpdate();

            boolean sucesso = rowsAffected > 0;
            System.out.println("Setor atualizado: " + sucesso);
            return sucesso;

        } catch (SQLException e) {
            System.out.println("ERRO ao atualizar setor: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
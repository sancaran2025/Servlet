package com.example.sancaranservlet.dao;

import com.example.sancaranservlet.model.Conexao;
import com.example.sancaranservlet.model.Producao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProducaoDAO {
    private Connection conn;

    public ProducaoDAO() {
        conn = new Conexao().conectar();
    }

    public List<Producao> listar() {
        List<Producao> lista = new ArrayList<>();
        String sql = "SELECT p.*, s.nome AS nome_setor FROM Producao p LEFT JOIN Setor s ON p.id_setor = s.id ORDER BY p.id";

        System.out.println("=== DEBUG ProducaoDAO.listar() ===");
        System.out.println("SQL: " + sql);

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            int count = 0;
            while (rs.next()) {
                count++;
                Producao p = new Producao();
                p.setId(rs.getInt("id"));
                p.setDtRegistro(rs.getString("dt_registro"));
                p.setQntProduzida(rs.getInt("qnt_produzida"));
                p.setIdSetor(rs.getInt("id_setor"));

                // Pega o nome do setor, se existir
                String nomeSetor = rs.getString("nome_setor");
                p.setNomeSetor(nomeSetor != null ? nomeSetor : "Setor " + p.getIdSetor());

                lista.add(p);

                System.out.println("Produção " + count + ": ID=" + p.getId() +
                        ", Data=" + p.getDtRegistro() +
                        ", Qnt=" + p.getQntProduzida() +
                        ", ID_Setor=" + p.getIdSetor() +
                        ", Nome_Setor=" + p.getNomeSetor());
            }

            System.out.println("Total de produções encontradas: " + count);

        } catch (SQLException e) {
            System.out.println("ERRO na consulta: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    public boolean inserir(Producao p) {
        String sql = "INSERT INTO Producao (dt_registro, qnt_produzida, id_setor) VALUES (?, ?, ?)";

        System.out.println("=== DEBUG ProducaoDAO.inserir() ===");
        System.out.println("Inserindo: Data=" + p.getDtRegistro() + ", Qnt=" + p.getQntProduzida() + ", Setor=" + p.getIdSetor());

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDate(1, Date.valueOf(p.getDtRegistro()));
            ps.setInt(2, p.getQntProduzida());
            ps.setInt(3, p.getIdSetor());
            ps.executeUpdate();

            System.out.println("Produção inserida com sucesso!");
            return true;
        } catch (SQLException e) {
            System.out.println("ERRO ao inserir: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean atualizar(Producao p) {
        String sql = "UPDATE Producao SET dt_registro = ?, qnt_produzida = ?, id_setor = ? WHERE id = ?";

        System.out.println("=== DEBUG ProducaoDAO.atualizar() ===");
        System.out.println("Atualizando ID=" + p.getId() + ": Data=" + p.getDtRegistro() + ", Qnt=" + p.getQntProduzida() + ", Setor=" + p.getIdSetor());

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDate(1, Date.valueOf(p.getDtRegistro()));
            ps.setInt(2, p.getQntProduzida());
            ps.setInt(3, p.getIdSetor());
            ps.setInt(4, p.getId());
            ps.executeUpdate();

            System.out.println("Produção atualizada com sucesso!");
            return true;
        } catch (SQLException e) {
            System.out.println("ERRO ao atualizar: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM Producao WHERE id = ?";

        System.out.println("=== DEBUG ProducaoDAO.excluir() ===");
        System.out.println("Excluindo produção ID=" + id);

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();

            System.out.println("Produção excluída com sucesso!");
            return true;
        } catch (SQLException e) {
            System.out.println("ERRO ao excluir: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Producao buscarPorId(int id) {
        String sql = "SELECT p.*, s.nome AS nome_setor FROM Producao p LEFT JOIN Setor s ON p.id_setor = s.id WHERE p.id = ?";

        System.out.println("=== DEBUG ProducaoDAO.buscarPorId() ===");
        System.out.println("Buscando produção ID=" + id);

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Producao p = new Producao();
                p.setId(rs.getInt("id"));
                p.setDtRegistro(rs.getString("dt_registro"));
                p.setQntProduzida(rs.getInt("qnt_produzida"));
                p.setIdSetor(rs.getInt("id_setor"));

                String nomeSetor = rs.getString("nome_setor");
                p.setNomeSetor(nomeSetor != null ? nomeSetor : "Setor " + p.getIdSetor());

                System.out.println("Produção encontrada: ID=" + p.getId() + ", Data=" + p.getDtRegistro());
                return p;
            }
        } catch (SQLException e) {
            System.out.println("ERRO ao buscar por ID: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Produção não encontrada para ID=" + id);
        return null;
    }
}
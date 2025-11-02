package com.example.sancaranservlet.dao;

import com.example.sancaranservlet.model.Meta;
import com.example.sancaranservlet.model.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MetaDAO {

    public List<Meta> listarTodas() {
        List<Meta> metas = new ArrayList<>();
        Conexao conexao = new Conexao();
        Connection conn = conexao.conectar();

        String sql = "SELECT * FROM Meta ORDER BY data_inicio DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Meta meta = new Meta();
                meta.setId(rs.getInt("id"));
                meta.setDataInicio(rs.getDate("data_inicio"));
                meta.setDataFim(rs.getDate("data_fim"));
                meta.setObservacoes(rs.getString("observacoes"));
                meta.setDescricao(rs.getString("descricao"));
                meta.setStatus(rs.getString("status").charAt(0));
                meta.setIdProducao(rs.getInt("id_producao"));
                metas.add(meta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conexao.desconectar(conn);
        }
        return metas;
    }

    public boolean inserir(Meta meta) {
        Conexao conexao = new Conexao();
        Connection conn = conexao.conectar();
        boolean sucesso = false;

        String sql = "INSERT INTO Meta (data_inicio, data_fim, observacoes, descricao, status, id_producao) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, meta.getDataInicio());
            stmt.setDate(2, meta.getDataFim());
            stmt.setString(3, meta.getObservacoes());
            stmt.setString(4, meta.getDescricao());
            stmt.setString(5, String.valueOf(meta.getStatus()));
            stmt.setInt(6, meta.getIdProducao());

            sucesso = stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conexao.desconectar(conn);
        }
        return sucesso;
    }

    public boolean atualizar(Meta meta) {
        Conexao conexao = new Conexao();
        Connection conn = conexao.conectar();
        boolean sucesso = false;

        String sql = "UPDATE Meta SET data_inicio=?, data_fim=?, observacoes=?, descricao=?, status=?, id_producao=? WHERE id=?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, meta.getDataInicio());
            stmt.setDate(2, meta.getDataFim());
            stmt.setString(3, meta.getObservacoes());
            stmt.setString(4, meta.getDescricao());
            stmt.setString(5, String.valueOf(meta.getStatus()));
            stmt.setInt(6, meta.getIdProducao());
            stmt.setInt(7, meta.getId());

            sucesso = stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conexao.desconectar(conn);
        }
        return sucesso;
    }

    public boolean excluir(int id) {
        Conexao conexao = new Conexao();
        Connection conn = conexao.conectar();
        boolean sucesso = false;

        String sql = "DELETE FROM Meta WHERE id=?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            sucesso = stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conexao.desconectar(conn);
        }
        return sucesso;
    }

    public Meta buscarPorId(int id) {
        Meta meta = null;
        Conexao conexao = new Conexao();
        Connection conn = conexao.conectar();

        String sql = "SELECT * FROM Meta WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                meta = new Meta();
                meta.setId(rs.getInt("id"));
                meta.setDataInicio(rs.getDate("data_inicio"));
                meta.setDataFim(rs.getDate("data_fim"));
                meta.setObservacoes(rs.getString("observacoes"));
                meta.setDescricao(rs.getString("descricao"));
                meta.setStatus(rs.getString("status").charAt(0));
                meta.setIdProducao(rs.getInt("id_producao"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conexao.desconectar(conn);
        }
        return meta;
    }
}
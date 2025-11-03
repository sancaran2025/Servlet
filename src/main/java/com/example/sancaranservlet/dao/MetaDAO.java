package com.example.sancaranservlet.dao;

import com.example.sancaranservlet.model.Meta;
import com.example.sancaranservlet.model.Conexao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MetaDAO {

    // Lista todas as metas do banco, ordenadas pela data de início mais recente
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
            e.printStackTrace(); // Loga o erro caso dê problema no SQL
        } finally {
            conexao.desconectar(conn); // Sempre fecha a conexão
        }
        return metas;
    }

    // Filtra metas por status e/ou datas (data início e data fim)
    public List<Meta> filtrarMetas(String status, String dataInicio, String dataFim) {
        List<Meta> metas = new ArrayList<>();
        Conexao conexao = new Conexao();
        Connection conn = conexao.conectar();

        // Monta a query dinamicamente de acordo com os filtros
        StringBuilder sql = new StringBuilder("SELECT * FROM Meta WHERE 1=1");
        List<Object> parametros = new ArrayList<>();

        if (status != null && !status.isEmpty()) {
            sql.append(" AND status = ?");
            parametros.add(status);
        }

        if (dataInicio != null && !dataInicio.isEmpty()) {
            sql.append(" AND data_inicio >= ?");
            parametros.add(Date.valueOf(dataInicio));
        }

        if (dataFim != null && !dataFim.isEmpty()) {
            sql.append(" AND data_fim <= ?");
            parametros.add(Date.valueOf(dataFim));
        }

        sql.append(" ORDER BY data_inicio DESC");

        try (PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            // Passa os parâmetros para a query
            for (int i = 0; i < parametros.size(); i++) {
                stmt.setObject(i + 1, parametros.get(i));
            }

            ResultSet rs = stmt.executeQuery();
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

    // Insere uma nova meta no banco
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

            sucesso = stmt.executeUpdate() > 0; // true se inseriu alguma linha
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conexao.desconectar(conn);
        }
        return sucesso;
    }

    // Atualiza uma meta existente
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

    // Exclui uma meta pelo ID
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

    // Busca uma meta específica pelo ID
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

    // Busca todas as metas com um status específico
    public List<Meta> buscarPorStatus(char status) {
        List<Meta> metas = new ArrayList<>();
        Conexao conexao = new Conexao();
        Connection conn = conexao.conectar();

        String sql = "SELECT * FROM Meta WHERE status = ? ORDER BY data_inicio DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, String.valueOf(status));
            ResultSet rs = stmt.executeQuery();

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

    // Conta quantas metas existem com um determinado status
    public int contarPorStatus(char status) {
        int count = 0;
        Conexao conexao = new Conexao();
        Connection conn = conexao.conectar();

        String sql = "SELECT COUNT(*) FROM Meta WHERE status = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, String.valueOf(status));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conexao.desconectar(conn);
        }
        return count;
    }

    // Retorna todas as metas que já passaram da data fim e ainda não estão concluídas
    public List<Meta> buscarMetasVencidas() {
        List<Meta> metas = new ArrayList<>();
        Conexao conexao = new Conexao();
        Connection conn = conexao.conectar();

        String sql = "SELECT * FROM Meta WHERE data_fim < CURDATE() AND status != 'C' ORDER BY data_fim ASC";

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
}

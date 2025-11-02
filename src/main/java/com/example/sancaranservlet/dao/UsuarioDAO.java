package com.example.sancaranservlet.dao;

import com.example.sancaranservlet.model.Conexao;
import com.example.sancaranservlet.model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private final Conexao conexao;

    public UsuarioDAO() {
        this.conexao = new Conexao();
    }

    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT u.*, f.fone as telefone FROM usuario u LEFT JOIN fone f ON u.id = f.id_usuario ORDER BY u.id";

        try (Connection conn = conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario u = extrairUsuarioResultSet(rs);
                usuarios.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuarios;
    }

    // MÉTODO SIMPLES PARA INSERIR (sem telefone - para compatibilidade)
    public boolean inserir(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, cpf, cep, tipo, email, cargo, senha) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpf());
            stmt.setString(3, usuario.getCep());
            stmt.setString(4, String.valueOf(usuario.getTipo()));
            stmt.setString(5, usuario.getEmail());
            stmt.setString(6, usuario.getCargo());
            stmt.setString(7, usuario.getSenha());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean inserirUsuarioComTelefone(Usuario usuario, String telefone) {
        Connection conn = null;
        try {
            conn = conexao.conectar();
            conn.setAutoCommit(false);

            // 1. INSERIR USUÁRIO
            String sqlUsuario = "INSERT INTO usuario (nome, cpf, cep, tipo, email, cargo, senha) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmtUsuario = conn.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS);

            stmtUsuario.setString(1, usuario.getNome());
            stmtUsuario.setString(2, usuario.getCpf());
            stmtUsuario.setString(3, usuario.getCep());
            stmtUsuario.setString(4, String.valueOf(usuario.getTipo()));
            stmtUsuario.setString(5, usuario.getEmail());
            stmtUsuario.setString(6, usuario.getCargo());
            stmtUsuario.setString(7, usuario.getSenha());

            int affectedRows = stmtUsuario.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                return false;
            }

            // 2. OBTER ID DO USUÁRIO CRIADO
            int idUsuario;
            try (ResultSet generatedKeys = stmtUsuario.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    idUsuario = generatedKeys.getInt(1);
                } else {
                    conn.rollback();
                    return false;
                }
            }

            // 3. INSERIR TELEFONE
            if (telefone != null && !telefone.trim().isEmpty()) {
                String sqlFone = "INSERT INTO fone (fone, id_usuario) VALUES (?, ?)";
                PreparedStatement stmtFone = conn.prepareStatement(sqlFone);
                stmtFone.setString(1, telefone);
                stmtFone.setInt(2, idUsuario);
                stmtFone.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    public boolean atualizar(Usuario usuario) {
        String sql = "UPDATE usuario SET nome = ?, cpf = ?, cep = ?, email = ?, cargo = ?, tipo = ? WHERE id = ?";

        try (Connection conn = conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpf());
            stmt.setString(3, usuario.getCep());
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, usuario.getCargo());
            stmt.setString(6, String.valueOf(usuario.getTipo()));
            stmt.setInt(7, usuario.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // MÉTODO PARA ATUALIZAR TELEFONE (QUE ESTAVA FALTANDO)
    public boolean atualizarTelefone(int idUsuario, String novoTelefone) {
        Connection conn = null;
        try {
            conn = conexao.conectar();
            conn.setAutoCommit(false);

            // Primeiro verifica se já existe um telefone para este usuário
            String sqlSelect = "SELECT id FROM fone WHERE id_usuario = ?";
            PreparedStatement stmtSelect = conn.prepareStatement(sqlSelect);
            stmtSelect.setInt(1, idUsuario);
            ResultSet rs = stmtSelect.executeQuery();

            if (rs.next()) {
                // Já existe telefone - ATUALIZA
                int idFone = rs.getInt("id");
                String sqlUpdate = "UPDATE fone SET fone = ? WHERE id = ?";
                PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate);
                stmtUpdate.setString(1, novoTelefone);
                stmtUpdate.setInt(2, idFone);
                stmtUpdate.executeUpdate();
            } else {
                // Não existe telefone - INSERE
                String sqlInsert = "INSERT INTO fone (fone, id_usuario) VALUES (?, ?)";
                PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert);
                stmtInsert.setString(1, novoTelefone);
                stmtInsert.setInt(2, idUsuario);
                stmtInsert.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    public boolean excluir(int id) {
        // Por causa do ON DELETE CASCADE na tabela fone, podemos deletar apenas o usuário
        String sql = "DELETE FROM usuario WHERE id = ?";

        try (Connection conn = conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Usuario buscarPorId(int id) {
        String sql = "SELECT u.*, f.fone as telefone FROM usuario u LEFT JOIN fone f ON u.id = f.id_usuario WHERE u.id = ?";
        Usuario usuario = null;

        try (Connection conn = conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = extrairUsuarioResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuario;
    }

    public Usuario validarLogin(String email, String senha) {
        String sql = "SELECT u.*, f.fone as telefone FROM usuario u LEFT JOIN fone f ON u.id = f.id_usuario WHERE u.email = ? AND u.senha = ?";
        Usuario usuario = null;

        try (Connection conn = conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = extrairUsuarioResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuario;
    }

    // VERIFICAR SE USUÁRIO JÁ EXISTE (CPF ou EMAIL)
    public boolean usuarioExiste(String cpf, String email) {
        String sql = "SELECT id FROM usuario WHERE cpf = ? OR email = ?";

        try (Connection conn = conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            stmt.setString(2, email);

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT u.*, f.fone as telefone FROM usuario u LEFT JOIN fone f ON u.id = f.id_usuario WHERE u.email = ?";
        Usuario usuario = null;

        try (Connection conn = conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = extrairUsuarioResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuario;
    }

    // MÉTODO AUXILIAR PARA EXTRAIR DADOS DO RESULTSET
    private Usuario extrairUsuarioResultSet(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setCpf(rs.getString("cpf"));
        usuario.setCep(rs.getString("cep"));
        usuario.setEmail(rs.getString("email"));
        usuario.setCargo(rs.getString("cargo"));
        usuario.setSenha(rs.getString("senha"));
        usuario.setTelefone(rs.getString("telefone"));

        String tipo = rs.getString("tipo");
        if (tipo != null && !tipo.isEmpty()) {
            usuario.setTipo(tipo.charAt(0));
        }

        return usuario;
    }
}
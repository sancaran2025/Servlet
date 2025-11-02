package com.example.sancaranservlet.servlet;

import com.example.sancaranservlet.dao.UsuarioDAO;
import com.example.sancaranservlet.model.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

@WebServlet("/AdmServlet")
public class AdmServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        UsuarioDAO dao = new UsuarioDAO();
        List<Usuario> todosUsuarios = dao.listarTodos();
        List<Usuario> administradores = new ArrayList<>();

        for (Usuario usuario : todosUsuarios) {
            if (usuario.getTipo() == 'A') {
                administradores.add(usuario);
            }
        }

        request.setAttribute("adms", administradores);
        RequestDispatcher rd = request.getRequestDispatcher("/adm/ListaADM.jsp");
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        UsuarioDAO dao = new UsuarioDAO();
        String acao = request.getParameter("acao");
        String mensagem = "";

        if ("inserir".equals(acao)) {
            Usuario u = new Usuario();
            u.setNome(request.getParameter("nome"));
            u.setEmail(request.getParameter("email"));
            u.setSenha(request.getParameter("senha"));
            u.setTipo('A');
            u.setCpf(request.getParameter("cpf")); // NOVO
            u.setCep(request.getParameter("cep")); // NOVO
            u.setCargo(request.getParameter("cargo")); // NOVO

            String telefone = request.getParameter("telefone"); // NOVO

            // CORREÇÃO: usar inserirUsuarioComTelefone
            if (dao.inserirUsuarioComTelefone(u, telefone)) {
                mensagem = "Administrador cadastrado com sucesso!";
            } else {
                mensagem = "Erro ao cadastrar administrador!";
            }

        } else if ("atualizar".equals(acao)) {
            Usuario u = new Usuario();
            u.setId(Integer.parseInt(request.getParameter("id")));
            u.setNome(request.getParameter("nome"));
            u.setEmail(request.getParameter("email"));
            u.setSenha(request.getParameter("senha"));
            u.setTipo('A');
            u.setCpf(request.getParameter("cpf")); // NOVO
            u.setCep(request.getParameter("cep")); // NOVO
            u.setCargo(request.getParameter("cargo")); // NOVO

            if (dao.atualizar(u)) {
                mensagem = "Administrador atualizado com sucesso!";

                // Atualizar telefone se fornecido
                String telefone = request.getParameter("telefone");
                if (telefone != null && !telefone.trim().isEmpty()) {
                    dao.atualizarTelefone(u.getId(), telefone);
                }
            } else {
                mensagem = "Erro ao atualizar administrador!";
            }

        } else if ("excluir".equals(acao)) {
            int id = Integer.parseInt(request.getParameter("id"));
            if (dao.excluir(id)) {
                mensagem = "Administrador excluído com sucesso!";
            } else {
                mensagem = "Erro ao excluir administrador!";
            }
        }

        HttpSession session = request.getSession();
        session.setAttribute("mensagem", mensagem);
        response.sendRedirect(request.getContextPath() + "/AdmServlet");
    }
}
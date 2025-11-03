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

    // Método chamado quando a requisição é GET
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Evita cache da página
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        // Instancia o DAO e busca todos os usuários
        UsuarioDAO dao = new UsuarioDAO();
        List<Usuario> todosUsuarios = dao.listarTodos();

        // Filtra apenas os administradores (tipo 'A')
        List<Usuario> administradores = new ArrayList<>();
        for (Usuario usuario : todosUsuarios) {
            if (usuario.getTipo() == 'A') {
                administradores.add(usuario);
            }
        }

        // Coloca a lista de administradores no request e encaminha para JSP
        request.setAttribute("adms", administradores);
        RequestDispatcher rd = request.getRequestDispatcher("/adm/ListaADM.jsp");
        rd.forward(request, response);
    }

    // Método chamado quando a requisição é POST
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Define encoding para UTF-8
        request.setCharacterEncoding("UTF-8");
        UsuarioDAO dao = new UsuarioDAO();
        String acao = request.getParameter("acao"); // Ação enviada no form
        String mensagem = "";

        // INSERIR novo administrador
        if ("inserir".equals(acao)) {
            Usuario u = new Usuario();
            u.setNome(request.getParameter("nome"));
            u.setEmail(request.getParameter("email"));
            u.setSenha(request.getParameter("senha"));
            u.setTipo('A'); // Define tipo como administrador
            u.setCpf(request.getParameter("cpf"));
            u.setCep(request.getParameter("cep"));
            u.setCargo(request.getParameter("cargo"));
            String telefone = request.getParameter("telefone");

            // Insere usuário e telefone
            if (dao.inserirUsuarioComTelefone(u, telefone)) {
                mensagem = "Administrador cadastrado com sucesso!";
            } else {
                mensagem = "Erro ao cadastrar administrador!";
            }

            // ATUALIZAR administrador existente
        } else if ("atualizar".equals(acao)) {
            Usuario u = new Usuario();
            u.setId(Integer.parseInt(request.getParameter("id")));
            u.setNome(request.getParameter("nome"));
            u.setEmail(request.getParameter("email"));
            u.setSenha(request.getParameter("senha"));
            u.setTipo('A');
            u.setCpf(request.getParameter("cpf"));
            u.setCep(request.getParameter("cep"));
            u.setCargo(request.getParameter("cargo"));

            if (dao.atualizar(u)) {
                mensagem = "Administrador atualizado com sucesso!";

                // Atualiza telefone se foi fornecido
                String telefone = request.getParameter("telefone");
                if (telefone != null && !telefone.trim().isEmpty()) {
                    dao.atualizarTelefone(u.getId(), telefone);
                }
            } else {
                mensagem = "Erro ao atualizar administrador!";
            }

            // EXCLUIR administrador
        } else if ("excluir".equals(acao)) {
            int id = Integer.parseInt(request.getParameter("id"));
            if (dao.excluir(id)) {
                mensagem = "Administrador excluído com sucesso!";
            } else {
                mensagem = "Erro ao excluir administrador!";
            }
        }

        // Salva mensagem na sessão e redireciona de volta para listar administradores
        HttpSession session = request.getSession();
        session.setAttribute("mensagem", mensagem);
        response.sendRedirect(request.getContextPath() + "/AdmServlet");
    }
}

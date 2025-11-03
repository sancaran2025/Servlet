package com.example.sancaranservlet.servlet;

import com.example.sancaranservlet.dao.UsuarioDAO;
import com.example.sancaranservlet.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    // Método que processa requisições POST (quando o usuário envia o formulário de login)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtém os dados do formulário
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        // Cria instância do DAO para consultar o banco
        UsuarioDAO dao = new UsuarioDAO();
        Usuario usuario = dao.validarLogin(email, senha); // verifica email e senha

        // Se encontrou o usuário
        if (usuario != null) {
            // Verifica se o usuário é administrador (tipo 'A')
            if (Character.toUpperCase(usuario.getTipo()) == 'A') {
                // Cria sessão e armazena o usuário
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);

                // Redireciona para a área restrita de administradores
                response.sendRedirect("AreaRestrita.jsp");
            } else {
                // Usuário encontrado, mas não é admin: redireciona para página de erro
                response.sendRedirect("LoginRestrito.jsp?erro=2");
            }
        } else {
            // Usuário não encontrado ou senha incorreta: redireciona para página de erro
            response.sendRedirect("LoginRestrito.jsp?erro=1");
        }
    }
}

package com.example.sancaranservlet.servlet;

import com.example.sancaranservlet.dao.UsuarioDAO;
import com.example.sancaranservlet.model.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "EntrarContaServlet", value = "/entrar-conta")
public class EntrarContaServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO = new UsuarioDAO(); // DAO para manipulação de usuários

    // POST: recebe os dados do formulário de login
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String senha = request.getParameter("password");

        // Valida campos obrigatórios
        if (email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty()) {
            response.sendRedirect("EntrarConta.jsp?error=" +
                    URLEncoder.encode("Email e senha são obrigatórios", StandardCharsets.UTF_8));
            return;
        }

        // Valida formato do email
        if (!validarEmail(email)) {
            response.sendRedirect("EntrarConta.jsp?error=" +
                    URLEncoder.encode("Formato de email inválido", StandardCharsets.UTF_8));
            return;
        }

        // Verifica login no banco
        Usuario usuario = usuarioDAO.validarLogin(email, senha);

        if (usuario != null) {
            // Login correto: cria sessão e redireciona para painel
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuario);
            session.setAttribute("usuarioLogado", true);
            session.setAttribute("usuarioNome", usuario.getNome());
            session.setAttribute("usuarioTipo", usuario.getTipo());
            response.sendRedirect("painelGeral.jsp");

        } else {
            // Login incorreto: volta para login com mensagem
            response.sendRedirect("EntrarConta.jsp?error=" +
                    URLEncoder.encode("Email ou senha incorretos", StandardCharsets.UTF_8));
        }
    }

    // GET: apenas encaminha para a página de login
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("EntrarConta.jsp");
        dispatcher.forward(request, response);
    }

    // Validação simples de email usando regex
    private boolean validarEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(regex);
    }
}

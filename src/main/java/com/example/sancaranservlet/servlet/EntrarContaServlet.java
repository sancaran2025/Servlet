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

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String senha = request.getParameter("password");

        System.out.println("=== TENTATIVA DE LOGIN ===");
        System.out.println("Email: " + email);
        System.out.println("Senha: " + (senha != null ? "***" : "null"));

        // Validações básicas
        if (email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty()) {
            response.sendRedirect("EntrarConta.jsp?error=" +
                    URLEncoder.encode("Email e senha são obrigatórios", StandardCharsets.UTF_8));
            return;
        }

        // Validar formato do email
        if (!validarEmail(email)) {
            response.sendRedirect("EntrarConta.jsp?error=" +
                    URLEncoder.encode("Formato de email inválido", StandardCharsets.UTF_8));
            return;
        }

        // Buscar usuário no banco
        Usuario usuario = usuarioDAO.validarLogin(email, senha);

        if (usuario != null) {
            System.out.println("✅ LOGIN BEM-SUCEDIDO: " + usuario.getNome());

            // Criar sessão
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuario);
            session.setAttribute("usuarioLogado", true);
            session.setAttribute("usuarioNome", usuario.getNome());
            session.setAttribute("usuarioTipo", usuario.getTipo());

            // Redirecionar para painel
            response.sendRedirect("painelGeral.jsp");

        } else {
            System.out.println("❌ LOGIN FALHOU para: " + email);
            response.sendRedirect("EntrarConta.jsp?error=" +
                    URLEncoder.encode("Email ou senha incorretos", StandardCharsets.UTF_8));
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Redirecionar direto para a página de login
        RequestDispatcher dispatcher = request.getRequestDispatcher("EntrarConta.jsp");
        dispatcher.forward(request, response);
    }

    private boolean validarEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(regex);
    }
}
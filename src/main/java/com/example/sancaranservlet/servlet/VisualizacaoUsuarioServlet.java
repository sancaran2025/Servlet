package com.example.sancaranservlet.servlet;

import com.example.sancaranservlet.dao.UsuarioDAO;
import com.example.sancaranservlet.model.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

/**
 * Servlet responsável pela visualização de usuários cadastrados.
 * Recupera todos os usuários do sistema e encaminha para o JSP de adm.
 */
@WebServlet("/UsuarioVisualizacaoServlet")
public class VisualizacaoUsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Inicializa o DAO responsável pelos usuários
        UsuarioDAO dao = new UsuarioDAO();

        // Recupera todos os usuários cadastrados
        List<Usuario> todosUsuarios = dao.listarTodos();

        // Define o atributo "usuarios" que será acessível pelo JSP
        request.setAttribute("usuarios", todosUsuarios);

        // Encaminha a requisição para o JSP de visualização de usuários
        RequestDispatcher rd = request.getRequestDispatcher("/adm/adm-visualizacao.jsp");
        rd.forward(request, response);
    }

    // SEM doPost, pois esta servlet é apenas para visualização
}

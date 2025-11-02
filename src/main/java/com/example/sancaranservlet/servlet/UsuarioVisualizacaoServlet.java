package com.example.sancaranservlet.servlet;

import com.example.sancaranservlet.dao.UsuarioDAO;
import com.example.sancaranservlet.model.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/UsuarioVisualizacaoServlet")
public class UsuarioVisualizacaoServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UsuarioDAO dao = new UsuarioDAO();
        List<Usuario> todosUsuarios = dao.listarTodos();

        request.setAttribute("usuarios", todosUsuarios);
        RequestDispatcher rd = request.getRequestDispatcher("/adm/adm-visualizacao.jsp");
        rd.forward(request, response);
    }

    // SEM doPost - porque é só visualização!
}
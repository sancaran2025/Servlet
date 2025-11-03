package com.example.sancaranservlet.servlet;

import com.example.sancaranservlet.dao.SetorDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/ExcluirSetorServlet")
public class ExcluirSetorServlet extends HttpServlet {

    // POST: recebe requisição para excluir um setor
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Obtém o ID do setor enviado pelo formulário
        String idStr = request.getParameter("id");

        // Verifica se o ID não é nulo ou vazio
        if (idStr != null && !idStr.isEmpty()) {
            int id = Integer.parseInt(idStr); // converte para int
            SetorDAO dao = new SetorDAO();   // instancia o DAO de Setor
            dao.excluir(id);                  // chama método para excluir o setor do banco
        }

        // Redireciona de volta para a lista de setores
        response.sendRedirect("SetorServlet");
    }
}

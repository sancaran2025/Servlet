package com.example.sancaranservlet.servlet;

import com.example.sancaranservlet.dao.SetorDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet("/ExcluirSetorServlet")
public class ExcluirSetorServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idStr = request.getParameter("id");

        if (idStr != null && !idStr.isEmpty()) {
            int id = Integer.parseInt(idStr);
            SetorDAO dao = new SetorDAO();
            dao.excluir(id);
        }

        response.sendRedirect("SetorServlet");
    }
}
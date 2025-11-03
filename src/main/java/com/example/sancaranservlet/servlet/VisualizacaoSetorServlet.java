package com.example.sancaranservlet.servlet;

import com.example.sancaranservlet.dao.SetorDAO;
import com.example.sancaranservlet.model.Setor;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Servlet responsável pela visualização de setores.
 * Recupera todos os setores cadastrados no sistema e encaminha para o JSP correspondente.
 */
@WebServlet("/VisualizacaoSetorServlet")
public class VisualizacaoSetorServlet extends HttpServlet {

    private SetorDAO setorDAO;

    @Override
    public void init() {
        // Inicializa o DAO responsável pelo acesso aos setores
        setorDAO = new SetorDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Recupera todos os setores do banco de dados
            List<Setor> setores = setorDAO.listarTodos();

            // Define o atributo "setores" que será acessível pelo JSP
            request.setAttribute("setores", setores);

            // Encaminha a requisição para o JSP de visualização de setores
            RequestDispatcher dispatcher = request.getRequestDispatcher("/setor/visualizacaoSetor.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            // Em caso de erro, redireciona para uma página de erro genérica
            request.setAttribute("erro", "Erro ao carregar setores: " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/erro.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Encaminha requisições POST para GET para manter comportamento consistente
        doGet(request, response);
    }
}

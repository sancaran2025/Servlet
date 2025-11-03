package com.example.sancaranservlet.servlet;

import com.example.sancaranservlet.dao.ProducaoDAO;
import com.example.sancaranservlet.model.Producao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Servlet responsável pela visualização de produções.
 * Busca todas as produções cadastradas no sistema e envia para o JSP correspondente.
 */
@WebServlet("/VisualizacaoProducaoServlet")
public class VisualizacaoProducaoServlet extends HttpServlet {

    private ProducaoDAO producaoDAO;

    @Override
    public void init() {
        // Inicializa o DAO para comunicação com o banco
        producaoDAO = new ProducaoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Recupera todas as produções do banco de dados
            List<Producao> producoes = producaoDAO.listar();

            // Define o atributo "producoes" que será acessível pelo JSP
            request.setAttribute("producoes", producoes);

            // Encaminha a requisição para o JSP de visualização de produção
            RequestDispatcher dispatcher = request.getRequestDispatcher("/producao/visualizacaoProducao.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            // Em caso de erro, redireciona para uma página de erro genérica
            request.setAttribute("erro", "Erro ao carregar produções: " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/erro.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Encaminha POST para GET, mantendo comportamento idêntico
        doGet(request, response);
    }
}

package com.example.sancaranservlet.servlet;

import com.example.sancaranservlet.dao.ProducaoDAO;
import com.example.sancaranservlet.dao.SetorDAO;
import com.example.sancaranservlet.model.Producao;
import com.example.sancaranservlet.model.Setor;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Servlet responsável pelo gerenciamento das Produções.
 * Permite listar, cadastrar, atualizar e excluir registros de produção.
 */
@WebServlet("/ProducaoServlet")
public class ProducaoServlet extends HttpServlet {

    private ProducaoDAO producaoDAO;
    private SetorDAO setorDAO;

    @Override
    public void init() {
        // Inicializa os DAOs para acesso ao banco de dados
        producaoDAO = new ProducaoDAO();
        setorDAO = new SetorDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");

        try {
            // Se nenhuma ação for especificada, ou for "listar", exibe a lista de produções
            if (acao == null || acao.isEmpty() || acao.equals("listar")) {
                listarProducoes(request, response);
            }
            // Se a ação for "excluir", realiza a exclusão de um registro
            else if ("excluir".equals(acao)) {
                excluirProducao(request, response);
            }
            // Para qualquer outra ação via GET, retorna à listagem
            else {
                listarProducoes(request, response);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Captura os parâmetros do formulário
        String idStr = request.getParameter("id");
        String dtRegistro = request.getParameter("dt_registro");
        String qntStr = request.getParameter("qnt_produzida");
        String idSetorStr = request.getParameter("id_setor");

        // Valida campos obrigatórios
        if (dtRegistro == null || dtRegistro.isEmpty() ||
                qntStr == null || qntStr.isEmpty() ||
                idSetorStr == null || idSetorStr.isEmpty()) {

            HttpSession session = request.getSession();
            session.setAttribute("mensagem", "Erro: Todos os campos são obrigatórios!");
            response.sendRedirect("ProducaoServlet");
            return;
        }

        int qntProduzida = Integer.parseInt(qntStr);
        int idSetor = Integer.parseInt(idSetorStr);

        Producao producao = new Producao();
        producao.setDtRegistro(dtRegistro);
        producao.setQntProduzida(qntProduzida);
        producao.setIdSetor(idSetor);

        try {
            if (idStr == null || idStr.isEmpty()) {
                // Inserção de nova produção
                boolean sucesso = producaoDAO.inserir(producao);
                HttpSession session = request.getSession();
                session.setAttribute("mensagem", sucesso ?
                        "Produção cadastrada com sucesso!" :
                        "Erro ao cadastrar produção!");
            } else {
                // Atualização de produção existente
                int id = Integer.parseInt(idStr);
                producao.setId(id);
                boolean sucesso = producaoDAO.atualizar(producao);
                HttpSession session = request.getSession();
                session.setAttribute("mensagem", sucesso ?
                        "Produção atualizada com sucesso!" :
                        "Erro ao atualizar produção!");
            }
        } catch (Exception e) {
            HttpSession session = request.getSession();
            session.setAttribute("mensagem", "Erro ao salvar produção: " + e.getMessage());
        }

        // Redireciona de volta para a lista de produções
        response.sendRedirect("ProducaoServlet");
    }

    /**
     * Lista todas as produções e os setores disponíveis.
     * Prepara os dados para exibição na JSP.
     */
    private void listarProducoes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Producao> producoes = producaoDAO.listar();
        List<Setor> setores = setorDAO.listarTodos(); // Usado no modal de cadastro/edição

        // Atribui os dados à requisição para uso na JSP
        request.setAttribute("producoes", producoes);
        request.setAttribute("setores", setores);

        // Encaminha para a JSP responsável pela listagem
        RequestDispatcher dispatcher = request.getRequestDispatcher("/producao/listaProducao.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Exclui uma produção com base no ID fornecido.
     */
    private void excluirProducao(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        boolean sucesso = producaoDAO.excluir(id);

        HttpSession session = request.getSession();
        session.setAttribute("mensagem", sucesso ?
                "Produção excluída com sucesso!" :
                "Erro ao excluir produção!");

        // Redireciona de volta para a listagem
        response.sendRedirect("ProducaoServlet");
    }
}

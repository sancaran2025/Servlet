package com.example.sancaranservlet.servlet;

import com.example.sancaranservlet.dao.SetorDAO;
import com.example.sancaranservlet.model.Setor;
import com.example.sancaranservlet.model.Usuario;
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
 * Servlet responsável pelo gerenciamento dos Setores.
 * Permite listar, cadastrar, atualizar e excluir registros de setores.
 */
@WebServlet("/SetorServlet")
public class SetorServlet extends HttpServlet {

    private SetorDAO setorDAO;

    @Override
    public void init() {
        // Inicializa o DAO para acesso aos dados de setor
        setorDAO = new SetorDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verifica se o usuário está autenticado
        if (!verificarAutenticacao(request, response)) {
            return;
        }

        String acao = request.getParameter("acao");

        try {
            if (acao == null || acao.isEmpty() || acao.equals("listar")) {
                // Listagem de todos os setores
                listarSetores(request, response);
            } else if ("excluir".equals(acao)) {
                // Exclusão de setor via GET
                excluirSetor(request, response);
            } else {
                // Qualquer ação desconhecida redireciona para a listagem
                listarSetores(request, response);
            }
        } catch (Exception e) {
            tratarErro(request, response, "Erro ao processar requisição: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verifica se o usuário está autenticado
        if (!verificarAutenticacao(request, response)) {
            return;
        }

        request.setCharacterEncoding("UTF-8");

        // Captura parâmetros do formulário
        String idStr = request.getParameter("id");
        String nome = request.getParameter("nome");
        String descricao = request.getParameter("descricao");
        String acao = request.getParameter("acao");

        HttpSession session = request.getSession();
        String mensagem = "";

        try {
            if ("excluir".equals(acao)) {
                // Exclusão via POST (geralmente a partir de modal)
                excluirSetor(request, response);
                return;
            } else {
                // Inserção ou atualização de setor
                if (nome == null || nome.trim().isEmpty()) {
                    mensagem = "Erro: Nome é obrigatório!";
                } else {
                    Setor setor = new Setor();
                    setor.setNome(nome.trim());
                    setor.setDescricao(descricao != null ? descricao.trim() : "");

                    boolean sucesso;
                    if (idStr == null || idStr.trim().isEmpty()) {
                        // Novo cadastro
                        sucesso = setorDAO.inserir(setor);
                        mensagem = sucesso ? "Setor cadastrado com sucesso!" : "Erro ao cadastrar setor!";
                    } else {
                        // Edição de setor existente
                        try {
                            int id = Integer.parseInt(idStr);
                            setor.setId(id);
                            sucesso = setorDAO.atualizar(setor);
                            mensagem = sucesso ? "Setor atualizado com sucesso!" : "Erro ao atualizar setor!";
                        } catch (NumberFormatException e) {
                            mensagem = "Erro: ID inválido!";
                        }
                    }
                }
            }
        } catch (Exception e) {
            mensagem = "Erro interno do sistema: " + e.getMessage();
        }

        // Salva a mensagem de operação na sessão e redireciona para a listagem
        session.setAttribute("mensagem", mensagem);
        response.sendRedirect("SetorServlet");
    }

    /**
     * Lista todos os setores cadastrados e encaminha para a JSP de exibição.
     */
    private void listarSetores(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Setor> setores = setorDAO.listarTodos();
            request.setAttribute("setores", setores);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/setor/Setor.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            throw new ServletException("Erro ao listar setores", e);
        }
    }

    /**
     * Exclui um setor com base no ID fornecido.
     * Salva mensagem de sucesso ou erro na sessão e redireciona para listagem.
     */
    private void excluirSetor(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession();
        String mensagem = "";

        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
                mensagem = "Erro: ID não fornecido!";
            } else {
                int id = Integer.parseInt(idParam);
                boolean sucesso = setorDAO.excluir(id);
                mensagem = sucesso ? "Setor excluído com sucesso!" : "Erro ao excluir setor!";
            }
        } catch (NumberFormatException e) {
            mensagem = "Erro: ID inválido!";
        } catch (Exception e) {
            mensagem = "Erro interno ao excluir setor: " + e.getMessage();
        }

        session.setAttribute("mensagem", mensagem);
        response.sendRedirect("SetorServlet");
    }

    /**
     * Verifica se o usuário está autenticado.
     * Caso contrário, redireciona para a página de login.
     */
    private boolean verificarAutenticacao(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("LoginRestrito.jsp");
            return false;
        }
        return true;
    }

    /**
     * Encaminha para a página de erro com mensagem específica.
     */
    private void tratarErro(HttpServletRequest request, HttpServletResponse response, String mensagem)
            throws ServletException, IOException {

        request.setAttribute("erro", mensagem);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/erro.jsp");
        dispatcher.forward(request, response);
    }
}

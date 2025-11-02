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

@WebServlet("/ProducaoServlet")
public class ProducaoServlet extends HttpServlet {

    private ProducaoDAO producaoDAO;
    private SetorDAO setorDAO;

    @Override
    public void init() {
        producaoDAO = new ProducaoDAO();
        setorDAO = new SetorDAO();

        System.out.println("=== DEBUG ProducaoServlet.init() ===");
        System.out.println("DAOs inicializados");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");

        System.out.println("=== DEBUG ProducaoServlet.doGet() ===");
        System.out.println("Ação: " + (acao != null ? acao : "listar"));

        try {
            // AGORA SÓ PRECISAMOS DA LISTAGEM - O RESTO É FEITO VIA MODAL
            if (acao == null || acao.isEmpty() || acao.equals("listar")) {
                listarProducoes(request, response);
            } else if ("excluir".equals(acao)) {
                excluirProducao(request, response);
            } else {
                // Se tentar acessar 'novo' ou 'editar' via GET, redireciona para listagem
                listarProducoes(request, response);
            }
        } catch (Exception e) {
            System.out.println("ERRO no doGet: " + e.getMessage());
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        String dtRegistro = request.getParameter("dt_registro");
        String qntStr = request.getParameter("qnt_produzida");
        String idSetorStr = request.getParameter("id_setor");

        System.out.println("=== DEBUG ProducaoServlet.doPost() ===");
        System.out.println("Dados: id=" + idStr + ", data=" + dtRegistro + ", qnt=" + qntStr + ", setor=" + idSetorStr);

        // VALIDAÇÕES
        if (dtRegistro == null || dtRegistro.isEmpty() ||
                qntStr == null || qntStr.isEmpty() ||
                idSetorStr == null || idSetorStr.isEmpty()) {

            System.out.println("ERRO: Campos obrigatórios faltando");
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
                // NOVO CADASTRO
                System.out.println("Inserindo nova produção...");
                boolean sucesso = producaoDAO.inserir(producao);
                if (sucesso) {
                    HttpSession session = request.getSession();
                    session.setAttribute("mensagem", "Produção cadastrada com sucesso!");
                    System.out.println("Produção inserida com sucesso!");
                } else {
                    HttpSession session = request.getSession();
                    session.setAttribute("mensagem", "Erro ao cadastrar produção!");
                    System.out.println("Falha ao inserir produção!");
                }
            } else {
                // EDIÇÃO
                int id = Integer.parseInt(idStr);
                producao.setId(id);
                System.out.println("Atualizando produção ID=" + id);
                boolean sucesso = producaoDAO.atualizar(producao);
                if (sucesso) {
                    HttpSession session = request.getSession();
                    session.setAttribute("mensagem", "Produção atualizada com sucesso!");
                    System.out.println("Produção atualizada com sucesso!");
                } else {
                    HttpSession session = request.getSession();
                    session.setAttribute("mensagem", "Erro ao atualizar produção!");
                    System.out.println("Falha ao atualizar produção!");
                }
            }
        } catch (Exception e) {
            System.out.println("ERRO no doPost: " + e.getMessage());
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("mensagem", "Erro ao salvar produção: " + e.getMessage());
        }

        response.sendRedirect("ProducaoServlet");
    }

    private void listarProducoes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("=== DEBUG listarProducoes() ===");

        List<Producao> producoes = producaoDAO.listar();
        List<Setor> setores = setorDAO.listarTodos(); // ← CARREGA SETORES PARA O MODAL

        System.out.println("Produções retornadas do DAO: " + producoes.size());
        System.out.println("Setores carregados: " + setores.size());

        request.setAttribute("producoes", producoes);
        request.setAttribute("setores", setores); // ← PASSA SETORES PARA O JSP

        RequestDispatcher dispatcher = request.getRequestDispatcher("/producao/listaProducao.jsp");
        dispatcher.forward(request, response);

        System.out.println("Redirecionado para listaProducao.jsp");
    }

    // MÉTODOS REMOVIDOS - AGORA SÓ PRECISAMOS DA EXCLUSÃO
    // - mostrarFormulario() - REMOVIDO (feito via modal)
    // - editarProducao() - REMOVIDO (feito via modal)

    private void excluirProducao(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        int id = Integer.parseInt(request.getParameter("id"));
        System.out.println("=== DEBUG excluirProducao() ===");
        System.out.println("Excluindo produção ID=" + id);

        boolean sucesso = producaoDAO.excluir(id);

        HttpSession session = request.getSession();
        if (sucesso) {
            session.setAttribute("mensagem", "Produção excluída com sucesso!");
            System.out.println("Produção excluída com sucesso!");
        } else {
            session.setAttribute("mensagem", "Erro ao excluir produção!");
            System.out.println("Erro ao excluir produção!");
        }

        response.sendRedirect("ProducaoServlet");
    }
}
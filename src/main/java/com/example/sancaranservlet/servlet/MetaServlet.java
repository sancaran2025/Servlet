package com.example.sancaranservlet.servlet;

import com.example.sancaranservlet.dao.MetaDAO;
import com.example.sancaranservlet.model.Meta;
import com.example.sancaranservlet.model.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/MetaServlet")
public class MetaServlet extends HttpServlet {

    // Instância do DAO para manipular metas no banco
    private final MetaDAO metaDAO = new MetaDAO();

    // Método que responde a requisições GET
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Recupera sessão e verifica se o usuário está logado
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) { // Se não houver usuário logado, redireciona para login
            response.sendRedirect("LoginRestrito.jsp");
            return;
        }

        String acao = request.getParameter("acao"); // Ação enviada via parâmetro

        // Ação padrão: listar todas as metas
        if (acao == null || acao.isEmpty() || acao.equals("listar")) {
            List<Meta> metas = metaDAO.listarTodas();
            request.setAttribute("metas", metas);
            RequestDispatcher rd = request.getRequestDispatcher("meta/ListaMeta.jsp"); // JSP de listagem
            rd.forward(request, response);
            return;
        }

        // Exibir formulário de nova meta
        if (acao.equals("novo")) {
            RequestDispatcher rd = request.getRequestDispatcher("meta/EditarMeta.jsp"); // JSP para inserir/editar
            rd.forward(request, response);
            return;
        }

        // Editar meta existente
        if (acao.equals("editar")) {
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                try {
                    int id = Integer.parseInt(idStr);
                    Meta meta = metaDAO.buscarPorId(id);
                    if (meta != null) {
                        request.setAttribute("meta", meta);
                        RequestDispatcher rd = request.getRequestDispatcher("meta/EditarMeta.jsp"); // Preenche formulário com dados
                        rd.forward(request, response);
                        return;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            response.sendRedirect("MetaServlet?erro=id_invalido"); // Redireciona se ID inválido
            return;
        }

        // Redireciona para lista de metas caso ação não seja reconhecida
        response.sendRedirect("MetaServlet");
    }

    // Método que responde a requisições POST (criar, atualizar ou excluir)
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8"); // Garante encoding correto
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) { // Bloqueia ação se não estiver logado
            response.sendRedirect("LoginRestrito.jsp");
            return;
        }

        String acao = request.getParameter("acao"); // Ação enviada via formulário
        String mensagem = "";

        if ("excluir".equals(acao)) { // Excluir meta
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                try {
                    int id = Integer.parseInt(idStr);
                    if (metaDAO.excluir(id)) {
                        mensagem = "Meta excluída com sucesso!";
                    } else {
                        mensagem = "Erro ao excluir meta!";
                    }
                } catch (NumberFormatException e) {
                    mensagem = "ID inválido!";
                }
            }
        } else {
            // Inserir nova meta ou atualizar existente
            String idStr = request.getParameter("id");
            String descricao = request.getParameter("descricao");
            String observacoes = request.getParameter("observacoes");
            String dataInicio = request.getParameter("dataInicio");
            String dataFim = request.getParameter("dataFim");
            String status = request.getParameter("status");
            String idProducaoStr = request.getParameter("idProducao");

            Meta meta = new Meta();
            meta.setDescricao(descricao);
            meta.setObservacoes(observacoes);
            meta.setStatus(status != null ? status.charAt(0) : 'P'); // 'P' = padrão/pendente

            try {
                meta.setDataInicio(Date.valueOf(dataInicio));
                meta.setDataFim(Date.valueOf(dataFim));
                meta.setIdProducao(Integer.parseInt(idProducaoStr));
            } catch (Exception e) {
                mensagem = "Data ou ID de produção inválido!";
            }

            if (idStr == null || idStr.isEmpty()) {
                // Nova meta
                if (metaDAO.inserir(meta)) {
                    mensagem = "Meta cadastrada com sucesso!";
                } else {
                    mensagem = "Erro ao cadastrar meta!";
                }
            } else {
                try {
                    // Atualizar meta existente
                    meta.setId(Integer.parseInt(idStr));
                    if (metaDAO.atualizar(meta)) {
                        mensagem = "Meta atualizada com sucesso!";
                    } else {
                        mensagem = "Erro ao atualizar meta!";
                    }
                } catch (NumberFormatException e) {
                    mensagem = "ID inválido!";
                }
            }
        }

        // Salva mensagem na sessão para exibição na página
        session.setAttribute("mensagem", mensagem);
        response.sendRedirect("MetaServlet"); // Redireciona para lista de metas
    }
}

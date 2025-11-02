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

    private final MetaDAO metaDAO = new MetaDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect("LoginRestrito.jsp");
            return;
        }

        String acao = request.getParameter("acao");

        if (acao == null || acao.isEmpty() || acao.equals("listar")) {
            List<Meta> metas = metaDAO.listarTodas();
            request.setAttribute("metas", metas);
            // ATENÇÃO: Você precisa criar este arquivo JSP
            RequestDispatcher rd = request.getRequestDispatcher("meta/ListaMeta.jsp");
            rd.forward(request, response);
            return;
        }

        if (acao.equals("novo")) {
            // ATENÇÃO: Você precisa criar este arquivo JSP
            RequestDispatcher rd = request.getRequestDispatcher("meta/EditarMeta.jsp");
            rd.forward(request, response);
            return;
        }

        if (acao.equals("editar")) {
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                try {
                    int id = Integer.parseInt(idStr);
                    Meta meta = metaDAO.buscarPorId(id);
                    if (meta != null) {
                        request.setAttribute("meta", meta);
                        // ATENÇÃO: Você precisa criar este arquivo JSP
                        RequestDispatcher rd = request.getRequestDispatcher("meta/EditarMeta.jsp");
                        rd.forward(request, response);
                        return;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            response.sendRedirect("MetaServlet?erro=id_invalido");
            return;
        }

        response.sendRedirect("MetaServlet");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect("LoginRestrito.jsp");
            return;
        }

        String acao = request.getParameter("acao");
        String mensagem = "";

        if ("excluir".equals(acao)) {
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
            // salvar (novo ou atualizar)
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
            meta.setStatus(status != null ? status.charAt(0) : 'P');

            try {
                meta.setDataInicio(Date.valueOf(dataInicio));
                meta.setDataFim(Date.valueOf(dataFim));
                meta.setIdProducao(Integer.parseInt(idProducaoStr));
            } catch (Exception e) {
                mensagem = "Data ou ID de produção inválido!";
            }

            if (idStr == null || idStr.isEmpty()) {
                // novo
                if (metaDAO.inserir(meta)) {
                    mensagem = "Meta cadastrada com sucesso!";
                } else {
                    mensagem = "Erro ao cadastrar meta!";
                }
            } else {
                try {
                    // atualizar
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

        session.setAttribute("mensagem", mensagem);
        response.sendRedirect("MetaServlet");
    }
}
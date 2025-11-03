package com.example.sancaranservlet.servlet;

import com.example.sancaranservlet.dao.MetaDAO;
import com.example.sancaranservlet.model.Meta;
import com.example.sancaranservlet.model.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

/**
 * Servlet responsável pela visualização de metas.
 * Permite listar todas as metas ou aplicar filtros por status e período.
 * Também calcula estatísticas básicas de quantidade de metas por status.
 */
@WebServlet("/metas-visualizacao")
public class VisualizacaoMetaServlet extends HttpServlet {

    private final MetaDAO metaDAO = new MetaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // Redireciona para login caso usuário não esteja autenticado
        if (usuario == null) {
            response.sendRedirect("entrarConta.jsp");
            return;
        }

        // Captura parâmetros de filtro da requisição
        String filtroStatus = request.getParameter("filtroStatus");
        String filtroDataInicio = request.getParameter("filtroDataInicio");
        String filtroDataFim = request.getParameter("filtroDataFim");

        List<Meta> metas;

        // Se algum filtro estiver presente, aplica filtragem no DAO
        if ((filtroStatus != null && !filtroStatus.isEmpty()) ||
                (filtroDataInicio != null && !filtroDataInicio.isEmpty()) ||
                (filtroDataFim != null && !filtroDataFim.isEmpty())) {

            metas = metaDAO.filtrarMetas(filtroStatus, filtroDataInicio, filtroDataFim);
        } else {
            // Caso contrário, lista todas as metas
            metas = metaDAO.listarTodas();
        }

        // Inicializa contadores de status para estatísticas
        int totalConcluidas = 0;
        int totalAndamento = 0;
        int totalPendentes = 0;

        // Percorre todas as metas para contar quantidade por status
        for (Meta meta : metas) {
            switch (meta.getStatus()) {
                case 'C': totalConcluidas++; break;
                case 'A': totalAndamento++; break;
                case 'P': totalPendentes++; break;
            }
        }

        // Define atributos que serão acessíveis pelo JSP
        request.setAttribute("metas", metas);
        request.setAttribute("filtroStatus", filtroStatus);
        request.setAttribute("filtroDataInicio", filtroDataInicio);
        request.setAttribute("filtroDataFim", filtroDataFim);
        request.setAttribute("totalConcluidas", totalConcluidas);
        request.setAttribute("totalAndamento", totalAndamento);
        request.setAttribute("totalPendentes", totalPendentes);

        // Encaminha para o JSP de visualização das metas
        RequestDispatcher rd = request.getRequestDispatcher("/meta/metas-visualizacao.jsp");
        rd.forward(request, response);
    }
}

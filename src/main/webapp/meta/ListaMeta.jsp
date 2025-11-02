<%@ page import="com.example.sancaranservlet.dao.MetaDAO, com.example.sancaranservlet.model.Meta, java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    MetaDAO dao = new MetaDAO();
    List<Meta> metas = dao.listarTodas();

    String mensagem = (String) session.getAttribute("mensagem");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Metas - Sancaran</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/listaMeta.css?v=1.0">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>

<div class="sidebar">
    <div class="logo">
        <img src="${pageContext.request.contextPath}/imgs/logo.png" alt="Logo Sancaran" class="logo-img">
        <span class="logo-text">SANCARAN</span>
    </div>
    <a href="${pageContext.request.contextPath}/AreaRestrita.jsp" class="btn">
        <i class="fas fa-tachometer-alt"></i> PAINEL GERAL
    </a>
    <a href="${pageContext.request.contextPath}/ProducaoServlet" class="btn">
        <i class="fas fa-chart-line"></i> PRODUÇÃO
    </a>
    <a href="${pageContext.request.contextPath}/MetaServlet" class="btn active">
        <i class="fas fa-bullseye"></i> METAS
    </a>
    <a href="${pageContext.request.contextPath}/SetorServlet" class="btn">
        <i class="fas fa-building"></i> SETOR
    </a>
    <a href="${pageContext.request.contextPath}/AdmServlet" class="btn">
        <i class="fas fa-users-cog"></i> ADMIN
    </a>
    <a href="${pageContext.request.contextPath}/sair.jsp" class="logout">
        <i class="fas fa-sign-out-alt"></i> Sair
    </a>
</div>

<div class="content">
    <div class="container">
        <h1 class="title">
            <i class="fas fa-bullseye"></i> Gerenciamento de Metas
        </h1>

        <% if (mensagem != null && !mensagem.isEmpty()) { %>
        <div class="mensagem">
            <i class="fas fa-info-circle"></i> <%= mensagem %>
        </div>
        <% session.removeAttribute("mensagem"); %>
        <% } %>

        <button onclick="abrirModal()" class="btn primary">
            <i class="fas fa-plus"></i> Nova Meta
        </button>

        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Data Início</th>
                <th>Data Fim</th>
                <th>Descrição</th>
                <th>Status</th>
                <th>ID Produção</th>
                <th>Ações</th>
            </tr>
            </thead>
            <tbody>
            <%
                if (metas.isEmpty()) {
            %>
            <tr>
                <td colspan="7" class="empty">
                    <i class="fas fa-bullseye"></i> Nenhuma meta cadastrada.
                </td>
            </tr>
            <%
            } else {
                for (Meta m : metas) {
                    // Corrigindo as verificações para char (não pode ser null)
                    char status = m.getStatus(); // char sempre tem valor, não pode ser null
                    String statusText = "";
                    switch(status) {
                        case 'P': statusText = "Pendente"; break;
                        case 'A': statusText = "Em Andamento"; break;
                        case 'C': statusText = "Concluída"; break;
                        default: statusText = "Pendente";
                    }
            %>
            <tr>
                <td><strong>#<%=m.getId()%></strong></td>
                <td><%=m.getDataInicio() != null ? m.getDataInicio() : "-"%></td>
                <td><%=m.getDataFim() != null ? m.getDataFim() : "-"%></td>
                <td><%=m.getDescricao() != null ? m.getDescricao() : "-"%></td>
                <td>
                    <span class="status status-<%=status%>">
                        <%=statusText%>
                    </span>
                </td>
                <td><%=m.getIdProducao() > 0 ? m.getIdProducao() : "-"%></td>
                <td class="actions">
                    <button onclick="editarMeta(
                        <%=m.getId()%>,
                            '<%=m.getDataInicio() != null ? m.getDataInicio() : ""%>',
                            '<%=m.getDataFim() != null ? m.getDataFim() : ""%>',
                            '<%=m.getDescricao() != null ? m.getDescricao().replace("'", "\\'") : ""%>',
                            '<%=status%>',
                        <%=m.getIdProducao()%>
                            )" class="action edit" title="Editar">
                        <i class="fas fa-edit"></i>
                    </button>

                    <form action="${pageContext.request.contextPath}/MetaServlet" method="post" style="display:inline;">
                        <input type="hidden" name="acao" value="excluir">
                        <input type="hidden" name="id" value="<%=m.getId()%>">
                        <button type="submit" class="action delete" title="Excluir"
                                onclick="return confirm('Tem certeza que deseja excluir esta meta?')">
                            <i class="fas fa-trash"></i>
                        </button>
                    </form>
                </td>
            </tr>
            <%
                    }
                }
            %>
            </tbody>
        </table>
    </div>
</div>

<!-- MODAL PARA NOVO/EDITAR META -->
<div id="modalMeta" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h2 id="modalTitle">
                <i class="fas fa-plus"></i> Nova Meta
            </h2>
            <span class="close" onclick="fecharModal()">&times;</span>
        </div>
        <div class="modal-body">
            <form id="formMeta" action="MetaServlet" method="post">
                <input type="hidden" id="metaId" name="id" value="">

                <div class="form-group">
                    <label for="dataInicio">
                        <i class="fas fa-calendar-alt"></i> Data Início:
                    </label>
                    <input type="date" id="dataInicio" name="dataInicio" required>
                </div>

                <div class="form-group">
                    <label for="dataFim">
                        <i class="fas fa-calendar-check"></i> Data Fim:
                    </label>
                    <input type="date" id="dataFim" name="dataFim" required>
                </div>

                <div class="form-group">
                    <label for="descricao">
                        <i class="fas fa-align-left"></i> Descrição:
                    </label>
                    <textarea id="descricao" name="descricao" rows="4"
                              placeholder="Descreva a meta..."></textarea>
                </div>

                <div class="form-group">
                    <label for="status">
                        <i class="fas fa-tasks"></i> Status:
                    </label>
                    <select id="status" name="status" required>
                        <option value="P">Pendente</option>
                        <option value="A">Em Andamento</option>
                        <option value="C">Concluída</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="idProducao">
                        <i class="fas fa-chart-line"></i> ID Produção:
                    </label>
                    <input type="number" id="idProducao" name="idProducao"
                           placeholder="ID da produção relacionada">
                </div>

                <div class="form-actions">
                    <button type="button" class="btn secondary" onclick="fecharModal()">
                        <i class="fas fa-times"></i> Cancelar
                    </button>
                    <button type="submit" class="btn primary">
                        <i class="fas fa-save"></i> Salvar Meta
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    // FUNÇÕES DO MODAL
    function abrirModal() {
        document.getElementById('modalMeta').style.display = 'block';
        document.getElementById('modalTitle').innerHTML = '<i class="fas fa-plus"></i> Nova Meta';
        document.getElementById('formMeta').reset();
        document.getElementById('metaId').value = '';

        // Focar no primeiro campo
        setTimeout(() => {
            document.getElementById('dataInicio').focus();
        }, 300);
    }

    function fecharModal() {
        document.getElementById('modalMeta').style.display = 'none';
    }

    function editarMeta(id, dataInicio, dataFim, descricao, status, idProducao) {
        document.getElementById('modalMeta').style.display = 'block';
        document.getElementById('modalTitle').innerHTML = '<i class="fas fa-edit"></i> Editar Meta';
        document.getElementById('metaId').value = id;
        document.getElementById('dataInicio').value = dataInicio;
        document.getElementById('dataFim').value = dataFim;
        document.getElementById('descricao').value = descricao;
        document.getElementById('status').value = status;
        document.getElementById('idProducao').value = idProducao;

        // Focar no primeiro campo
        setTimeout(() => {
            document.getElementById('dataInicio').focus();
        }, 300);
    }

    // Fechar modal clicando fora
    window.onclick = function(event) {
        var modal = document.getElementById('modalMeta');
        if (event.target == modal) {
            fecharModal();
        }
    }

    // Fechar modal com ESC
    document.addEventListener('keydown', function(event) {
        if (event.key === 'Escape') {
            fecharModal();
        }
    });

    // Validação do formulário
    document.getElementById('formMeta').addEventListener('submit', function(e) {
        const dataInicio = document.getElementById('dataInicio').value;
        const dataFim = document.getElementById('dataFim').value;
        const descricao = document.getElementById('descricao').value.trim();

        if (!dataInicio) {
            e.preventDefault();
            alert('Por favor, selecione a data de início.');
            document.getElementById('dataInicio').focus();
            return;
        }

        if (!dataFim) {
            e.preventDefault();
            alert('Por favor, selecione a data de fim.');
            document.getElementById('dataFim').focus();
            return;
        }

        if (dataInicio > dataFim) {
            e.preventDefault();
            alert('A data de início não pode ser posterior à data de fim.');
            document.getElementById('dataInicio').focus();
            return;
        }

        if (!descricao) {
            e.preventDefault();
            alert('Por favor, insira uma descrição para a meta.');
            document.getElementById('descricao').focus();
            return;
        }
    });
</script>
</body>
</html>
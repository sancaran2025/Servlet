<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.example.sancaranservlet.model.Setor" %>
<%@ page import="java.util.List" %>
<%
    List<Setor> setores = (List<Setor>) request.getAttribute("setores");
    if (setores == null) {
        response.sendRedirect("VisualizacaoSetorServlet");
        return;
    }
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Visualização de Setores - Sancaran</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/visualizacaoSetor.css?v=1.0">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>

<div class="sidebar">
    <div class="logo">
        <img src="${pageContext.request.contextPath}/imgs/logo.png" alt="Logo Sancaran" class="logo-img">
        <span class="logo-text">SANCARAN</span>
    </div>
    <a href="${pageContext.request.contextPath}/painelGeral.jsp" class="btn">
        <i class="fas fa-tachometer-alt"></i> PAINEL GERAL
    </a>
    <a href="${pageContext.request.contextPath}/VisualizacaoProducaoServlet" class="btn">
        <i class="fas fa-chart-line"></i> PRODUÇÃO
    </a>
    <a href="${pageContext.request.contextPath}/metas-visualizacao" class="btn active">
        <i class="fas fa-bullseye"></i> METAS
    </a>
    <a href="${pageContext.request.contextPath}/VisualizacaoSetorServlet" class="btn">
        <i class="fas fa-building"></i> SETOR
    </a>
    <a href="${pageContext.request.contextPath}/UsuarioVisualizacaoServlet" class="btn">
        <i class="fas fa-users-cog"></i> ADMIN
    </a>
    <a href="${pageContext.request.contextPath}/index.html" class="logout">
        <i class="fas fa-sign-out-alt"></i> Sair
    </a>
</div>

<div class="content">
    <div class="container">
        <h1 class="title">
            <i class="fas fa-eye"></i> Visualização de Setores
        </h1>

        <!-- Filtros -->
        <div class="filtros-container">
            <h3><i class="fas fa-filter"></i> Filtros</h3>
            <div class="filtros-grid">
                <div class="filtro-group">
                    <label for="nome-filtro"><i class="fas fa-search"></i> Nome do Setor:</label>
                    <input type="text" id="nome-filtro" placeholder="Digite para filtrar...">
                </div>
                <div class="filtro-actions">
                    <button class="btn primary" onclick="aplicarFiltros()">
                        <i class="fas fa-check"></i> Aplicar Filtros
                    </button>
                    <button class="btn secondary" onclick="limparFiltros()">
                        <i class="fas fa-times"></i> Limpar
                    </button>
                </div>
            </div>
        </div>

        <!-- Tabela de Setores -->
        <div class="table-container">
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>NOME</th>
                    <th>DESCRIÇÃO</th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (setores.isEmpty()) {
                %>
                <tr>
                    <td colspan="3" class="empty">
                        <i class="fas fa-inbox"></i> Nenhum setor cadastrado.
                    </td>
                </tr>
                <%
                } else {
                    for (Setor setor : setores) {
                %>
                <tr>
                    <td><strong>#<%= setor.getId() %></strong></td>
                    <td><strong class="nome-setor"><%= setor.getNome() != null ? setor.getNome() : "" %></strong></td>
                    <td class="descricao-cell"><%= setor.getDescricao() != null ? setor.getDescricao() : "" %></td>
                </tr>
                <%
                        }
                    }
                %>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script>
    function aplicarFiltros() {
        const nomeFiltro = document.getElementById('nome-filtro').value.toLowerCase().trim();

        // Filtragem client-side
        const linhas = document.querySelectorAll('tbody tr');
        let linhasVisiveis = 0;

        linhas.forEach(linha => {
            if (linha.classList.contains('empty')) return;

            const nomeSetor = linha.querySelector('.nome-setor').textContent.toLowerCase();
            let mostrar = true;

            // Filtro por nome
            if (nomeFiltro && !nomeSetor.includes(nomeFiltro)) {
                mostrar = false;
            }

            linha.style.display = mostrar ? '' : 'none';
            if (mostrar) linhasVisiveis++;
        });

        // Feedback
        if (linhasVisiveis === 0) {
            alert('Nenhum setor encontrado com os filtros aplicados.');
        } else {
            alert(`Filtros aplicados!\nFiltro por nome: ${nomeFiltro || 'Não definido'}\n\n${linhasVisiveis} setor(es) encontrado(s).`);
        }
    }

    function limparFiltros() {
        document.getElementById('nome-filtro').value = '';

        // Mostrar todas as linhas
        const linhas = document.querySelectorAll('tbody tr');
        linhas.forEach(linha => {
            linha.style.display = '';
        });

        alert('Filtros limpos! Todos os setores estão sendo exibidos.');
    }
</script>
</body>
</html>
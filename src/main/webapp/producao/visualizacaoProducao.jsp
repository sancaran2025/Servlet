<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.example.sancaranservlet.model.Producao" %>
<%@ page import="java.util.List" %>
<%
    List<Producao> producoes = (List<Producao>) request.getAttribute("producoes");
    if (producoes == null) {
        response.sendRedirect("VisualizacaoProducaoServlet");
        return;
    }
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Visualização de Produção - Sancaran</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/visualizacaoProducao.css?v=1.0">
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
            <i class="fas fa-eye"></i> Visualização de Produção
        </h1>

        <!-- Filtros -->
        <div class="filtros-container">
            <h3><i class="fas fa-filter"></i> Filtros</h3>
            <div class="filtros-grid">
                <div class="filtro-group">
                    <label for="quantidade-min"><i class="fas fa-boxes"></i> Quantidade Mínima:</label>
                    <input type="number" id="quantidade-min" min="0" placeholder="Ex: 100">
                </div>
                <div class="filtro-group">
                    <label for="quantidade-max"><i class="fas fa-boxes"></i> Quantidade Máxima:</label>
                    <input type="number" id="quantidade-max" min="0" placeholder="Ex: 500">
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

        <!-- Tabela de Produção -->
        <div class="table-container">
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Data</th>
                    <th>Quantidade Produzida</th>
                    <th>Setor</th>
                </tr>
                </thead>
                <tbody>
                <%
                    if (producoes.isEmpty()) {
                %>
                <tr>
                    <td colspan="4" class="empty">
                        <i class="fas fa-inbox"></i> Nenhuma produção registrada.
                    </td>
                </tr>
                <%
                } else {
                    for (Producao p : producoes) {
                %>
                <tr>
                    <td><strong>#<%= p.getId() %></strong></td>
                    <td><%= p.getDtRegistro() != null ? p.getDtRegistro() : "<span style='color: #81B5E1;'>N/A</span>" %></td>
                    <td><strong class="quantidade"><%= p.getQntProduzida() %></strong> unidades</td>
                    <td>
                            <span class="setor-badge">
                                <%= p.getNomeSetor() != null ? p.getNomeSetor() : "Setor " + p.getIdSetor() %>
                            </span>
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
</div>

<script>
    function aplicarFiltros() {
        const qntMin = document.getElementById('quantidade-min').value;
        const qntMax = document.getElementById('quantidade-max').value;

        // Validação
        if (qntMin && qntMax && parseInt(qntMin) > parseInt(qntMax)) {
            alert('A quantidade mínima não pode ser maior que a máxima.');
            return;
        }

        // Filtragem client-side
        const linhas = document.querySelectorAll('tbody tr');
        let linhasVisiveis = 0;

        linhas.forEach(linha => {
            if (linha.classList.contains('empty')) return;

            const quantidade = parseInt(linha.querySelector('.quantidade').textContent);
            let mostrar = true;

            // Filtro por quantidade mínima
            if (qntMin && quantidade < parseInt(qntMin)) {
                mostrar = false;
            }

            // Filtro por quantidade máxima
            if (qntMax && quantidade > parseInt(qntMax)) {
                mostrar = false;
            }

            linha.style.display = mostrar ? '' : 'none';
            if (mostrar) linhasVisiveis++;
        });

        // Feedback
        if (linhasVisiveis === 0) {
            alert('Nenhuma produção encontrada com os filtros aplicados.');
        } else {
            alert(`Filtros aplicados!\nQuantidade mínima: ${qntMin || 'Não definida'}\nQuantidade máxima: ${qntMax || 'Não definida'}\n\n${linhasVisiveis} produção(ões) encontrada(s).`);
        }
    }

    function limparFiltros() {
        document.getElementById('quantidade-min').value = '';
        document.getElementById('quantidade-max').value = '';

        // Mostrar todas as linhas
        const linhas = document.querySelectorAll('tbody tr');
        linhas.forEach(linha => {
            linha.style.display = '';
        });

        alert('Filtros limpos! Todas as produções estão sendo exibidas.');
    }
</script>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Visualização de Metas</title>
    <link rel="stylesheet" href="css/metas-visualizacao.css">
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

<!-- Conteúdo Principal -->
<div class="content">
    <div class="container">
        <h1 class="title">Visualização de Metas</h1>

        <!-- Filtros -->
        <div class="filtros-container">
            <h3>Filtros</h3>
            <div class="filtros-grid">
                <div class="filtro-group">
                    <label for="status">Status:</label>
                    <select id="status">
                        <option value="todos">Todos os Status</option>
                        <option value="concluidas">Concluídas</option>
                        <option value="andamento">Em Andamento</option>
                        <option value="pendentes">Pendentes</option>
                    </select>
                </div>
                <div class="filtro-group">
                    <label for="data-inicio">Data Início:</label>
                    <input type="date" id="data-inicio">
                </div>
                <div class="filtro-group">
                    <label for="data-fim">Data Fim:</label>
                    <input type="date" id="data-fim">
                </div>
                <div class="filtro-actions">
                    <button class="btn primary">Aplicar Filtros</button>
                    <button class="btn secondary">Limpar</button>
                </div>
            </div>
        </div>

        <!-- Estatísticas -->
        <div class="stats-container">
            <div class="stat-card">
                <div class="stat-icon">📊</div>
                <div class="stat-info">
                    <h3>3</h3>
                    <p>Total de Metas</p>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">✅</div>
                <div class="stat-info">
                    <h3>0</h3>
                    <p>Concluídas</p>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">🔄</div>
                <div class="stat-info">
                    <h3>2</h3>
                    <p>Em Andamento</p>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">⏳</div>
                <div class="stat-info">
                    <h3>1</h3>
                    <p>Pendentes</p>
                </div>
            </div>
        </div>

        <!-- Tabela de Metas -->
        <div class="table-container">
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Data Início</th>
                    <th>Data Fim</th>
                    <th>Descrição</th>
                    <th>Observações</th>
                    <th>Status</th>
                    <th>ID Produção</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>#3</td>
                    <td>2025-09-05</td>
                    <td>2025-09-25</td>
                    <td class="descricao-cell">Aprovar 95% das peças</td>
                    <td class="observacoes-cell">Meta de qualidade</td>
                    <td><span class="status status-A">EM ANDAMENTO</span></td>
                    <td><span class="id-producao">#3</span></td>
                </tr>
                <tr>
                    <td>#2</td>
                    <td>2025-09-02</td>
                    <td>2025-09-20</td>
                    <td class="descricao-cell">Pintar 500 peças</td>
                    <td class="observacoes-cell">Meta de pintura</td>
                    <td><span class="status status-P">PENDENTE</span></td>
                    <td><span class="id-producao">#2</span></td>
                </tr>
                <tr>
                    <td>#1</td>
                    <td>2025-08-15</td>
                    <td>2025-09-10</td>
                    <td class="descricao-cell">Montar 300 unidades</td>
                    <td class="observacoes-cell">Meta de montagem</td>
                    <td><span class="status status-A">EM ANDAMENTO</span></td>
                    <td><span class="id-producao">#1</span></td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script>
    // Função para aplicar filtros
    document.querySelector('.btn.primary').addEventListener('click', function() {
        const status = document.getElementById('status').value;
        const dataInicio = document.getElementById('data-inicio').value;
        const dataFim = document.getElementById('data-fim').value;

        alert(`Filtros aplicados:\nStatus: ${status}\nData Início: ${dataInicio}\nData Fim: ${dataFim}`);
    });

    // Função para limpar filtros
    document.querySelector('.btn.secondary').addEventListener('click', function() {
        document.getElementById('status').value = 'todos';
        document.getElementById('data-inicio').value = '';
        document.getElementById('data-fim').value = '';
    });
</script>
</body>
</html>
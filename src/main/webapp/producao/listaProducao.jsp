<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.example.sancaranservlet.model.Producao" %>
<%@ page import="com.example.sancaranservlet.model.Setor" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%
    // BLOQUEAR ACESSO DIRETO - redirecionar para o Servlet
    List<Producao> producoes = (List<Producao>) request.getAttribute("producoes");
    List<Setor> setores = (List<Setor>) request.getAttribute("setores");
    if (producoes == null) {
        response.sendRedirect("ProducaoServlet");
        return;
    }

    if (setores == null) {
        setores = new ArrayList<>();
    }

    String mensagem = (String) session.getAttribute("mensagem");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Produção - Sancaran</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/listaProducao.css?v=1.0">
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
    <a href="${pageContext.request.contextPath}/ProducaoServlet" class="btn active">
        <i class="fas fa-chart-line"></i> PRODUÇÃO
    </a>
    <a href="${pageContext.request.contextPath}/MetaServlet" class="btn">
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
            <i class="fas fa-chart-line"></i> Produções Registradas
        </h1>

        <% if (mensagem != null && !mensagem.isEmpty()) { %>
        <div class="mensagem">
            <i class="fas fa-info-circle"></i> <%= mensagem %>
        </div>
        <% session.removeAttribute("mensagem"); %>
        <% } %>

        <button onclick="abrirModal()" class="btn primary">
            <i class="fas fa-plus"></i> Nova Produção
        </button>

        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Data</th>
                <th>Quantidade Produzida</th>
                <th>Setor</th>
                <th>Ações</th>
            </tr>
            </thead>
            <tbody>
            <%
                if (producoes.isEmpty()) {
            %>
            <tr>
                <td colspan="5" class="empty">
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
                <td><strong><%= p.getQntProduzida() %></strong> unidades</td>
                <td>
                    <span class="status-badge status-completed">
                        <%= p.getNomeSetor() != null ? p.getNomeSetor() : "Setor " + p.getIdSetor() %>
                    </span>
                </td>
                <td class="icons">
                    <button onclick="editarProducao(<%= p.getId() %>, '<%= p.getDtRegistro() != null ? p.getDtRegistro() : "" %>', <%= p.getQntProduzida() %>, <%= p.getIdSetor() %>)"
                            class="action edit" title="Editar">
                        <i class="fas fa-edit"></i>
                    </button>

                    <a href="ProducaoServlet?acao=excluir&id=<%= p.getId() %>" class="action delete" title="Excluir"
                       onclick="return confirm('Tem certeza que deseja excluir esta produção?')">
                        <i class="fas fa-trash"></i>
                    </a>
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

<!-- MODAL PARA NOVO/EDITAR PRODUÇÃO -->
<div id="modalProducao" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h2 id="modalTitle">
                <i class="fas fa-plus"></i> Nova Produção
            </h2>
            <span class="close" onclick="fecharModal()">&times;</span>
        </div>
        <div class="modal-body">
            <form id="formProducao" action="ProducaoServlet" method="post">
                <input type="hidden" id="producaoId" name="id" value="">

                <div class="form-group">
                    <label for="dt_registro">
                        <i class="fas fa-calendar"></i> Data de Registro:
                    </label>
                    <input type="date" id="dt_registro" name="dt_registro" required>
                </div>

                <div class="form-group">
                    <label for="qnt_produzida">
                        <i class="fas fa-boxes"></i> Quantidade Produzida:
                    </label>
                    <input type="number" id="qnt_produzida" name="qnt_produzida"
                           min="1" required placeholder="Ex: 100">
                </div>

                <div class="form-group">
                    <label for="id_setor">
                        <i class="fas fa-building"></i> Setor:
                    </label>
                    <select id="id_setor" name="id_setor" required>
                        <option value="">Selecione um setor</option>
                        <%
                            for (Setor s : setores) {
                        %>
                        <option value="<%= s.getId() %>"><%= s.getNome() %></option>
                        <%
                            }
                        %>
                    </select>
                </div>

                <div class="form-actions">
                    <button type="button" class="btn secondary" onclick="fecharModal()">
                        <i class="fas fa-times"></i> Cancelar
                    </button>
                    <button type="submit" class="btn primary">
                        <i class="fas fa-save"></i> Salvar Produção
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    // FUNÇÕES DO MODAL
    function abrirModal() {
        document.getElementById('modalProducao').style.display = 'block';
        document.getElementById('modalTitle').innerHTML = '<i class="fas fa-plus"></i> Nova Produção';
        document.getElementById('formProducao').reset();
        document.getElementById('producaoId').value = '';

        // Data atual
        const today = new Date().toISOString().split('T')[0];
        document.getElementById('dt_registro').value = today;

        // Focar no campo quantidade
        setTimeout(() => {
            document.getElementById('qnt_produzida').focus();
        }, 300);
    }

    function fecharModal() {
        document.getElementById('modalProducao').style.display = 'none';
    }

    function editarProducao(id, data, quantidade, idSetor) {
        document.getElementById('modalProducao').style.display = 'block';
        document.getElementById('modalTitle').innerHTML = '<i class="fas fa-edit"></i> Editar Produção';
        document.getElementById('producaoId').value = id;
        document.getElementById('dt_registro').value = data;
        document.getElementById('qnt_produzida').value = quantidade;
        document.getElementById('id_setor').value = idSetor;

        // Focar no primeiro campo
        setTimeout(() => {
            document.getElementById('dt_registro').focus();
        }, 300);
    }

    // Fechar modal clicando fora
    window.onclick = function(event) {
        var modal = document.getElementById('modalProducao');
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
    document.getElementById('formProducao').addEventListener('submit', function(e) {
        const quantidade = document.getElementById('qnt_produzida').value;
        const setor = document.getElementById('id_setor').value;

        if (!setor) {
            e.preventDefault();
            alert('Por favor, selecione um setor.');
            document.getElementById('id_setor').focus();
            return;
        }

        if (!quantidade || quantidade < 1) {
            e.preventDefault();
            alert('Por favor, insira uma quantidade válida.');
            document.getElementById('qnt_produzida').focus();
            return;
        }
    });
</script>
</body>
</html>
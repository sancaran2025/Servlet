<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.example.sancaranservlet.model.Setor" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%
    // BLOQUEAR ACESSO DIRETO - redirecionar para o Servlet
    List<Setor> setores = (List<Setor>) request.getAttribute("setores");
    if (setores == null) {
        response.sendRedirect("SetorServlet");
        return;
    }

    String mensagem = (String) session.getAttribute("mensagem");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Setores - Sancaran</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/setor.css?v=1.0">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>

<div class="sidebar">
    <div class="logo">
        <i class="fas fa-shield-alt"></i> SANCARAN
    </div>
    <a href="${pageContext.request.contextPath}/AreaRestrita.jsp" class="btn">
        <i class="fas fa-tachometer-alt"></i> PAINEL GERAL
    </a>
    <a href="${pageContext.request.contextPath}/ProducaoServlet" class="btn">
        <i class="fas fa-chart-line"></i> PRODUÇÃO
    </a>
    <a href="${pageContext.request.contextPath}/MetaServlet" class="btn">
        <i class="fas fa-bullseye"></i> METAS
    </a>
    <a href="${pageContext.request.contextPath}/SetorServlet" class="btn active">
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
            <i class="fas fa-building"></i> Gerenciar Setores
        </h1>

        <% if (mensagem != null && !mensagem.isEmpty()) { %>
        <div class="mensagem">
            <i class="fas fa-info-circle"></i> <%= mensagem %>
        </div>
        <% session.removeAttribute("mensagem"); %>
        <% } %>

        <button onclick="abrirModal()" class="btn primary">
            <i class="fas fa-plus"></i> Novo Setor
        </button>

        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>NOME</th>
                <th>DESCRIÇÃO</th>
                <th>AÇÕES</th>
            </tr>
            </thead>
            <tbody>
            <%
                if (setores.isEmpty()) {
            %>
            <tr>
                <td colspan="4" style="text-align: center; color: #81B5E1; font-style: italic; padding: 40px;">
                    <i class="fas fa-inbox"></i> Nenhum setor cadastrado.
                </td>
            </tr>
            <%
            } else {
                for (Setor setor : setores) {
            %>
            <tr>
                <td><strong>#<%= setor.getId() %></strong></td>
                <td><strong><%= setor.getNome() != null ? setor.getNome() : "" %></strong></td>
                <td><%= setor.getDescricao() != null ? setor.getDescricao() : "" %></td>
                <td class="icons">
                    <button onclick="editarSetor(<%= setor.getId() %>, '<%= setor.getNome() != null ? setor.getNome().replace("'", "\\'") : "" %>', '<%= setor.getDescricao() != null ? setor.getDescricao().replace("'", "\\'") : "" %>')"
                            class="action edit" title="Editar">
                        <i class="fas fa-edit"></i>
                    </button>

                    <form action="${pageContext.request.contextPath}/SetorServlet" method="post" style="display:inline;">
                        <input type="hidden" name="acao" value="excluir">
                        <input type="hidden" name="id" value="<%= setor.getId() %>">
                        <button type="submit" class="action delete" title="Excluir"
                                onclick="return confirm('Tem certeza que deseja excluir este setor?');">
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

<!-- MODAL PARA NOVO/EDITAR SETOR -->
<div id="modalSetor" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h2 id="modalTitle">
                <i class="fas fa-plus"></i> Novo Setor
            </h2>
            <span class="close" onclick="fecharModal()">&times;</span>
        </div>
        <div class="modal-body">
            <form id="formSetor" action="SetorServlet" method="post">
                <input type="hidden" id="setorId" name="id" value="">

                <div class="form-group">
                    <label for="nome">
                        <i class="fas fa-tag"></i> Nome do Setor:
                    </label>
                    <input type="text" id="nome" name="nome" required
                           placeholder="Ex: Produção, Montagem, Qualidade...">
                </div>

                <div class="form-group">
                    <label for="descricao">
                        <i class="fas fa-align-left"></i> Descrição:
                    </label>
                    <textarea id="descricao" name="descricao" rows="4"
                              placeholder="Descreva as atividades deste setor..."></textarea>
                </div>

                <div class="form-actions">
                    <button type="button" class="btn secondary" onclick="fecharModal()">
                        <i class="fas fa-times"></i> Cancelar
                    </button>
                    <button type="submit" class="btn primary">
                        <i class="fas fa-save"></i> Salvar Setor
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    // FUNÇÕES DO MODAL
    function abrirModal() {
        document.getElementById('modalSetor').style.display = 'block';
        document.getElementById('modalTitle').innerHTML = '<i class="fas fa-plus"></i> Novo Setor';
        document.getElementById('formSetor').reset();
        document.getElementById('setorId').value = '';

        // Focar no primeiro campo
        setTimeout(() => {
            document.getElementById('nome').focus();
        }, 300);
    }

    function fecharModal() {
        document.getElementById('modalSetor').style.display = 'none';
    }

    function editarSetor(id, nome, descricao) {
        document.getElementById('modalSetor').style.display = 'block';
        document.getElementById('modalTitle').innerHTML = '<i class="fas fa-edit"></i> Editar Setor';
        document.getElementById('setorId').value = id;
        document.getElementById('nome').value = nome;
        document.getElementById('descricao').value = descricao;

        // Focar no primeiro campo
        setTimeout(() => {
            document.getElementById('nome').focus();
        }, 300);
    }

    // Fechar modal clicando fora ou com ESC
    window.onclick = function(event) {
        var modal = document.getElementById('modalSetor');
        if (event.target == modal) {
            fecharModal();
        }
    }

    document.addEventListener('keydown', function(event) {
        if (event.key === 'Escape') {
            fecharModal();
        }
    });

    // Validação do formulário
    document.getElementById('formSetor').addEventListener('submit', function(e) {
        const nome = document.getElementById('nome').value.trim();

        if (!nome) {
            e.preventDefault();
            alert('Por favor, insira um nome para o setor.');
            document.getElementById('nome').focus();
            return;
        }
    });
</script>
</body>
</html>
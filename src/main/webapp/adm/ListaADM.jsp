<%@ page import="java.util.List" %>
<%@ page import="com.example.sancaranservlet.model.Usuario" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    List<Usuario> adms = (List<Usuario>) request.getAttribute("adms");
    if (adms == null) {
        adms = new ArrayList<>();
    }

    String mensagem = (String) session.getAttribute("mensagem");
    String tipoMensagem = (String) session.getAttribute("tipoMensagem");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Administradores - Sancaran</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/adm.css?v=1.0">
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
    <a href="${pageContext.request.contextPath}/MetaServlet" class="btn">
        <i class="fas fa-bullseye"></i> METAS
    </a>
    <a href="${pageContext.request.contextPath}/SetorServlet" class="btn">
        <i class="fas fa-building"></i> SETOR
    </a>
    <a href="${pageContext.request.contextPath}/AdmServlet" class="btn active">
        <i class="fas fa-users-cog"></i> ADMIN
    </a>
    <a href="${pageContext.request.contextPath}/sair.jsp" class="logout">
        <i class="fas fa-sign-out-alt"></i> Sair
    </a>
</div>

<div class="content">
    <div class="container">
        <h1 class="title">
            <i class="fas fa-users-cog"></i> Administradores
        </h1>

        <% if (mensagem != null && !mensagem.isEmpty()) { %>
        <div class="mensagem <%= "erro".equals(tipoMensagem) ? "erro" : "" %>">
            <i class="fas <%= "erro".equals(tipoMensagem) ? "fa-exclamation-triangle" : "fa-info-circle" %>"></i>
            <%= mensagem %>
        </div>
        <%
                session.removeAttribute("mensagem");
                session.removeAttribute("tipoMensagem");
            }
        %>

        <button onclick="abrirModal()" class="btn primary">
            <i class="fas fa-plus"></i> Novo Administrador
        </button>

        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>NOME</th>
                <th>EMAIL</th>
                <th>SENHA</th>
                <th>AÇÕES</th>
            </tr>
            </thead>
            <tbody>
            <%
                if (adms.isEmpty()) {
            %>
            <tr>
                <td colspan="5" class="empty">
                    <i class="fas fa-users"></i> Nenhum administrador cadastrado
                </td>
            </tr>
            <%
            } else {
                for (Usuario u : adms) {
                    // Senha totalmente anonimizada com asteriscos
                    String senhaAnonimizada = "••••••••";
            %>
            <tr>
                <td><strong>#<%= u.getId() %></strong></td>
                <td><%= u.getNome() != null ? u.getNome() : "" %></td>
                <td><%= u.getEmail() != null ? u.getEmail() : "" %></td>
                <td style="font-family: monospace; color: #666; letter-spacing: 2px;">
                    <%= senhaAnonimizada %>
                </td>
                <td class="icons">
                    <button onclick="editarAdm(<%= u.getId() %>, '<%= u.getNome() != null ? u.getNome().replace("'", "\\'") : "" %>', '<%= u.getEmail() != null ? u.getEmail().replace("'", "\\'") : "" %>', '<%= u.getSenha() != null ? u.getSenha().replace("'", "\\'") : "" %>')"
                            class="action edit" title="Editar">
                        <i class="fas fa-edit"></i>
                    </button>

                    <form action="${pageContext.request.contextPath}/AdmServlet" method="post" style="display:inline;">
                        <input type="hidden" name="acao" value="excluir">
                        <input type="hidden" name="id" value="<%= u.getId() %>">
                        <button type="submit" class="action delete" title="Excluir"
                                onclick="return confirm('Tem certeza que deseja excluir este administrador?')">
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
<!-- MODAL PARA NOVO/EDITAR ADMINISTRADOR -->
<div id="modalAdm" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h2 id="modalTitle">
                <i class="fas fa-plus"></i> Novo Administrador
            </h2>
            <span class="close" onclick="fecharModal()">&times;</span>
        </div>
        <div class="modal-body">
            <form id="formAdm" action="AdmServlet" method="post">
                <input type="hidden" id="admId" name="id" value="">

                <div class="form-group">
                    <label for="nome">
                        <i class="fas fa-user"></i> Nome Completo:
                    </label>
                    <input type="text" id="nome" name="nome" required
                           placeholder="Digite o nome completo">
                </div>

                <div class="form-group">
                    <label for="email">
                        <i class="fas fa-envelope"></i> E-mail:
                    </label>
                    <input type="email" id="email" name="email" required
                           placeholder="exemplo@email.com">
                </div>

                <div class="form-group">
                    <label for="senha">
                        <i class="fas fa-lock"></i> Senha:
                    </label>
                    <div style="position: relative;">
                        <input type="password" id="senha" name="senha" required
                               placeholder="Digite a senha" style="padding-right: 45px;">
                        <button type="button" id="toggleSenha"
                                style="position: absolute; right: 10px; top: 50%; transform: translateY(-50%);
                                       background: none; border: none; cursor: pointer; color: #666;"
                                onclick="toggleSenhaVisibility()">
                            <i class="fas fa-eye"></i>
                        </button>
                    </div>
                </div>

                <div class="form-actions">
                    <button type="button" class="btn secondary" onclick="fecharModal()">
                        <i class="fas fa-times"></i> Cancelar
                    </button>
                    <button type="submit" class="btn primary">
                        <i class="fas fa-save"></i> Salvar Administrador
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    // FUNÇÃO PARA MOSTRAR/OCULTAR SENHA
    function toggleSenhaVisibility() {
        const senhaInput = document.getElementById('senha');
        const toggleButton = document.getElementById('toggleSenha');
        const icon = toggleButton.querySelector('i');

        if (senhaInput.type === 'password') {
            senhaInput.type = 'text';
            icon.classList.remove('fa-eye');
            icon.classList.add('fa-eye-slash');
            toggleButton.style.color = '#2B68A6';
        } else {
            senhaInput.type = 'password';
            icon.classList.remove('fa-eye-slash');
            icon.classList.add('fa-eye');
            toggleButton.style.color = '#666';
        }
    }

    // FUNÇÕES DO MODAL
    function abrirModal() {
        document.getElementById('modalAdm').style.display = 'block';
        document.getElementById('modalTitle').innerHTML = '<i class="fas fa-plus"></i> Novo Administrador';
        document.getElementById('formAdm').reset();
        document.getElementById('admId').value = '';

        // Resetar o ícone do olho
        const toggleButton = document.getElementById('toggleSenha');
        const icon = toggleButton.querySelector('i');
        icon.classList.remove('fa-eye-slash');
        icon.classList.add('fa-eye');
        toggleButton.style.color = '#666';

        // Focar no primeiro campo
        setTimeout(() => {
            document.getElementById('nome').focus();
        }, 300);
    }

    function fecharModal() {
        document.getElementById('modalAdm').style.display = 'none';
    }

    function editarAdm(id, nome, email, senha) {
        document.getElementById('modalAdm').style.display = 'block';
        document.getElementById('modalTitle').innerHTML = '<i class="fas fa-edit"></i> Editar Administrador';
        document.getElementById('admId').value = id;
        document.getElementById('nome').value = nome;
        document.getElementById('email').value = email;
        document.getElementById('senha').value = senha; // Preenche com a senha atual

        // Resetar o ícone do olho
        const toggleButton = document.getElementById('toggleSenha');
        const icon = toggleButton.querySelector('i');
        icon.classList.remove('fa-eye-slash');
        icon.classList.add('fa-eye');
        toggleButton.style.color = '#666';

        // Focar no primeiro campo
        setTimeout(() => {
            document.getElementById('nome').focus();
        }, 300);
    }

    // Fechar modal clicando fora
    window.onclick = function(event) {
        var modal = document.getElementById('modalAdm');
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
    document.getElementById('formAdm').addEventListener('submit', function(e) {
        const nome = document.getElementById('nome').value.trim();
        const email = document.getElementById('email').value.trim();
        const senha = document.getElementById('senha').value;

        if (!nome) {
            e.preventDefault();
            alert('Por favor, insira o nome do administrador.');
            document.getElementById('nome').focus();
            return;
        }

        if (!email) {
            e.preventDefault();
            alert('Por favor, insira o e-mail do administrador.');
            document.getElementById('email').focus();
            return;
        }

        if (!senha) {
            e.preventDefault();
            alert('Por favor, insira a senha do administrador.');
            document.getElementById('senha').focus();
            return;
        }
    });
</script>
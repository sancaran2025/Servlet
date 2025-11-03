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
                <th>CPF</th>
                <th>CARGO</th>
                <th>TELEFONE</th>
                <th>AÇÕES</th>
            </tr>
            </thead>
            <tbody>
            <%
                if (adms.isEmpty()) {
            %>
            <tr>
                <td colspan="8" class="empty">
                    <i class="fas fa-users"></i> Nenhum administrador cadastrado
                </td>
            </tr>
            <%
            } else {
                for (Usuario u : adms) {
                    String senhaAnonimizada = "••••••••";
                    String cpf = (u.getCpf() != null && !u.getCpf().isEmpty()) ? u.getCpf() : "---";
                    String cargo = (u.getCargo() != null && !u.getCargo().isEmpty()) ? u.getCargo() : "---";
                    String telefone = (u.getTelefone() != null && !u.getTelefone().isEmpty()) ? u.getTelefone() : "---";
            %>
            <tr>
                <td><strong>#<%= u.getId() %></strong></td>
                <td><%= u.getNome() != null ? u.getNome() : "" %></td>
                <td><%= u.getEmail() != null ? u.getEmail() : "" %></td>
                <td style="font-family: monospace; color: #666; letter-spacing: 2px;">
                    <%= senhaAnonimizada %>
                </td>
                <td><%= cpf %></td>
                <td><%= cargo %></td>
                <td><%= telefone %></td>
                <td class="icons">
                    <button onclick="editarAdm(
                        <%= u.getId() %>,
                            '<%= u.getNome() != null ? u.getNome().replace("'", "\\'") : "" %>',
                            '<%= u.getEmail() != null ? u.getEmail().replace("'", "\\'") : "" %>',
                            '<%= u.getSenha() != null ? u.getSenha().replace("'", "\\'") : "" %>',
                            '<%= u.getCpf() != null ? u.getCpf().replace("'", "\\'") : "" %>',
                            '<%= u.getCargo() != null ? u.getCargo().replace("'", "\\'") : "" %>',
                            '<%= u.getTelefone() != null ? u.getTelefone().replace("'", "\\'") : "" %>'
                            )"
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

<!-- MODAL NOVO/EDITAR COM TODOS OS DADOS -->
<div id="modalAdm" class="modal">
    <div class="modal-content">
        <div class="modal-header">
            <h2 id="modalTitle"><i class="fas fa-plus"></i> Novo Administrador</h2>
            <span class="close" onclick="fecharModal()">&times;</span>
        </div>
        <div class="modal-body">
            <form id="formAdm" action="AdmServlet" method="post">
                <input type="hidden" id="admId" name="id" value="">

                <div class="form-group">
                    <label for="nome"><i class="fas fa-user"></i> Nome Completo:</label>
                    <input type="text" id="nome" name="nome" required placeholder="Digite o nome completo">
                </div>

                <div class="form-group">
                    <label for="email"><i class="fas fa-envelope"></i> E-mail:</label>
                    <input type="email" id="email" name="email" required placeholder="exemplo@email.com">
                </div>

                <div class="form-group">
                    <label for="senha"><i class="fas fa-lock"></i> Senha:</label>
                    <div style="position: relative;">
                        <input type="password" id="senha" name="senha" required placeholder="Digite a senha" style="padding-right: 45px;">
                        <button type="button" id="toggleSenha" onclick="toggleSenhaVisibility()">
                            <i class="fas fa-eye"></i>
                        </button>
                    </div>
                </div>

                <div class="form-group">
                    <label for="cpf"><i class="fas fa-id-card"></i> CPF:</label>
                    <input type="text" id="cpf" name="cpf" placeholder="Digite o CPF">
                </div>

                <div class="form-group">
                    <label for="cargo"><i class="fas fa-briefcase"></i> Cargo:</label>
                    <input type="text" id="cargo" name="cargo" placeholder="Digite o cargo">
                </div>

                <div class="form-group">
                    <label for="telefone"><i class="fas fa-phone"></i> Telefone:</label>
                    <input type="text" id="telefone" name="telefone" placeholder="Digite o telefone">
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
    function toggleSenhaVisibility() {
        const senhaInput = document.getElementById('senha');
        const toggleButton = document.getElementById('toggleSenha');
        const icon = toggleButton.querySelector('i');
        if (senhaInput.type === 'password') {
            senhaInput.type = 'text';
            icon.classList.remove('fa-eye');
            icon.classList.add('fa-eye-slash');
        } else {
            senhaInput.type = 'password';
            icon.classList.remove('fa-eye-slash');
            icon.classList.add('fa-eye');
        }
    }

    function abrirModal() {
        document.getElementById('modalAdm').style.display = 'block';
        document.getElementById('modalTitle').innerHTML = '<i class="fas fa-plus"></i> Novo Administrador';
        document.getElementById('formAdm').reset();
        document.getElementById('admId').value = '';
    }

    function fecharModal() {
        document.getElementById('modalAdm').style.display = 'none';
    }

    function editarAdm(id, nome, email, senha, cpf, cargo, telefone) {
        document.getElementById('modalAdm').style.display = 'block';
        document.getElementById('modalTitle').innerHTML = '<i class="fas fa-edit"></i> Editar Administrador';
        document.getElementById('admId').value = id;
        document.getElementById('nome').value = nome;
        document.getElementById('email').value = email;
        document.getElementById('senha').value = senha;
        document.getElementById('cpf').value = cpf;
        document.getElementById('cargo').value = cargo;
        document.getElementById('telefone').value = telefone;
    }

    window.onclick = function(event) {
        var modal = document.getElementById('modalAdm');
        if (event.target == modal) fecharModal();
    }

    document.addEventListener('keydown', function(event) {
        if (event.key === 'Escape') fecharModal();
    });
</script>
</body>
</html>

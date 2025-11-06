<%@ page import="java.util.List" %>
<%@ page import="com.example.sancaranservlet.model.Usuario" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuarios");
    if (usuarios == null) {
        usuarios = new ArrayList<>();
    }

    String filtroTipo = request.getParameter("filtroTipo");
    if (filtroTipo == null) filtroTipo = "todos";
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Visualizar Usuários - Sancaran</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/adm.css?v=1.0">
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
    <a href="${pageContext.request.contextPath}/metas-visualizacao" class="btn">
        <i class="fas fa-bullseye"></i> METAS
    </a>
    <a href="${pageContext.request.contextPath}/VisualizacaoSetorServlet" class="btn">
        <i class="fas fa-building"></i> SETOR
    </a>
    <a href="${pageContext.request.contextPath}/UsuarioVisualizacaoServlet" class="btn active">
        <i class="fas fa-users-cog"></i> ADMIN
    </a>
    <a href="${pageContext.request.contextPath}/index.html" class="logout">
        <i class="fas fa-sign-out-alt"></i> Sair
    </a>
</div>

<div class="content">
    <div class="container">
        <h1 class="title">
            <i class="fas fa-users"></i> Visualizar Usuários
        </h1>

        <!-- FILTRO POR TIPO -->
        <div class="filtro-container">
            <label for="filtroTipo" class="filtro-label">
                <i class="fas fa-filter"></i> Filtrar por tipo:
            </label>
            <select id="filtroTipo" name="filtroTipo" onchange="filtrarUsuarios(this.value)" class="filtro-select">
                <option value="todos" <%= "todos".equals(filtroTipo) ? "selected" : "" %>>Todos os Usuários</option>
                <option value="A" <%= "A".equals(filtroTipo) ? "selected" : "" %>>Administradores</option>
                <option value="G" <%= "G".equals(filtroTipo) ? "selected" : "" %>>Gestores</option>
                <option value="S" <%= "S".equals(filtroTipo) ? "selected" : "" %>>Supervisores</option>
                <option value="F" <%= "F".equals(filtroTipo) ? "selected" : "" %>>Funcionários</option>
            </select>

            <span class="contador-usuarios">
                <i class="fas fa-user-friends"></i>
                <%= usuarios.size() %> usuário(s) encontrado(s)
            </span>
        </div>

        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>NOME</th>
                <th>EMAIL</th>
                <th>TIPO</th>
                <th>CARGO</th>
                <th>TELEFONE</th>
            </tr>
            </thead>
            <tbody>
            <%
                if (usuarios.isEmpty()) {
            %>
            <tr>
                <td colspan="6" class="empty">
                    <i class="fas fa-users"></i> Nenhum usuário encontrado
                </td>
            </tr>
            <%
            } else {
                int usuariosFiltrados = 0;
                for (Usuario u : usuarios) {
                    // Aplicar filtro se necessário
                    if (!"todos".equals(filtroTipo) && u.getTipo() != filtroTipo.charAt(0)) {
                        continue;
                    }
                    usuariosFiltrados++;

                    String tipoDescricao = "";
                    String tipoIcon = "";
                    String tipoCor = "";

                    switch(u.getTipo()) {
                        case 'A':
                            tipoDescricao = "Administrador";
                            tipoIcon = "fa-users-cog";
                            tipoCor = "#e74c3c";
                            break;
                        case 'G':
                            tipoDescricao = "Gestor";
                            tipoIcon = "fa-user-tie";
                            tipoCor = "#2ecc71";
                            break;
                        case 'S':
                            tipoDescricao = "Supervisor";
                            tipoIcon = "fa-user-shield";
                            tipoCor = "#3498db";
                            break;
                        case 'F':
                            tipoDescricao = "Funcionário";
                            tipoIcon = "fa-user";
                            tipoCor = "#95a5a6";
                            break;
                        default:
                            tipoDescricao = "Desconhecido";
                            tipoIcon = "fa-question";
                            tipoCor = "#7f8c8d";
                    }
            %>
            <tr>
                <td><strong>#<%= u.getId() %></strong></td>
                <td>
                    <div class="user-info">
                        <div class="user-name"><%= u.getNome() != null ? u.getNome() : "" %></div>
                        <% if(u.getCpf() != null && !u.getCpf().isEmpty()) { %>
                        <div class="user-cpf">CPF: <%= u.getCpf() %></div>
                        <% } %>
                    </div>
                </td>
                <td class="email-cell">
                    <i class="fas fa-envelope"></i>
                    <%= u.getEmail() != null ? u.getEmail() : "" %>
                </td>
                <td>
                    <span class="tipo-badge" style="background-color: <%= tipoCor %>;">
                        <i class="fas <%= tipoIcon %>"></i>
                        <%= tipoDescricao %>
                    </span>
                </td>
                <td><%= u.getCargo() != null ? u.getCargo() : "---" %></td>
                <td class="telefone-cell">
                    <% if(u.getTelefone() != null && !u.getTelefone().isEmpty()) { %>
                    <i class="fas fa-phone"></i>
                    <%= u.getTelefone() %>
                    <% } else { %>
                    <span class="sem-info">---</span>
                    <% } %>
                </td>
            </tr>
            <%
                }

                // Se filtro ativo e nenhum usuário encontrado
                if (usuariosFiltrados == 0 && !"todos".equals(filtroTipo)) {
            %>
            <tr>
                <td colspan="6" class="empty">
                    <i class="fas fa-search"></i> Nenhum usuário do tipo <%= filtroTipo %> encontrado
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

<script>
    // FUNÇÃO PARA FILTRAR USUÁRIOS
    function filtrarUsuarios(tipo) {
        window.location.href = '${pageContext.request.contextPath}/UsuarioVisualizacaoServlet?filtroTipo=' + tipo;
    }
</script>
</body>
</html>
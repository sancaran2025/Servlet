<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.example.sancaranservlet.model.Usuario" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Editar Administrador</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/CriaEditaADM.css">
</head>
<body>
<div class="container">
    <h1>Editar Administrador</h1>

    <%
        Usuario adm = (Usuario) request.getAttribute("adm");
        if (adm != null) {
    %>
    <form action="${pageContext.request.contextPath}/AdmServlet" method="post" class="form-cadastro">
        <input type="hidden" name="acao" value="atualizar">
        <input type="hidden" name="id" value="<%= adm.getId() %>">

        <input type="text" name="nome" value="<%= adm.getNome() %>" required>
        <input type="email" name="email" value="<%= adm.getEmail() %>" required>
        <input type="password" name="senha" placeholder="Nova senha (opcional)">
        <button type="submit">Salvar Alterações</button>
    </form>
    <% } else { %>
    <p>Administrador não encontrado.</p>
    <% } %>

    <a href="${pageContext.request.contextPath}/AdmServlet" class="voltar">← Voltar para a lista</a>
</div>
</body>
</html>
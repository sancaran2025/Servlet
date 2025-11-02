<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.example.sancaranservlet.model.Setor" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    Setor setor = (Setor) request.getAttribute("setor");
    if (setor == null) {
        response.sendRedirect("SetorServlet");
        return;
    }
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Editar Setor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/editarSetor.css">
</head>
<body>
<div class="container">
    <h2>Editar Setor</h2>
    <form action="${pageContext.request.contextPath}/SetorServlet" method="post">
        <input type="hidden" name="acao" value="salvar">
        <input type="hidden" name="id" value="<%= setor.getId() %>">

        <div class="form-group">
            <label>Nome:</label>
            <input type="text" name="nome" value="<%= setor.getNome() %>" required>
        </div>

        <div class="form-group">
            <label>Descrição:</label>
            <textarea name="descricao" rows="4"><%= setor.getDescricao() %></textarea>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn-primary">Atualizar</button>
            <a href="${pageContext.request.contextPath}/SetorServlet" class="btn-secondary">Cancelar</a>
        </div>
    </form>
</div>
</body>
</html>
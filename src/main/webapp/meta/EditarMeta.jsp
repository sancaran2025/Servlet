<%@ page import="com.example.sancaranservlet.dao.MetaDAO, com.example.sancaranservlet.model.Meta" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    int id = Integer.parseInt(request.getParameter("id"));
    MetaDAO dao = new MetaDAO();
    Meta meta = dao.buscarPorId(id);
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Editar Meta</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/editarMeta.css">
</head>
<body>
<div class="container">
    <h1>Editar Meta</h1>
    <form action="${pageContext.request.contextPath}/MetaServlet" method="post">
        <input type="hidden" name="acao" value="atualizar">
        <input type="hidden" name="id" value="<%=meta.getId()%>">

        <div class="form-group">
            <label>Data Início:</label>
            <input type="date" name="dataInicio" value="<%=meta.getDataInicio()%>" required>
        </div>

        <div class="form-group">
            <label>Data Fim:</label>
            <input type="date" name="dataFim" value="<%=meta.getDataFim()%>" required>
        </div>

        <div class="form-group">
            <label>Descrição:</label>
            <input type="text" name="descricao" value="<%=meta.getDescricao()%>" required>
        </div>

        <div class="form-group">
            <label>Observações:</label>
            <textarea name="observacoes"><%=meta.getObservacoes()%></textarea>
        </div>

        <div class="form-group">
            <label>Status:</label>
            <select name="status" required>
                <option value="P" <%= meta.getStatus() == 'P' ? "selected" : "" %>>Pendente</option>
                <option value="A" <%= meta.getStatus() == 'A' ? "selected" : "" %>>Em Andamento</option>
                <option value="C" <%= meta.getStatus() == 'C' ? "selected" : "" %>>Concluída</option>
            </select>
        </div>

        <div class="form-group">
            <label>ID Produção:</label>
            <input type="number" name="idProducao" value="<%=meta.getIdProducao()%>">
        </div>

        <div class="form-actions">
            <button type="submit" class="btn-primary">Salvar Alterações</button>
            <a href="${pageContext.request.contextPath}/MetaServlet" class="btn-secondary">Cancelar</a>
        </div>
    </form>
</div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Novo Setor</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/novoSetor.css">
</head>
<body>
<div class="container">
    <h2>Novo Setor</h2>
    <form action="${pageContext.request.contextPath}/SetorServlet" method="post">
        <input type="hidden" name="acao" value="salvar">

        <div class="form-group">
            <label>Nome:</label>
            <input type="text" name="nome" required>
        </div>

        <div class="form-group">
            <label>Descrição:</label>
            <textarea name="descricao" rows="4"></textarea>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn-primary">Salvar</button>
            <a href="${pageContext.request.contextPath}/SetorServlet" class="btn-secondary">Cancelar</a>
        </div>
    </form>
</div>
</body>
</html>
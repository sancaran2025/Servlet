<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>PAINEL DE ADM - SANCARAN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/arearestrita.css">
</head>
<body>
<div class="container">
    <h1 class="title">SANCARAN</h1>
    <h2 class="subtitle">PAINEL DE ADM</h2>
    <p class="instruction">ESCOLHA ALGO PARA MODIFICAR</p>

    <div class="menu">
        <a href="meta/ListaMeta.jsp" class="menu-item">META</a>
        <a href="AdmServlet" class="menu-item">ADM</a>
    </div>

    <div class="divider"></div>

    <div class="production">
        <a href="ProducaoServlet" class="prod-item">PRODUÇÃO</a>
        <a href="setor/Setor.jsp" class="prod-item">SETOR</a>
    </div>
</div>
</body>
</html>

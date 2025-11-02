<%@ page import="com.example.sancaranservlet.model.Usuario" %>
<%
    Usuario usuarioLogado = (Usuario) session.getAttribute("usuario");
    if (usuarioLogado == null) {
        response.sendRedirect("LoginRestrito.jsp");
        return;
    }
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Painel Administrativo - Sancaran</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/arearestrita.css">
</head>
<body>
<header class="painel-header">
    <div class="header-content">
        <div class="logo-area">
            <img src="${pageContext.request.contextPath}/imgs/logo.png" alt="Logo Sancaran" class="logo-img">
            <h1>SANCARAN</h1>
        </div>
        <div class="user-area">
            <span>Bem-vindo, <strong><%= usuarioLogado.getNome() %></strong></span>
            <a href="${pageContext.request.contextPath}/sair.jsp" class="logout-btn">🚪 Sair</a>
        </div>
    </div>
</header>

<main class="painel-conteudo">
    <div class="cards-grid">
        <a href="${pageContext.request.contextPath}/AdmServlet" class="card-item">
            <img src="${pageContext.request.contextPath}/imgs/administrativo.jpg" alt="Administradores">
            <div class="card-info">
                <h3>Administradores</h3>
                <p>Gerencie os administradores do sistema</p>
            </div>
        </a>

        <a href="${pageContext.request.contextPath}/SetorServlet" class="card-item">
            <img src="${pageContext.request.contextPath}/imgs/setor.jpg" alt="Setores">
            <div class="card-info">
                <h3>Setores</h3>
                <p>Organize os setores da empresa</p>
            </div>
        </a>

        <a href="${pageContext.request.contextPath}/MetaServlet" class="card-item">
            <img src="${pageContext.request.contextPath}/imgs/meta.jpg" alt="Metas">
            <div class="card-info">
                <h3>Metas</h3>
                <p>Controle as metas de produção</p>
            </div>
        </a>

        <a href="${pageContext.request.contextPath}/ProducaoServlet" class="card-item">
            <img src="${pageContext.request.contextPath}/imgs/producao.jpg" alt="Produção">
            <div class="card-info">
                <h3>Produção</h3>
                <p>Acompanhe o desempenho da produção</p>
            </div>
        </a>
    </div>
</main>
</body>
</html>

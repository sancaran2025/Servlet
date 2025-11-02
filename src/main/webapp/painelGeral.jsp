<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.example.sancaranservlet.model.Usuario" %>
<%
    if (session.getAttribute("usuarioLogado") == null) {
        response.sendRedirect("EntrarConta.jsp");
        return;
    }
    Usuario usuario = (Usuario) session.getAttribute("usuario");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>PAINEL DE VISUALIZAÇÃO - SANCARAN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/painelGeral.css">
</head>
<body>
<header class="painel-header">
    <div class="header-content">
        <div class="logo-area">
            <img src="${pageContext.request.contextPath}/imgs/logo.png" alt="Logo Sancaran" class="logo-img">
            <h1>SANCARAN</h1>
        </div>
        <div class="user-area">
            <span>👋 Olá, <%= usuario.getNome() %></span>
            <a href="logout" class="logout-btn">🚪 Sair</a>
        </div>
    </div>
</header>

<main class="painel-conteudo">
    <h2 class="painel-titulo">PAINEL DE VISUALIZAÇÃO</h2>

    <div class="visualizacao-badge">
        <div class="badge">MODO APENAS VISUALIZAÇÃO</div>
    </div>

    <p class="painel-instrucao">ESCOLHA ALGO PARA VISUALIZAR</p>

    <div class="cards-grid">
        <!-- USANDO AS IMAGENS QUE VOCÊ JÁ TEM -->
        <a href="meta/ListaMetaVisualizacao.jsp" class="card-item">
            <img src="${pageContext.request.contextPath}/imgs/meta.jpg" alt="Metas">
            <div class="card-info">
                <h3>METAS</h3>
                <p>Visualizar metas e objetivos do setor</p>
            </div>
        </a>

        <a href="painel-usuarios.jsp" class="card-item">
            <img src="${pageContext.request.contextPath}/imgs/administrativo.jpg" alt="Usuários">
            <div class="card-info">
                <h3>USUÁRIOS</h3>
                <p>Visualizar todos os usuários do sistema</p>
            </div>
        </a>

        <a href="ProducaoVisualizacaoServlet" class="card-item">
            <img src="${pageContext.request.contextPath}/imgs/producao.jpg" alt="Produção">
            <div class="card-info">
                <h3>PRODUÇÃO</h3>
                <p>Visualizar dados de produção</p>
            </div>
        </a>

        <a href="setor/SetorVisualizacao.jsp" class="card-item">
            <img src="${pageContext.request.contextPath}/imgs/setor.jpg" alt="Setor">
            <div class="card-info">
                <h3>SETOR</h3>
                <p>Visualizar informações do setor</p>
            </div>
        </a>
    </div>
</main>
</body>
</html>
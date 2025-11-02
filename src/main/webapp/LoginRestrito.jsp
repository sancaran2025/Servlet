<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <title>Login - Painel Administrativo</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/loginADM.css?v=2.1">
</head>
<body>
<header class="painel-header">
  <div class="header-content">
    <div class="logo-area">
      <img src="${pageContext.request.contextPath}/imgs/logo.png" alt="Logo" class="logo-img">
      <h1>Login Administrativo</h1>
    </div>
    <a href="${pageContext.request.contextPath}/index.html" class="back-btn">
      ← Voltar ao Site
    </a>
  </div>
</header>

<main class="login-container">
  <div class="mascote-side">
    <img src="${pageContext.request.contextPath}/imgs/pet.png" alt="Mascote" class="mascote-img">
  </div>

  <form action="login" method="post" class="login-form">
    <h2>Entrar no Sistema</h2>
    <input type="email" name="email" placeholder="E-mail" required>
    <input type="password" name="senha" placeholder="Senha" required>
    <button type="submit">Entrar</button>

    <%
      String erro = request.getParameter("erro");
      if ("1".equals(erro)) {
    %>
    <p class="erro">Usuário ou senha inválidos!</p>
    <%
    } else if ("2".equals(erro)) {
    %>
    <p class="erro">Acesso negado! Somente administradores podem entrar.</p>
    <%
      }
    %>
  </form>
</main>
</body>
</html>
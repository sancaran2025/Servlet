<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Entrar na Conta - Sancaran</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/entrarConta.css">
</head>
<body>
<!-- HEADER -->
<header class="painel-header">
    <div class="header-content">
        <div class="logo-area">
            <img src="${pageContext.request.contextPath}/imgs/logo.png" alt="Logo Sancaran" class="logo-img">
            <h1>Entrar na Conta</h1>
        </div>
        <a href="${pageContext.request.contextPath}/index.jsp" class="back-btn">
            ← Voltar ao Site
        </a>
    </div>
</header>

<!-- CONTAINER PRINCIPAL -->
<main class="login-container">
    <!-- LADO DO MASCOTE -->
    <div class="mascote-side">
        <img src="${pageContext.request.contextPath}/imgs/pet.png" alt="Mascote Sancaran" class="mascote-img">
    </div>

    <!-- FORMULÁRIO DE LOGIN -->
    <div class="form-side">
        <%
            String error = request.getParameter("error");
            String success = request.getParameter("success");

            if (error != null) {
        %>
        <div class="alert error-alert">
            <span class="alert-icon">⚠️</span>
            <span class="alert-message"><%= error %></span>
        </div>
        <% } %>

        <% if (success != null) { %>
        <div class="alert success-alert">
            <span class="alert-icon">✅</span>
            <span class="alert-message"><%= success %></span>
        </div>
        <% } %>

        <form action="${pageContext.request.contextPath}/entrar-conta" method="post" class="auth-form">
            <h2>Entrar na Conta</h2>
            <p class="form-subtitle">Digite suas credenciais para acessar sua conta</p>

            <div class="form-group">
                <input type="email" id="email" name="email" placeholder="Email *" required class="form-input">
            </div>

            <div class="form-group">
                <input type="password" id="password" name="password" placeholder="Senha *" required class="form-input">
            </div>

            <button type="submit" class="btn-primary">
                <span class="btn-text">Entrar na Conta</span>
            </button>

            <div class="auth-footer">
                <p>Não tem uma conta? <a href="${pageContext.request.contextPath}/criarConta.jsp" class="link">Crie uma aqui</a></p>
            </div>
        </form>
    </div>
</main>

<script>
    // Validação básica do formulário
    document.querySelector('.auth-form').addEventListener('submit', function(e) {
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        if (!email || !password) {
            e.preventDefault();
            alert('Por favor, preencha todos os campos obrigatórios.');
            return false;
        }

        // Validação básica de email
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            e.preventDefault();
            alert('Por favor, digite um email válido.');
            return false;
        }

        return true;
    });
</script>
</body>
</html>
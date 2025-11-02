<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Criar Conta - Sancaran</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/criarConta.css?v=2.0">
    <style>
        .phone-validation {
            font-size: 0.75rem;
            margin-top: 4px;
            display: none;
        }

        .phone-valid {
            color: #16a34a;
        }

        .phone-invalid {
            color: #dc2626;
        }

        .input-valid {
            border-color: #16a34a !important;
        }

        .input-invalid {
            border-color: #dc2626 !important;
        }

        .password-error {
            color: #dc2626;
            font-size: 0.75rem;
            margin-top: 4px;
            display: none;
        }
    </style>
</head>
<body>
<!-- HEADER NO MESMO ESTILO -->
<header class="painel-header">
    <div class="header-content">
        <div class="logo-area">
            <img src="${pageContext.request.contextPath}/imgs/logo.png" alt="Logo Sancaran" class="logo-img">
            <h1>Criar Nova Conta</h1>
        </div>
        <a href="${pageContext.request.contextPath}/index.jsp" class="back-btn">
            ← Voltar ao Site
        </a>
    </div>
</header>

<!-- CONTAINER PRINCIPAL COM MASCOTE -->
<main class="login-container">
    <!-- LADO DO MASCOTE -->
    <div class="mascote-side">
        <img src="${pageContext.request.contextPath}/imgs/pet.png" alt="Mascote Sancaran" class="mascote-img">
    </div>

    <!-- FORMULÁRIO DE CRIAÇÃO DE CONTA -->
    <div class="form-side">
        <!-- Mensagens -->
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

        <form id="create-account-form" action="${pageContext.request.contextPath}/criar-conta" method="post" class="auth-form">
            <h2>Criar Nova Conta</h2>
            <p class="form-subtitle">Preencha os dados abaixo para criar sua conta</p>

            <div class="form-grid">
                <div class="form-group">
                    <input type="text" id="fullname" name="fullname" placeholder="Nome Completo *" required class="form-input">
                </div>

                <div class="form-group">
                    <input type="email" id="email" name="email" placeholder="Email *" required class="form-input">
                </div>

                <div class="form-group">
                    <input type="text" id="cpf" name="cpf" placeholder="CPF *" required class="form-input" maxlength="14">
                </div>

                <div class="form-group">
                    <input type="tel" id="phone" name="phone" placeholder="Telefone *" required class="form-input">
                    <div id="phone-validation" class="phone-validation">
                        <span id="phone-valid" class="phone-valid">✅ Telefone válido</span>
                        <span id="phone-invalid" class="phone-invalid">❌ Digite um telefone completo com DDD</span>
                    </div>
                </div>

                <div class="form-group">
                    <input type="text" id="cep" name="cep" placeholder="CEP *" required class="form-input" maxlength="9">
                </div>

                <div class="form-group">
                    <input type="password" id="password" name="password" placeholder="Senha * (Mínimo 8 caracteres)" required class="form-input" minlength="8">
                </div>

                <div class="form-group">
                    <input type="password" id="confirm-password" name="confirm-password" placeholder="Confirmar Senha *" required class="form-input" minlength="8">
                    <div id="password-error" class="password-error">
                        ❌ As senhas não coincidem.
                    </div>
                </div>

                <!-- Classificação -->
                <div class="form-group full-width">
                    <label class="form-label">Classificação *</label>
                    <div class="radio-group">
                        <label class="radio-option">
                            <input type="radio" name="tipo" value="F" checked>
                            <span class="radio-checkmark"></span>
                            <span class="radio-label">Funcionário</span>
                        </label>
                        <label class="radio-option">
                            <input type="radio" name="tipo" value="G">
                            <span class="radio-checkmark"></span>
                            <span class="radio-label">Gestor</span>
                        </label>
                        <label class="radio-option">
                            <input type="radio" name="tipo" value="S">
                            <span class="radio-checkmark"></span>
                            <span class="radio-label">Supervisor</span>
                        </label>
                    </div>
                </div>

                <div class="form-group">
                    <input type="text" id="cargo" name="cargo" placeholder="Cargo *" required class="form-input">
                </div>

                <div class="form-group">
                    <input type="text" id="sector" name="sector" placeholder="Setor/Departamento *" required class="form-input">
                </div>
            </div>

            <!-- Termos -->
            <div class="checkbox-group">
                <label class="checkbox-option">
                    <input type="checkbox" id="terms" name="terms" required>
                    <span class="checkbox-checkmark"></span>
                    <span class="checkbox-label">Concordo com os <a href="#" class="link">Termos e Condições</a></span>
                </label>
            </div>

            <!-- Botão -->
            <button type="submit" class="btn-primary">
                <span class="btn-text">Criar Conta</span>
            </button>

            <!-- Link para login -->
            <div class="auth-footer">
                <p>Já tem uma conta? <a href="${pageContext.request.contextPath}/EntrarConta.jsp" class="link">Entre aqui</a></p>
            </div>
        </form>
    </div>
</main>

<script>
    // Elementos DOM
    const form = document.getElementById('create-account-form');
    const passwordError = document.getElementById('password-error');
    const phoneInput = document.getElementById('phone');
    const phoneValidation = document.getElementById('phone-validation');
    const phoneValid = document.getElementById('phone-valid');
    const phoneInvalid = document.getElementById('phone-invalid');

    // ===== VALIDAÇÃO DE TELEFONE =====
    phoneInput.addEventListener('input', function(e) {
        let value = e.target.value.replace(/\D/g, '');

        // Limita a 11 dígitos
        if (value.length > 11) {
            value = value.substring(0, 11);
        }

        // Formatação progressiva
        if (value.length === 11) {
            value = value.replace(/(\d{2})(\d{5})(\d{4})/, '($1) $2-$3');
        } else if (value.length > 6) {
            value = value.replace(/(\d{2})(\d{0,5})/, '($1) $2');
        } else if (value.length > 2) {
            value = value.replace(/(\d{2})(\d{0,4})/, '($1) $2');
        } else if (value.length > 0) {
            value = value.replace(/(\d{0,2})/, '($1');
        }

        e.target.value = value;

        // Feedback visual em tempo real
        const phoneDigits = value.replace(/\D/g, '');
        if (phoneDigits.length === 11) {
            phoneInput.classList.remove('input-invalid');
            phoneInput.classList.add('input-valid');
            phoneValidation.style.display = 'block';
            phoneValid.style.display = 'inline';
            phoneInvalid.style.display = 'none';
        } else if (phoneDigits.length > 0) {
            phoneInput.classList.remove('input-valid');
            phoneInput.classList.add('input-invalid');
            phoneValidation.style.display = 'block';
            phoneValid.style.display = 'none';
            phoneInvalid.style.display = 'inline';
        } else {
            phoneInput.classList.remove('input-valid', 'input-invalid');
            phoneValidation.style.display = 'none';
        }
    });

    // ===== FORMATAÇÃO DE CPF =====
    document.getElementById('cpf').addEventListener('input', function(e) {
        let value = e.target.value.replace(/\D/g, '');
        if (value.length > 11) value = value.substring(0, 11);

        if (value.length > 9) {
            value = value.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
        } else if (value.length > 6) {
            value = value.replace(/(\d{3})(\d{3})(\d{0,3})/, '$1.$2.$3');
        } else if (value.length > 3) {
            value = value.replace(/(\d{3})(\d{0,3})/, '$1.$2');
        }

        e.target.value = value;
    });

    // ===== FORMATAÇÃO DE CEP =====
    document.getElementById('cep').addEventListener('input', function(e) {
        let value = e.target.value.replace(/\D/g, '');
        if (value.length > 8) value = value.substring(0, 8);

        if (value.length > 5) {
            value = value.replace(/(\d{5})(\d{0,3})/, '$1-$2');
        }

        e.target.value = value;
    });

    // ===== VALIDAÇÃO NO ENVIO DO FORMULÁRIO =====
    form.addEventListener('submit', function(e) {
        let isValid = true;

        // Validação do Telefone
        const phoneDigits = phoneInput.value.replace(/\D/g, '');
        if (phoneDigits.length !== 11) {
            isValid = false;
            phoneInput.classList.add('input-invalid');
            phoneValidation.style.display = 'block';
            phoneValid.style.display = 'none';
            phoneInvalid.style.display = 'inline';
            alert('❌ Telefone inválido! Digite um número completo com DDD + 9 dígitos.\nEx: (11) 98765-4321');
            phoneInput.focus();
        }

        // Validação das Senhas
        const password = document.getElementById('password');
        const confirm = document.getElementById('confirm-password');

        if (password.value !== confirm.value) {
            isValid = false;
            passwordError.style.display = 'block';
            if (isValid) {
                confirm.focus();
            }
        } else {
            passwordError.style.display = 'none';
        }

        // Validação de CPF (mínimo 11 dígitos)
        const cpfDigits = document.getElementById('cpf').value.replace(/\D/g, '');
        if (cpfDigits.length !== 11) {
            isValid = false;
            alert('❌ CPF inválido! Digite um CPF completo com 11 dígitos.');
            document.getElementById('cpf').focus();
        }

        // Validação de CEP (mínimo 8 dígitos)
        const cepDigits = document.getElementById('cep').value.replace(/\D/g, '');
        if (cepDigits.length !== 8) {
            isValid = false;
            alert('❌ CEP inválido! Digite um CEP completo com 8 dígitos.');
            document.getElementById('cep').focus();
        }

        if (!isValid) {
            e.preventDefault();
            return false;
        }

        return true;
    });

    // ===== ESCONDER MENSAGENS DE ERRO AO DIGITAR =====
    document.getElementById('confirm-password').addEventListener('input', function() {
        passwordError.style.display = 'none';
    });

    phoneInput.addEventListener('input', function() {
        const phoneDigits = this.value.replace(/\D/g, '');
        if (phoneDigits.length === 11) {
            phoneValidation.style.display = 'block';
            phoneValid.style.display = 'inline';
            phoneInvalid.style.display = 'none';
        }
    });

    // ===== VALIDAÇÃO EM TEMPO REAL DAS SENHAS =====
    document.getElementById('confirm-password').addEventListener('input', function() {
        const password = document.getElementById('password').value;
        const confirm = this.value;

        if (confirm.length > 0 && password !== confirm) {
            this.classList.add('input-invalid');
            this.classList.remove('input-valid');
        } else if (confirm.length > 0 && password === confirm) {
            this.classList.add('input-valid');
            this.classList.remove('input-invalid');
        } else {
            this.classList.remove('input-valid', 'input-invalid');
        }
    });

    document.getElementById('password').addEventListener('input', function() {
        const confirm = document.getElementById('confirm-password');
        if (confirm.value.length > 0) {
            if (this.value !== confirm.value) {
                confirm.classList.add('input-invalid');
                confirm.classList.remove('input-valid');
            } else {
                confirm.classList.add('input-valid');
                confirm.classList.remove('input-invalid');
            }
        }
    });
</script>
</body>
</html>
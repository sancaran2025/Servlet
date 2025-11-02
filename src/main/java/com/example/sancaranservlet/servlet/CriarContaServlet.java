package com.example.sancaranservlet.servlet;

import com.example.sancaranservlet.dao.UsuarioDAO;
import com.example.sancaranservlet.model.Usuario;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "CriarContaServlet", value = "/criar-conta")
public class CriarContaServlet extends HttpServlet {


    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obter parâmetros do formulário
        String nome = request.getParameter("fullname");
        String email = request.getParameter("email");
        String senha = request.getParameter("password");
        String confirmSenha = request.getParameter("confirm-password");
        String telefone = request.getParameter("phone");
        String cpf = request.getParameter("cpf");
        String cep = request.getParameter("cep");
        String setor = request.getParameter("sector");
        String tipo = request.getParameter("tipo"); // "F", "G", "S"
        String cargo = request.getParameter("cargo");
        String terms = request.getParameter("terms");

        // ===== VALIDAÇÕES COMPLETAS =====
        String errorMessage = validarDados(nome, email, senha, confirmSenha, telefone, cpf, cep, setor, tipo, cargo, terms);

        if (errorMessage != null) {
            response.sendRedirect("criarConta.jsp?error=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8));
            return;
        }

        // Verificar se usuário já existe
        if (usuarioDAO.usuarioExiste(cpf, email)) {
            response.sendRedirect("criarConta.jsp?error=" + URLEncoder.encode("CPF ou Email já cadastrado", StandardCharsets.UTF_8));
            return;
        }

        // Criar objeto Usuario
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setCpf(limparNumeros(cpf));
        usuario.setCep(limparNumeros(cep));
        usuario.setTipo(tipo.charAt(0)); // Converte String para char
        usuario.setEmail(email);
        usuario.setCargo(cargo);
        usuario.setSetor(setor);
        usuario.setSenha(senha); // Você pode adicionar hash aqui depois

        // Inserir no banco
        if (usuarioDAO.inserirUsuarioComTelefone(usuario, limparNumeros(telefone))) {
            response.sendRedirect("EntrarConta.jsp?success=" + URLEncoder.encode("Conta criada com sucesso! Faça login.", StandardCharsets.UTF_8));
        } else {
            response.sendRedirect("criarConta.jsp?error=" + URLEncoder.encode("Erro ao criar conta. Tente novamente.", StandardCharsets.UTF_8));
        }
    }

    // ===== MÉTODO DE VALIDAÇÃO COMPLETO =====
    private String validarDados(String nome, String email, String senha, String confirmSenha,
                                String telefone, String cpf, String cep, String setor,
                                String tipo, String cargo, String terms) {

        // Validação de campos obrigatórios
        if (nome == null || nome.trim().isEmpty()) {
            return "Nome completo é obrigatório.";
        }

        if (email == null || email.trim().isEmpty() || !validarEmail(email)) {
            return "Email inválido.";
        }

        // Validação de senha
        if (senha == null || senha.length() < 8) {
            return "Senha deve ter no mínimo 8 caracteres.";
        }

        if (!senha.equals(confirmSenha)) {
            return "As senhas não coincidem.";
        }

        // Validação de telefone
        String telefoneDigits = limparNumeros(telefone);
        if (telefoneDigits.length() != 11) {
            return "Telefone inválido! Digite um número completo com DDD + 9 dígitos. Ex: (11) 98765-4321";
        }

        // Validação de CPF
        String cpfDigits = limparNumeros(cpf);
        if (cpfDigits.length() != 11) {
            return "CPF inválido! Digite um CPF completo com 11 dígitos.";
        }

        if (!validarCPF(cpfDigits)) {
            return "CPF inválido! Verifique os dígitos.";
        }

        // Validação de CEP
        String cepDigits = limparNumeros(cep);
        if (cepDigits.length() != 8) {
            return "CEP inválido! Digite um CEP completo com 8 dígitos.";
        }

        // Validação de setor
        if (setor == null || setor.trim().isEmpty()) {
            return "Setor/Departamento é obrigatório.";
        }

        // Validação de tipo
        if (tipo == null || (!tipo.equals("F") && !tipo.equals("G") && !tipo.equals("S"))) {
            return "Classificação inválida.";
        }

        // Validação de cargo
        if (cargo == null || cargo.trim().isEmpty()) {
            return "Cargo é obrigatório.";
        }

        // Validação de termos
        if (terms == null) {
            return "Você deve concordar com os Termos e Condições.";
        }

        return null; // Sem erros
    }

    // ===== VALIDAÇÃO DE EMAIL =====
    private boolean validarEmail(String email) {
        if (email == null) return false;
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(regex);
    }

    // ===== VALIDAÇÃO DE CPF (com dígitos verificadores) =====
    private boolean validarCPF(String cpf) {
        if (cpf == null || cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int[] digits = new int[11];
            for (int i = 0; i < 11; i++) {
                digits[i] = Character.getNumericValue(cpf.charAt(i));
            }

            // Cálculo do primeiro dígito verificador
            int sum = 0;
            for (int i = 0; i < 9; i++) {
                sum += digits[i] * (10 - i);
            }
            int firstDigit = 11 - (sum % 11);
            if (firstDigit >= 10) firstDigit = 0;

            // Cálculo do segundo dígito verificador
            sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += digits[i] * (11 - i);
            }
            int secondDigit = 11 - (sum % 11);
            if (secondDigit >= 10) secondDigit = 0;

            return digits[9] == firstDigit && digits[10] == secondDigit;

        } catch (Exception e) {
            return false;
        }
    }

    // ===== MÉTODO PARA LIMPAR NÚMEROS =====
    private String limparNumeros(String input) {
        if (input == null) return "";
        return input.replaceAll("\\D", "");
    }

    // ===== MÉTODOS DE FORMATAÇÃO (úteis para outras partes do sistema) =====
    public static String formatarCPF(String cpf) {
        String digits = limparNumerosStatic(cpf);
        if (digits.length() != 11) return cpf;
        return digits.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    public static String formatarTelefone(String telefone) {
        String digits = limparNumerosStatic(telefone);
        if (digits.length() != 11) return telefone;
        return digits.replaceAll("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
    }

    public static String formatarCEP(String cep) {
        String digits = limparNumerosStatic(cep);
        if (digits.length() != 8) return cep;
        return digits.replaceAll("(\\d{5})(\\d{3})", "$1-$2");
    }

    private static String limparNumerosStatic(String input) {
        if (input == null) return "";
        return input.replaceAll("\\D", "");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Se alguém acessar via GET, redireciona para o formulário
        response.sendRedirect("criarConta.jsp");
    }
}
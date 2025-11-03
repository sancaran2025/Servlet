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

    private UsuarioDAO usuarioDAO = new UsuarioDAO(); // Instancia o DAO para manipular usuários

    // Método chamado quando o formulário envia POST
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Pega todos os parâmetros do formulário
        String nome = request.getParameter("fullname");
        String email = request.getParameter("email");
        String senha = request.getParameter("password");
        String confirmSenha = request.getParameter("confirm-password");
        String telefone = request.getParameter("phone");
        String cpf = request.getParameter("cpf");
        String cep = request.getParameter("cep");
        String setor = request.getParameter("sector");
        String tipo = request.getParameter("tipo");
        String cargo = request.getParameter("cargo");
        String terms = request.getParameter("terms");

        // Valida os dados do formulário
        String errorMessage = validarDados(nome, email, senha, confirmSenha, telefone, cpf, cep, setor, tipo, cargo, terms);
        if (errorMessage != null) { // Se tiver erro, volta para a página com mensagem
            request.setAttribute("error", errorMessage);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/criarConta.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Verifica se já existe usuário com CPF ou email
        if (usuarioDAO.usuarioExiste(cpf, email)) {
            request.setAttribute("error", "CPF ou Email já cadastrado");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/criarConta.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Cria objeto Usuario com os dados
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setCpf(limparNumeros(cpf));
        usuario.setCep(limparNumeros(cep));
        usuario.setTipo(tipo.charAt(0));
        usuario.setEmail(email);
        usuario.setCargo(cargo);
        usuario.setSetor(setor);
        usuario.setSenha(senha);

        // Insere usuário e telefone
        if (usuarioDAO.inserirUsuarioComTelefone(usuario, limparNumeros(telefone))) {
            response.sendRedirect("entrarConta.jsp?success=" + URLEncoder.encode("Conta criada com sucesso! Faça login.", StandardCharsets.UTF_8));
        } else { // Se erro, volta para criarConta.jsp
            request.setAttribute("error", "Erro ao criar conta. Tente novamente.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/criarConta.jsp");
            dispatcher.forward(request, response);
        }
    }

    // Validação dos campos do formulário
    private String validarDados(String nome, String email, String senha, String confirmSenha,
                                String telefone, String cpf, String cep, String setor,
                                String tipo, String cargo, String terms) {

        if (nome == null || nome.trim().isEmpty()) return "Nome completo é obrigatório.";
        if (email == null || email.trim().isEmpty() || !validarEmail(email)) return "Email inválido.";
        if (senha == null || senha.length() < 8) return "Senha deve ter no mínimo 8 caracteres.";
        if (!senha.equals(confirmSenha)) return "As senhas não coincidem.";

        String telefoneDigits = limparNumeros(telefone);
        if (telefoneDigits.length() != 11) return "Telefone inválido! Digite DDD + 9 dígitos.";

        String cpfDigits = limparNumeros(cpf);
        if (cpfDigits.length() != 11) return "CPF inválido! Digite um CPF completo com 11 dígitos.";
        if (!validarCPF(cpfDigits)) return "CPF inválido! Verifique os dígitos.";

        String cepDigits = limparNumeros(cep);
        if (cepDigits.length() != 8) return "CEP inválido! Digite um CEP completo com 8 dígitos.";

        if (setor == null || setor.trim().isEmpty()) return "Setor/Departamento é obrigatório.";
        if (tipo == null || (!tipo.equals("F") && !tipo.equals("G") && !tipo.equals("S"))) return "Classificação inválida.";
        if (cargo == null || cargo.trim().isEmpty()) return "Cargo é obrigatório.";
        if (terms == null) return "Você deve concordar com os Termos e Condições.";

        return null;
    }

    // Valida email usando regex
    private boolean validarEmail(String email) {
        if (email == null) return false;
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(regex);
    }

    // Valida CPF calculando dígitos verificadores
    private boolean validarCPF(String cpf) {
        if (cpf == null || cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) return false;

        try {
            int[] digits = new int[11];
            for (int i = 0; i < 11; i++) digits[i] = Character.getNumericValue(cpf.charAt(i));

            int sum = 0;
            for (int i = 0; i < 9; i++) sum += digits[i] * (10 - i);
            int firstDigit = 11 - (sum % 11);
            if (firstDigit >= 10) firstDigit = 0;

            sum = 0;
            for (int i = 0; i < 10; i++) sum += digits[i] * (11 - i);
            int secondDigit = 11 - (sum % 11);
            if (secondDigit >= 10) secondDigit = 0;

            return digits[9] == firstDigit && digits[10] == secondDigit;
        } catch (Exception e) {
            return false;
        }
    }

    // Remove tudo que não é número
    private String limparNumeros(String input) {
        if (input == null) return "";
        return input.replaceAll("\\D", "");
    }

    // Método GET apenas redireciona para a página de criar conta
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/criarConta.jsp");
        dispatcher.forward(request, response);
    }
}

package com.example.sancaranservlet.model;

public class Usuario {
    private int id;
    private String nome;
    private String cpf;
    private String cep;
    private char tipo; // F, G, S
    private String email;
    private String cargo;
    private String senha;
    private String setor;
    private String telefone; // ← ADICIONE ESTE CAMPO

    // Construtores
    public Usuario() {}

    public Usuario(String nome, String cpf, String cep, char tipo, String email, String cargo, String senha, String setor, String telefone) {
        this.nome = nome;
        this.cpf = cpf;
        this.cep = cep;
        this.tipo = tipo;
        this.email = email;
        this.cargo = cargo;
        this.senha = senha;
        this.setor = setor;
        this.telefone = telefone;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public char getTipo() { return tipo; }
    public void setTipo(char tipo) { this.tipo = tipo; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getSetor() { return setor; }
    public void setSetor(String setor) { this.setor = setor; }

    // ← ADICIONE ESTES DOIS MÉTODOS
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    // toString para debug
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", cep='" + cep + '\'' +
                ", tipo=" + tipo +
                ", email='" + email + '\'' +
                ", cargo='" + cargo + '\'' +
                ", setor='" + setor + '\'' +
                ", telefone='" + telefone + '\'' +
                '}';
    }
}
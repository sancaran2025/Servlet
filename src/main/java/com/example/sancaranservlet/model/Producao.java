package com.example.sancaranservlet.model;

public class Producao {

    private int id;
    private String dtRegistro;
    private int qntProduzida;
    private int idSetor;
    private String nomeSetor;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDtRegistro() {
        return dtRegistro;
    }

    public void setDtRegistro(String dtRegistro) {
        this.dtRegistro = dtRegistro;
    }

    public int getQntProduzida() {
        return qntProduzida;
    }

    public void setQntProduzida(int qntProduzida) {
        this.qntProduzida = qntProduzida;
    }

    public int getIdSetor() {
        return idSetor;
    }

    public void setIdSetor(int idSetor) {
        this.idSetor = idSetor;
    }

    public String getNomeSetor() {
        return nomeSetor;
    }

    public void setNomeSetor(String nomeSetor) {
        this.nomeSetor = nomeSetor;
    }
}

package com.maquina.ligador;
public class TabUso {
    String simbolo;
    int endereco;
    String sinal;

    public TabUso(String simbolo, int endereco, String sinal){
        this.endereco = endereco;
        this.simbolo = simbolo;
        this.sinal = sinal;
    }

    @Override
    public String toString() {
        return "Simbolo: " + simbolo + ", Endereço: " + endereco + ", Sinal: " + sinal;
    }

    public int getEndereco() {
        return endereco;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public String getSinal() {
        return sinal;
    }

    public void setEndereco(int endereco) {
        this.endereco = endereco;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public void setSinal(String sinal) {
        this.sinal = sinal;
    }
}

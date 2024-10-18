package com.maquina.ligador;
public class TabDef {
    String simbolo;
    int endereco;
    String realoc;

    public TabDef(String simbolo, int endereco, String realoc){
        this.endereco = endereco;
        this.simbolo = simbolo;
        this.realoc = realoc;
    }

    @Override
    public String toString() {
        return "Simbolo: " + simbolo + ", Endereço: " + endereco + ", Realoc: " + realoc;
    }

    public void setEndereco(int endereco) {
        this.endereco = endereco;
    }

    public int getEndereco() {
        return endereco;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setRealoc(String realoc) {
        this.realoc = realoc;
    }

    public String getRealoc() {
        return realoc;
    }

}

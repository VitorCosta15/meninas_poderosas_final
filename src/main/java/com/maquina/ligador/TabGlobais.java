package com.maquina.ligador;
public class TabGlobais {
    String simbolo;
    int endereco; 
    String modoReloc;

    public TabGlobais(String simbolo, int endereco, String modoReloc){
        this.simbolo = simbolo;
        this.endereco = endereco;
        this.modoReloc = modoReloc;
    }

    @Override
    public String toString() {
        return "Simbolo: " + simbolo + ", Endereço: " + endereco + ", Realoc: " + modoReloc;
    }

    public int getEndereco() {
        return endereco;
    }

    public String getModoReloc() {
        return modoReloc;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setEndereco(int endereco) {
        this.endereco = endereco;
    }

    public void setModoReloc(String modoReloc) {
        this.modoReloc = modoReloc;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public void printLinha(){
        System.out.println("Simbolo: "+simbolo+" Endereço: "+endereco+" Modo realocação: "+modoReloc);
    }
    
}

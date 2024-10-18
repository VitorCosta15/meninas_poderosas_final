package com.maquina.ligador;
import java.util.LinkedList;

public class Modulo {
    LinkedList<TabDef> linhasTabelaDef;
    LinkedList<TabUso> linhasTabelaUso;
    int tamModulo;

    public Modulo() {
        linhasTabelaDef = new LinkedList<>();
        linhasTabelaUso = new LinkedList<>();
        this.tamModulo = 0;
    }

    public int getTamModulo() {
        return tamModulo;
    }

    public void setTamModulo(int tamModulo) {
        this.tamModulo = tamModulo;
    }

    public void tamModAdd(){
        this.tamModulo = this.tamModulo + 1;
    }

    public void addTabelaDef(TabDef linhaTabela){
        this.linhasTabelaDef.add(linhaTabela);
    }//adiciona uma nova linha a linkedlist da tabela de definição 

    public void addTabelaUso(TabUso linhaTabela){
        this.linhasTabelaUso.add(linhaTabela);
    } //adicina uma nova linha a linkedlist da tabela de uso

    public void printTabDef(){
        if(!linhasTabelaDef.isEmpty()){
            for (TabDef linha : linhasTabelaDef) {
                System.out.println(linha);
            }
        }else{
            System.out.println("O módulo não possui tabela de definições!");
        }
        
    }

    public void printTabUso(){
        if(!linhasTabelaUso.isEmpty()){
            for (TabUso linha : linhasTabelaUso) {
                System.out.println(linha);
            }
        }else{
            System.out.println("O módulo não possui tabela de definições!");
        }
    }
}

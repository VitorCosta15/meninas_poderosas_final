package com.maquina.instrucoes;

import com.application.globals;
import com.maquina.Memoria;
import com.maquina.registradores.ACC;
import com.maquina.registradores.PC;

public class ADD{
    
    // Construtor sem parâmetros
    // public ADD() {
    //     super(2, 2, 1); // Passando valores iniciais, ajustando conforme necessário
    // }

    // // Método get para opADD
    // public int getOpADD() {
    //     return this.operandoADD;
    // }

    // // Método set para opADD
    // public void setOperandoADD(int opADD) {
    //     this.operandoADD = opADD;
    // }

    // Método executar para a instrução ADD
    public static void execute() {
        int valorAcumulador = ACC.getACC();
        ACC.setACC(Memoria.getDataFromMemory(PC.getPC()+1) + valorAcumulador); // Define o novo valor do acumulador
        PC.setPC(PC.getPC()+2);//A proóxima instrução estará a 2 endereços da instrução atual ADD.
    }
}


//Memoria.getDataFromMemory(PC.getPC()+1) será o novo operando 1
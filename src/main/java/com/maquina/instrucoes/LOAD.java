package com.maquina.instrucoes;

import com.application.globals;
import com.maquina.Memoria;
import com.maquina.registradores.ACC;
import com.maquina.registradores.PC;

public class LOAD {

    // Construtor sem parâmetros
    // public LOAD() {
    //     super(3, 2, 1); // Passando valores iniciais, ajustando conforme necessário
    // }


    // Método executar para a instrução LOAD
    public static void execute() {        
        ACC.setACC(Memoria.getDataFromMemory(PC.getPC()+1));
        PC.setPC(PC.getPC()+2);
    }
}
package com.maquina.instrucoes;

import com.maquina.Memoria;
import com.maquina.registradores.ACC;
import com.maquina.registradores.PC;

public class SUB {

    // Método executar para a instrução SUB
    public static void execute() {
        int op1 = Memoria.getDataFromMemory(PC.getPC()+1);
        int acc = ACC.getACC();
        ACC.setACC((acc - op1));
        PC.setPC(PC.getPC()+2);
    }
}
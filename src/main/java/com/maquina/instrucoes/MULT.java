package com.maquina.instrucoes;

import com.application.globals;
import com.maquina.Memoria;
import com.maquina.registradores.ACC;
import com.maquina.registradores.PC;

public class MULT {

    // Método executar para a instrução MULT
    public static void execute() {
        int acc = ACC.getACC();
        ACC.setACC((acc * Memoria.getDataFromMemory(PC.getPC()+1)));
        PC.setPC(PC.getPC()+2);
    }
}
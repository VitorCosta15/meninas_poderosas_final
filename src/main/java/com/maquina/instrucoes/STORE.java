package com.maquina.instrucoes;

import com.maquina.Memoria;
import com.maquina.registradores.ACC;
import com.maquina.registradores.PC;

public class STORE {

    public static void execute() {
        int acc = ACC.getACC(); // Obtém o valor do acumulador
        Memoria.setDataOnMemory(PC.getPC() + 1, acc, false);
        PC.setPC(PC.getPC() + 2);
    }
}
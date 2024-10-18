package com.maquina.instrucoes;


import com.maquina.Memoria;
import com.maquina.registradores.ACC;
import com.maquina.registradores.PC;

public class BRZERO {

    // Método executar para a instrução BRZERO
    public static void execute() {
        int acc = ACC.getACC();
        if (acc == 0) {
            PC.setPC(Memoria.getDataFromMemory(PC.getPC()+1));
        } else PC.setPC(PC.getPC()+2);
    }
}
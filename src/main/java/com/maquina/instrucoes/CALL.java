package com.maquina.instrucoes;

import com.application.globals;
import com.maquina.Memoria;
import com.maquina.registradores.PC;
import com.maquina.registradores.SP;

public class CALL{

    // Método executar para a instrução MULT
    public static void execute() {
        SP.setSP(PC.getPC());
        PC.setPC(Memoria.getDataFromMemory(PC.getPC()+1));
    }
}
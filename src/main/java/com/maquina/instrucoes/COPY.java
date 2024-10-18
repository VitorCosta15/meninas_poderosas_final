package com.maquina.instrucoes;

import com.maquina.Memoria;
import com.maquina.registradores.PC;

public class COPY {
    
    public static void execute(){
        Memoria.setDataOnMemory(PC.getPC()+1, Memoria.getDataFromMemory(PC.getPC()+2), false);
        PC.setPC(PC.getPC()+3);
    }
}

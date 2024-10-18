package com.maquina.instrucoes;


import com.maquina.Memoria;
import com.maquina.registradores.PC;

public class BR {
    


    public static void execute() {
        PC.setPC(Memoria.getDataFromMemory(PC.getPC()+1));
    }
}

package com.maquina.instrucoes;

import com.application.globals;
import com.maquina.Memoria;
import com.maquina.registradores.PC;

public class WRITE {
    public static void execute(){
        String output = String.valueOf(Memoria.getDataFromMemory(PC.getPC()+1));
        globals.out = output;
    }
}

package com.maquina.instrucoes;

import com.maquina.registradores.PC;
import com.maquina.registradores.SP;

public class RET {

    // Método executar para a instrução MULT
    public static void execute() {
        PC.setPC(SP.getSP());
    }
}
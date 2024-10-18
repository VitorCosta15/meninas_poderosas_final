package com.maquina.instrucoes;

import com.application.globals;
import com.maquina.Memoria;
import com.maquina.registradores.ACC;
import com.maquina.registradores.PC;

public class BRPOS{
    // Método executar para a instrução BRPOS
    public static void execute() {
        int acc = ACC.getACC();
        if (acc > 0) {
            PC.setPC(Memoria.getDataFromMemory(PC.getPC()+1));
        } else PC.setPC(PC.getPC()+2); //CASO ELE FALHE NO BREAK, TEMOS QUE INCREMENTAR PC PARA A PRÓXIMA INSTRUçÂO.
    }
}
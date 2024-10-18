package com.maquina.instrucoes;
import com.maquina.Memoria;
import com.maquina.front.MachineController;
import com.maquina.registradores.PC;



public class READ{
    public static void execute(){        
        Memoria.setDataOnMemory(PC.getPC()+1, Integer.valueOf(MachineController.valorAtualTextField), false);
        PC.setPC(PC.getPC()+2);
    } 

    // public static void execute2(String inputDoTextFieldString){
    //     globals.operando2 = Integer.valueOf(inputDoTextFieldString);
    // } 
}

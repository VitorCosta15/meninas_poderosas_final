package com.maquina.mid_end;

import com.maquina.instrucoes.ADD;
import com.maquina.instrucoes.BR;
import com.maquina.instrucoes.BRNEG;
import com.maquina.instrucoes.BRPOS;
import com.maquina.instrucoes.BRZERO;
import com.maquina.instrucoes.CALL;
import com.maquina.instrucoes.COPY;
import com.maquina.instrucoes.DIVIDE;
import com.maquina.instrucoes.LOAD;
import com.maquina.instrucoes.MULT;
import com.maquina.instrucoes.READ;
import com.maquina.instrucoes.RET;
import com.maquina.instrucoes.STOP;
import com.maquina.instrucoes.STORE;
import com.maquina.instrucoes.SUB;
import com.maquina.instrucoes.WRITE;

public class MemoryDisplay {
    private String enderecoInstrucao;
    private String mnemonico;
    private boolean isInstrucao;
    // public StringProperty enderecoInstrucaoProperty;
    // public StringProperty mnemonicoProperty;


    public MemoryDisplay(String enderecoInstrucao, String mnemonico, boolean instrucao){
        this.enderecoInstrucao = enderecoInstrucao;
        this.mnemonico = mnemonico;
        this.isInstrucao = instrucao;
        // this.enderecoInstrucaoProperty = new SimpleStringProperty(this.enderecoInstrucao);
        // this.mnemonicoProperty = new SimpleStringProperty(this.mnemonico);
    }


    public String getEnderecoInstrucao(){
        return enderecoInstrucao;
    }

    public String getMnemonico(){
        return mnemonico;
    }

    public boolean getIsInstrucao(){
        return isInstrucao;
    }


    public static MemoryDisplay opcodeToMnemonic(int opcode, int i, boolean isInstrucao){
        if(isInstrucao){
        switch (opcode) {
            case 2:
                return new MemoryDisplay(String.valueOf(i), "ADD", true);
                

            case 0:
                return new MemoryDisplay(String.valueOf(i), "BR", true);
                
            
            case 5:
                return new MemoryDisplay(String.valueOf(i), "BRNEG", true);
                

            case 1:
                return new MemoryDisplay(String.valueOf(i), "BRPOS", true);
                
            
            case 4:
                return new MemoryDisplay(String.valueOf(i), "BRZERO", true);
            
            case 15:
                return new MemoryDisplay(String.valueOf(i), "CALL", true);

            case 13:
                return new MemoryDisplay(String.valueOf(i), "COPY", true);

            case 10:
                return new MemoryDisplay(String.valueOf(i), "DIVIDE",true);


            case 3:
                return new MemoryDisplay(String.valueOf(i), "LOAD", true);

            case 14:
                return new MemoryDisplay(String.valueOf(i), "MULT", true);

            case 12:
                return new MemoryDisplay(String.valueOf(i), "READ", true);

            case 16:
                return new MemoryDisplay(String.valueOf(i), "RET", true);

            case 11:
                return new MemoryDisplay(String.valueOf(i), "STOP", true);
            
            case 7:
                return new MemoryDisplay(String.valueOf(i), "STORE", true);

            case 6:
                return new MemoryDisplay(String.valueOf(i), "SUB", true);
            
            case 8:
                return new MemoryDisplay(String.valueOf(i), "WRITE", true);


            default:
                return new MemoryDisplay(String.valueOf(i), "ERROR", true);                
        }
    }else{
        return new MemoryDisplay(String.valueOf(i), String.valueOf(opcode), false);
    }
    }

    public static void mnemonicToExecution(String Mnemonic){
        switch (Mnemonic) {
            case "ADD":
                ADD.execute(); //Já pega direto oq está salvo como operando 1.
                break;
            case "BR":
                BR.execute();
                break;
            case "BRNEG":
                BRNEG.execute();
                break;
            case "BRPOS":
                BRPOS.execute();
                break;
            case "BRZERO":
                BRZERO.execute();
                break;
            case "CALL":
                CALL.execute();
                break;
            case "COPY":
                COPY.execute();
                break;
            case "DIVIDE":
                DIVIDE.execute();
                break;
            case "LOAD":
                LOAD.execute();
                break;
            case "MULT":
                MULT.execute();
                break;
            case "READ":
                READ.execute();
                break;
            case "RET":
                RET.execute();
                break;
            case "STOP":
                STOP.execute();
                break;
            case "STORE":
                STORE.execute();
                break;
            case "SUB":
                SUB.execute();
                break;
            case "WRITE":
                WRITE.execute();
                break;
            default:
                System.out.println("ERROR");
                break;
        }
    }
}

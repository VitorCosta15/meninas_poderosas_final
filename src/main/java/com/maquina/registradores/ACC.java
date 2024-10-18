package com.maquina.registradores;

//classe ACC -> Accumulator
public class ACC {
    private static int valorInterno;
    private static int tamanho;

    static {
        tamanho = 16;
        valorInterno = 0;
    }


    //construtor
    private ACC(){
        throw new UnsupportedOperationException("Não é possível instanciar esta classe.");
    }

    public static void setACC(int valor){
        valorInterno = valor;
    }

    public static int getACC(){
        return valorInterno;
    }

}


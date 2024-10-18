package com.maquina.registradores;

//classe MOP -> Operation Mode
public class MOP {
    private static int valorInterno;
    private static int tamanho;

    static {
        tamanho = 8;
        valorInterno = 0;
    }


    //construtor
    private MOP(){
        throw new UnsupportedOperationException("Não é possível instanciar esta classe.");
    }

    public static void setMOP(int valor){
        valorInterno = valor;
    }

    public static int getMOP(){
        return valorInterno;
    }

}

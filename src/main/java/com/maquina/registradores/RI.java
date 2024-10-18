package com.maquina.registradores;

//classe RI -> Registrador de Instruções
public class RI {
    private static int valorInterno;
    private static int tamanho;

    static {
        tamanho = 16;
        valorInterno = 0;
    }

    //construtor
    private RI(){
        throw new UnsupportedOperationException("Não é possível instanciar esta classe.");
    }

    public static void setRI(int valor){
        valorInterno = valor;
    }

    public static int getRI(){
        return valorInterno;
    }

}

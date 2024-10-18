package com.maquina.registradores;

//classe RE -> Registrador de Endereçamento de Memória
public class RE {
    private static int valorInterno;
    private static int tamanho;

    static {
        tamanho = 16;
        valorInterno = 0;
    }


    //construtor
    private RE(){
        throw new UnsupportedOperationException("Não é possível instanciar esta classe.");
    }

    public static void setRE(int valor){
        valorInterno = valor;
    }

    public static int getRE(){
        return valorInterno;
    }

}

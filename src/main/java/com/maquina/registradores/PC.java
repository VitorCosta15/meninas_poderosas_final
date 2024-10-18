package com.maquina.registradores;

//classe PC -> Program Counter
public class PC {
    private static int valorInterno;
    private static int tamanho;

    static {
        tamanho = 16;
        valorInterno = 0;
    }


    //construtor
    private PC(){
        throw new UnsupportedOperationException("Não é possível instanciar esta classe.");
    }

    //PC + 4
    public static void executePC(){
        valorInterno += 4;
    }
    
    //atualiza o valor de PC para o valor inserido
    public static void setPC(int valor){
        valorInterno = valor;
    }

    //retorna o valor de PC
    public static int getPC(){
        return valorInterno;
    }

}

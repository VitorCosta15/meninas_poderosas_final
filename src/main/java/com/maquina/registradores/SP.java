package com.maquina.registradores;
import java.util.Stack;

//classe SP -> Stack Pointer
public class SP {
    private static Stack<Integer> pilha;
    private static int valorInterno;
    private static int LIMITE = 16384; // 2^16/4 não sei se está certo

    //membro de inicialização estática, ocorre apenas uma vez 
    static {
        pilha = new Stack<>();
        pilha.push(0);
        valorInterno = 2;
    }

    //construtor
    private SP(){
        throw new UnsupportedOperationException("Não é possível instanciar esta classe.");
    }

    //push na pilha
    public static void setSP(int valor){
        if (valorInterno < LIMITE){
            pilha.push(valor);
            valorInterno += 1;
        }
        return; //tentativa inválida de empilhar, não faz nada
    }

    //pop da pilha
    public static int getSP(){
        if(valorInterno > 2) {
            valorInterno -= 1;
            return pilha.pop();
        }
        return -1; //tentativa inválida de retirar da pilha
    }

}


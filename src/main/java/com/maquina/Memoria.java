package com.maquina;




public class Memoria {
   private static Integer[] memoria = new Integer[1000]; //Se cada palavra da memória deve ter 16 Bits, 2 Bytes, com 1000 posições temos 2KB de memória
   private static boolean[] isInstrucao = new boolean[1000];
   
   public static void inicializarMemoria(){
     for (int i=0; i < 1000; i++){
          memoria[i] = 0;
          isInstrucao[i] = false;
     }
   }

   public static Integer[] getMemoria(){
     return memoria;
   }

   public static int getDataFromMemory(int endereco){
        return memoria[endereco];
   }

   public static boolean getIsInstrucao(int endereco){
    return isInstrucao[endereco];
   }

   public static void setDataOnMemory(int endereco, int dado, boolean instrucao){
        memoria[endereco] = dado;
        isInstrucao[endereco] = instrucao;
   }

   public static void teste(String oi){
     System.out.println(oi);
   }

}

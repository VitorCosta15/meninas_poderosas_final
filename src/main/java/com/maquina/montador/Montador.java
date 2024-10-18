package com.maquina.montador;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.NumberFormatException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

//classe estatica do Montador
public class Montador {

    private static int adressCounter1; // contador de endereços 
    private static int adressCounter2; // contador de endereços 
    private static File ASM1; // arquivo assembly1
    private static File ASM2; // arquivo assembly2
    private static File InstructionsTable; // arquivo para orientar as passagens
    private static Map<String, String> mapTabelaRotulos1 = new HashMap<String, String>();
    private static Map<String, String> mapTabelaRotulos2 = new HashMap<String, String>();
    private static Map<String, Integer> contagemLabels = new HashMap<String, Integer>(); //Mapa auxiliar para contar quantas vezes cada operando foi usado (para erro7)
    private static Map<String, Integer> hashDefiniMap1 = new HashMap<String, Integer>();
    private static Map<String, Integer> hashDefiniMap2 = new HashMap<String, Integer>();
    private static Map<String, Integer> hashHasUsedMap = new HashMap<String, Integer>();
    private static ArrayList<Integer> linhalst = new ArrayList<Integer>();   // array de inteiros de linhas do lst

    static void initializeFiles() {
        ASM1 = new File("src/main/java/com/maquina/montador/entradaMontador1.asm");
        ASM2 = new File("src/main/java/com/maquina/montador/entradaMontador2.asm");
        InstructionsTable = new File("src/main/java/com/maquina/montador/InstructionsTable.txt");
    }

    // construtor que proibe instâncias de montadores
    private Montador() {
        throw new UnsupportedOperationException("Não é possível instanciar esta classe.");
    }

    // Funções para ver os
    // erros----------------------------------------------------------------------------------------------------------------
        // Verfica se as letras são maiusculas e se tiver null da certo 
        // erro N 1
    public static boolean saoLetrasMaiusculas(String input) {
        //boolean teste;
        if (input.equals(" ")) return true;

        if (isStringNumber(input)) return true; //se a string for inteira de números

        //EAR9110 --> pode começar com letra, apenas maiúscula, e ter números depois
        char[] caractere = input.toCharArray();
        //primeiro caractere garantido maiúsculo
        if (Character.isUpperCase(caractere[0])){ 
            //se não for maiúsculo, nem letra, nem dígito resulta em falso
            for (char c : input.toCharArray()) if(!Character.isUpperCase(c) && !Character.isLetterOrDigit(c)) return false; 
        } else return false;
        return true;
    }

    public static boolean isStringNumber(String input) {
        for (char c : input.toCharArray()) if(!Character.isDigit(c)) return false;
        return true;
    }

    public static boolean overflow(String input) {
        for (char c : input.toCharArray()) if(!Character.isDigit(c)) return true; //ou seja, nao é overflow se nao for digito
        try {
            if (Integer.valueOf(input) < -2147483648 || Integer.valueOf(input) > 2147483647){
                return false;
            }
        } catch (NumberFormatException e){
            System.out.println("Ocorreu um erro de conversão (inteiro muito grande).");
            return false;
        }
        return true;
    }


    // Verificar se e Alfanumérico erro N 1
    public static boolean saoAlfanumericos(String input) {
        if(input.equals(" ")) return true;

        // Retorna false se encontrar um caractere que nao seja alfanumérico
        for (char c : input.toCharArray()) if (!Character.isLetterOrDigit(c)) return false; 

        return true; // Caso todos forem alfanuméricos
    }

    // Verficar o tamanho da Linha do erro N 2
    public static boolean errotamanho(String linhaAtual) {
        if (linhaAtual == null) return false; // Se a linha for nula, retorna falso
        if (linhaAtual.length() > 80) return false;
        return true;
    }
    // Função para detectar presença de operadores da Linha do erro N 6
    public static int reconhecedordeoperadores(String operadorerro) {
        switch (operadorerro.toUpperCase()) {
            case "ADD":
            case "BR":
            case "BRNEG":
            case "BRPOS":
            case "BRZERO":
            case "CALL":
            case "DIVIDE":
            case "LOAD":
            case "MULT":
            case "READ":
            case "STORE":
            case "SUB":
            case "WRITE":
                return 1; 

            case "COPY":
                return 2; 

            case "RET":
            case "STOP":
                return 0; 

            case "CONST":
                return 1; 

            case "END":
            case "EXTDEF":
            case "EXTR":
            case "SPACE":
            case "STACK":
            case "START":
                return 0;

            default:
                return 1; 
        }
    }

    public static boolean checadoroperadores(String operacao, String Operador1, String Operador2) {
        int numero = reconhecedordeoperadores(operacao);
        int contagem = 0;
        if (!Operador1.equals(" ")) {
            contagem += 1;
        }
        if (!Operador2.equals(" ")) {
            contagem += 1;
        }
        if (numero != contagem) {
            return false;
        }

        return true;

    }

    // Alfanumerico ou caracteres > 8 da Linha do erro N 6
    public static boolean testelabel(String Label) {
        boolean erro1 = saoAlfanumericos(Label);
        if (erro1 == false) {
            return false;
        }
        if (Label.length() >= 9) {
            return false;
        }
        return true;
    }

    // Mnemônico Válido da Linha do erro N 9
    public static boolean isMnemonicoValido(String operador) {
        switch (operador.toUpperCase()) {
            case "ADD":
            case "BR":
            case "BRNEG":
            case "BRPOS":
            case "BRZERO":
            case "CALL":
            case "COPY":
            case "DIVIDE":
            case "LOAD":
            case "MULT":
            case "READ":
            case "RET":
            case "STOP":
            case "STORE":
            case "SUB":
            case "WRITE":
            case "CONST":
            case "END":
            case "EXTDEF":
            case "EXTR":
            case "SPACE":
            case "STACK":
            case "START":
                return true; // Mnemonico válido

            default:
                return false; // Mnemonico não reconhecido
        }
    }
    //Fim das análises de erro
    // --------------------------------------------
    public static void gerarLST(ArrayList<String> errosdolst, ArrayList<Integer> linhalst) {
        String nomeArquivo = "src/main/java/com/maquina/montador/Arquivo.lst"; 
        String erro; // Variavel auxiliar para a escrita do(s) erro(s) no arquivo .lst
        int linha; // Variavel auxiliar para verificar a linha que ocorreu o erro

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo))) {
            writer.write("----------------------------------\n");
            writer.write("Arquivo .LST\n");
            writer.write("----------------------------------\n");

            // Verifica se há erros nas listas ou se elas estão vazias
            if (errosdolst != null && !errosdolst.isEmpty() && linhalst != null && !linhalst.isEmpty()) {
                for (int i = 0; i < linhalst.size(); i++) {
                    if(!errosdolst.get(i).equals("none")){
                        erro = errosdolst.get(i);
                        linha = linhalst.get(i);
                        writer.write("Linha " + linha + ": " + erro + "\n");
                    }
                } // Se não houver erros não terá nada escrito no arquivo
            }
        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao criar o arquivo.");
            e.printStackTrace();
        }
    }

    public static void executeMontador() throws IOException {
        initializeFiles();
        ArrayList<String> errosdolst = new ArrayList<String>();
        int contadorLinha = 1; // Contagem de linhas

        adressCounter1 = 0; // supondo a inicialização de endereços em zero
        adressCounter2 = 0; // supondo a inicialização de endereços em zero
        Scanner reader1 = abrirArquivo(ASM1);
        Scanner reader2 = abrirArquivo(ASM2);
        

        // -------------------------> PRIMEIRA PASSAGEM 1º arquivo asm ----------------------------->

        while (reader1.hasNextLine()) {
            String linhaAssembly = reader1.nextLine();
                //Partes da linha -----------------------------------------------------------------
            String rotulo = "";
            String operador = "";
            String operando1 = "";
            String operando2 = "";
            String comentario = "";
                //Separa informação de comentário
            String[] partesLinha = linhaAssembly.split(";");
            String comando = partesLinha[0].trim();  //Label, Operação, Operandos
            if (partesLinha.length > 1) {
                comentario = partesLinha[1].trim();  //Caso tenha ; atualiza o comentário
            }
                //Separar label, operação e operandos
            String[] partesComando = comando.split("\\s+");  // Divide por espaços em branco
            switch (partesComando.length) {
                case 4:
                    rotulo = partesComando[0];
                    operador = partesComando[1];
                    operando1 = partesComando[2];
                    operando2 = partesComando[3];
                    break;
                case 3:
                    switch (partesComando[0]) {
                        case "COPY":
                        rotulo = " ";
                        operador = partesComando[0];
                        operando1 = partesComando[1];
                        operando2 = partesComando[2].trim();
                        break;

                        default:
                            rotulo = partesComando[0];
                            operador = partesComando[1];
                            operando1 = partesComando[2];
                            operando2 = " ";
                            break;}
                    break;
                case 2:
                    rotulo = " ";
                    operador = partesComando[0];
                    operando1 = partesComando[1];
                    operando2 = " ";
                    break;
                case 1:
                    rotulo = " ";
                    operador = partesComando[0];
                    operando1 = " ";
                    operando2 = " ";
                    break;
            
                default:
                    errosdolst.add("Foi detectada sintaxe de linha incorreta. Falta de itens ou itens demais");
            }  

                // Tipos de Erros =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
                // Erro 1: Caracter invalido
                boolean VerificaRotulo = saoAlfanumericos(rotulo);
                if (!VerificaRotulo){
                    errosdolst.add("Erro alfanumerico no rotulo");
                }
                boolean VerificaOperador = isMnemonicoValido(operador);
                if (!VerificaOperador){
                    errosdolst.add("Operador não encontrado");
                }

                // boolean VerificaOperando1 = true;
                boolean VerificaOperando1 = saoLetrasMaiusculas(operando1);
                if(!VerificaOperando1){
                    errosdolst.add("Erro de sintaxe no operando 1");
                }

                boolean VerificaOverflow = overflow(operando1);
                if (!VerificaOverflow){
                    errosdolst.add("Erro de overflow no operando 1");
                }

                boolean VerificaOperando2 = saoLetrasMaiusculas(operando2);
                if(!VerificaOperando2){
                    errosdolst.add("Erro de sintaxe no operando 2");
                }
                // Erro 2: Linha muito longa
                boolean VerificaTamanho = errotamanho(linhaAssembly);
                if (!VerificaTamanho) {
                    errosdolst.add("Foi detectado um tamanho de instrucao (linha) excessiva");
                }
                boolean erro61 = testelabel(rotulo);
                if (!erro61) {
                    errosdolst.add("Erro de sintaxe no rótulo");
                }
                //Verifica se os operadores estão corretos
                boolean erro62 = checadoroperadores(operador, operando1, operando2);
                if (!erro62) {
                    errosdolst.add("Foi detectado um operador a menos ou a mais");
                }
                // Erro 7: Símbolo redefinido
                boolean VerificaRedefinicao = true;
                // if (operador.equals("CONST")){
                //     if (contagemLabels.get(rotulo) != null){
                //         contagemLabels.put(rotulo, contagemLabels.get(rotulo) + 1);
                //     } else contagemLabels.put(rotulo, 1);
                // }
                // if (!contagemLabels.isEmpty() && contagemLabels.get(rotulo) > 1){
                //     VerificaRedefinicao = false;
                //     errosdolst.add("Simbolo redefinido");
                // }
                
                // Fim dos 10 erros base =-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
                
                if (!rotulo.equals(" ")) {
                    if (operador.equals("CONST") || operador.equals("SPACE") ){ 
                        mapTabelaRotulos1.put(rotulo, operando1);
                        hashDefiniMap1.put(rotulo, 1);
                    }
                    else {
                        mapTabelaRotulos1.put(rotulo, Integer.toString(adressCounter1));
                        hashDefiniMap1.put(rotulo, 0);
                    }
                    
                }
                // não deixa números serem anotados na tabela hash
                if (!operando1.equals(" ") && !mapTabelaRotulos1.containsKey(operando1) && !isInteger(operando1)) { 
                    mapTabelaRotulos1.put(operando1, " ");
                }
                if (!operando2.equals(" ") && !mapTabelaRotulos1.containsKey(operando2)) {
                    mapTabelaRotulos1.put(operando2, " ");
                }
                adressCounter1 += getLength(InstructionsTable, operador);
                

                // // Erro 8: Símbolo não definido
                // boolean VerificaNaoDefinicao = true;
                // if (!reader1.hasNextLine()){
                //     for(Map.Entry<String, String> simbolo : mapTabelaRotulos.entrySet()){
                //         if(simbolo.getValue().equals(" ")){
                //             VerificaNaoDefinicao = false;
                //             errosdolst.add("Simbolo " + simbolo.getKey() + " nao definido");
                //         }
                //     }
                // }

                if (VerificaTamanho 
                    && erro61 
                    && erro62 
                    && VerificaOperador 
                    && VerificaOperando1 
                    && VerificaOperando2 
                    && VerificaOverflow
                    && VerificaRedefinicao) {
                    errosdolst.add("none"); //garante que a lista de erros tenha o mesmo número de linhas (instruções)
                }
                linhalst.add(contadorLinha);
                gerarLST(errosdolst, linhalst);
                contadorLinha++; //Incremento da linha
        }

        reader1.close();

        // -------------------------> SEGUNDA PASSAGEM 1º arquivo asm ----------------------------->

        reader1 = abrirArquivo(ASM1);
        adressCounter1 = 0;

        FileWriter fileWriter1 = new FileWriter("src/main/java/com/maquina/ligador/entradas/mod1.obj");
        PrintWriter printWriter1 = new PrintWriter(fileWriter1);

        while (reader1.hasNextLine()) {
            String linhaAssembly = reader1.nextLine();
            //Partes da linha -----------------------------------------------------------------
            String rotulo = "";
            String operador = "";
            String operando1 = "";
            String operando2 = "";
            String comentario = "";
                //Separa informação de comentário
            String[] partesLinha = linhaAssembly.split(";");
            String comando = partesLinha[0].trim();  //Label, Operação, Operandos
            if (partesLinha.length > 1) {
                comentario = partesLinha[1].trim();  //Caso tenha ; atualiza o comentário
            }
                //Separar label, operação e operandos
            String[] partesComando = comando.split("\\s+");  // Divide por espaços em branco
            switch (partesComando.length) {
                case 4:
                    rotulo = partesComando[0];
                    operador = partesComando[1];
                    operando1 = partesComando[2];
                    operando2 = partesComando[3];
                    break;
                case 3:
                    switch (partesComando[0]) {
                        case "COPY":
                        rotulo = " ";
                        operador = partesComando[0];
                        operando1 = partesComando[1];
                        operando2 = partesComando[2].trim();
                        break;

                        default:
                            rotulo = partesComando[0];
                            operador = partesComando[1];
                            operando1 = partesComando[2];
                            operando2 = " ";
                            break;}
                    break;
                case 2:
                    rotulo = " ";
                    operador = partesComando[0];
                    operando1 = partesComando[1];
                    operando2 = " ";
                    break;
                case 1:
                    rotulo = " ";
                    operador = partesComando[0];
                    operando1 = " ";
                    operando2 = " ";
                    break;
            
                default:
                    throw new IOException("Impossível ler instrucao");
            }

            // [ [<label>] <opcode> [ <operand1> [ <operand2> ]]] [ <comentário> ]
            // [ [<linhaAssemblyQuebrada[0]>] <linhaAssemblyQuebrada[1]>
            // [<linhaAssemblyQuebrada[2]> [<linhaAssemblyQuebrada[3]>]]]
            // [<linhaAssemblyQuebrada[4]>]

            if (!operando1.equals(" ")) { // se tiver op1
                if (!operando2.equals(" ")) { // se tiver op1 e op2
                    printWriter1.println(
                        convertFormatMachineCodeString(Integer.toString(adressCounter1)) + //endereço
                        " " + getLength(InstructionsTable, operador) + //tamanho da operação
                        " " + getMachineCode(InstructionsTable, operador) + //código de máquina da operação
                        " " + mapTabelaRotulos1.get(operando1) + // código de máquina do operando 1
                        " " + mapTabelaRotulos1.get(operando2)); // código de máquina do operando 2
                        // pega os valores associados à chave (label) da tabela hash
                } else //apenas op1
                    if (operador.equals("CONST")) {
                            printWriter1.println(
                            convertFormatMachineCodeString(Integer.toString(adressCounter1)) + //endereço
                            " " + getLength(InstructionsTable, operador) + //tamanho da operação
                            " " + convertFormatMachineCodeString(operando1)); // código da constante operando 1
                    } else //se não for CONST e tiver op1
                        printWriter1.println(
                            convertFormatMachineCodeString(Integer.toString(adressCounter1)) + //endereço
                            " " + getLength(InstructionsTable, operador) + //tamanho da operação
                            " " + getMachineCode(InstructionsTable, operador) + //código de máquina da operação
                            " " + mapTabelaRotulos1.get(operando1)); // código de máquina do operando 1

            } else { // se tiver apenas operação
                if (!operador.equals("SPACE"))
                    printWriter1.println(
                        convertFormatMachineCodeString(Integer.toString(adressCounter1)) + //endereço
                        " " + getLength(InstructionsTable, operador) + //tamanho da operação
                        " " + getMachineCode(InstructionsTable, operador)); //código de máquina da operação
                    else //se a instrução for SPACE, adiciona XX (equivalente a 1 byte separado)
                        printWriter1.println(
                            convertFormatMachineCodeString(Integer.toString(adressCounter1)) + 
                            " " + getLength(InstructionsTable, operador) + 
                            " XX");
            }
            adressCounter1 += getLength(InstructionsTable, operador); //incrementa contador de endereços

        }

        //-----------------------> imprime a tabela de definições ---------------------------------->
        printWriter1.println(";");

        hashDefiniMap1.forEach((key, value) -> {
            // se não tiver sido definido por constante ou reservado por space
            if (value == 0){
                if (key.equals("INTOMUL")) printWriter1.println(key + " " + convertFormatMachineCodeString(mapTabelaRotulos1.get(key)) + " a");
                else printWriter1.println(key + " " + convertFormatMachineCodeString(mapTabelaRotulos1.get(key)) + " r");
            }
        });

        printWriter1.println(";");                      
                
        //-----------------------> imprime a tabela de uso ----------------------------------------->

        printWriter1.println("FIRST 17 +"); //aqui teria que ser feita alguma lógica de location counter linkado com cada símbolo

        // -------------------------> PRIMEIRA PASSAGEM 2º arquivo asm ----------------------------->

        while (reader2.hasNextLine()) {
            String linhaAssembly = reader2.nextLine();
                //Partes da linha -----------------------------------------------------------------
            String rotulo = "";
            String operador = "";
            String operando1 = "";
            String operando2 = "";
            String comentario = "";
                //Separa informação de comentário
            String[] partesLinha = linhaAssembly.split(";");
            String comando = partesLinha[0].trim();  //Label, Operação, Operandos
            if (partesLinha.length > 1) {
                comentario = partesLinha[1].trim();  //Caso tenha ; atualiza o comentário
            }
                //Separar label, operação e operandos
            String[] partesComando = comando.split("\\s+");  // Divide por espaços em branco
            switch (partesComando.length) {
                case 4:
                    rotulo = partesComando[0];
                    operador = partesComando[1];
                    operando1 = partesComando[2];
                    operando2 = partesComando[3];
                    break;
                case 3:
                    switch (partesComando[0]) {
                        case "COPY":
                        rotulo = " ";
                        operador = partesComando[0];
                        operando1 = partesComando[1];
                        operando2 = partesComando[2].trim();
                        break;

                        default:
                            rotulo = partesComando[0];
                            operador = partesComando[1];
                            operando1 = partesComando[2];
                            operando2 = " ";
                            break;}
                    break;
                case 2:
                    rotulo = " ";
                    operador = partesComando[0];
                    operando1 = partesComando[1];
                    operando2 = " ";
                    break;
                case 1:
                    rotulo = " ";
                    operador = partesComando[0];
                    operando1 = " ";
                    operando2 = " ";
                    break;
            
                default:
                    errosdolst.add("Foi detectada sintaxe de linha incorreta. Falta de itens ou itens demais");
            }  

                // Tipos de Erros =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
                // Erro 1: Caracter invalido
                boolean VerificaRotulo = saoAlfanumericos(rotulo);
                if (!VerificaRotulo){
                    errosdolst.add("Erro alfanumerico no rotulo");
                }
                boolean VerificaOperador = isMnemonicoValido(operador);
                if (!VerificaOperador){
                    errosdolst.add("Operador não encontrado");
                }

                // boolean VerificaOperando1 = true;
                boolean VerificaOperando1 = saoLetrasMaiusculas(operando1);
                if(!VerificaOperando1){
                    errosdolst.add("Erro de sintaxe no operando 1");
                }

                boolean VerificaOverflow = overflow(operando1);
                if (!VerificaOverflow){
                    errosdolst.add("Erro de overflow no operando 1");
                }

                boolean VerificaOperando2 = saoLetrasMaiusculas(operando2);
                if(!VerificaOperando2){
                    errosdolst.add("Erro de sintaxe no operando 2");
                }
                // Erro 2: Linha muito longa
                boolean VerificaTamanho = errotamanho(linhaAssembly);
                if (!VerificaTamanho) {
                    errosdolst.add("Foi detectado um tamanho de instrucao (linha) excessiva");
                }
                boolean erro61 = testelabel(rotulo);
                if (!erro61) {
                    errosdolst.add("Erro de sintaxe no rótulo");
                }
                //Verifica se os operadores estão corretos
                boolean erro62 = checadoroperadores(operador, operando1, operando2);
                if (!erro62) {
                    errosdolst.add("Foi detectado um operador a menos ou a mais");
                }
                // Erro 7: Símbolo redefinido
                boolean VerificaRedefinicao = true;
                // if (operador.equals("CONST")){
                //     if (contagemLabels.get(rotulo) != null){
                //         contagemLabels.put(rotulo, contagemLabels.get(rotulo) + 1);
                //     } else contagemLabels.put(rotulo, 1);
                // }
                // if (!contagemLabels.isEmpty() && contagemLabels.get(rotulo) > 1){
                //     VerificaRedefinicao = false;
                //     errosdolst.add("Simbolo redefinido");
                // }
                
                // Fim dos 10 erros base =-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
                
                if (!rotulo.equals(" ")) {
                    if (operador.equals("CONST") || operador.equals("SPACE")){ 
                        mapTabelaRotulos2.put(rotulo, operando1);
                        hashDefiniMap2.put(rotulo, 1);
                    } else {
                        mapTabelaRotulos2.put(rotulo, Integer.toString(adressCounter2));
                        hashDefiniMap2.put(rotulo, 0);
                    }
                }
                // não deixa números serem anotados na tabela hash
                if (!operando1.equals(" ") && !mapTabelaRotulos2.containsKey(operando1) && !isInteger(operando1)) { 
                    mapTabelaRotulos2.put(operando1, " ");
                }
                if (!operando2.equals(" ") && !mapTabelaRotulos2.containsKey(operando2)) {
                    mapTabelaRotulos2.put(operando2, " ");
                }
                adressCounter2 += getLength(InstructionsTable, operador);
                

                // // Erro 8: Símbolo não definido
                // boolean VerificaNaoDefinicao = true;
                // if (!reader2.hasNextLine()){
                //     for(Map.Entry<String, String> simbolo : mapTabelaRotulos.entrySet()){
                //         if(simbolo.getValue().equals(" ")){
                //             VerificaNaoDefinicao = false;
                //             errosdolst.add("Simbolo " + simbolo.getKey() + " nao definido");
                //         }
                //     }
                // }

                if (VerificaTamanho 
                    && erro61 
                    && erro62 
                    && VerificaOperador 
                    && VerificaOperando1 
                    && VerificaOperando2 
                    && VerificaOverflow
                    && VerificaRedefinicao) {
                    errosdolst.add("none"); //garante que a lista de erros tenha o mesmo número de linhas (instruções)
                }
                linhalst.add(contadorLinha);
                gerarLST(errosdolst, linhalst);
                contadorLinha++; //Incremento da linha
        }

        reader2.close();

        // -------------------------> SEGUNDA PASSAGEM 2º arquivo asm ----------------------------->

        reader2 = abrirArquivo(ASM2);
        adressCounter2 = 0;

        FileWriter fileWriter2 = new FileWriter("src/main/java/com/maquina/ligador/entradas/mod2.obj");
        PrintWriter printWriter2 = new PrintWriter(fileWriter2);

        while (reader2.hasNextLine()) {
            String linhaAssembly = reader2.nextLine();
            //Partes da linha -----------------------------------------------------------------
            String rotulo = "";
            String operador = "";
            String operando1 = "";
            String operando2 = "";
            String comentario = "";
                //Separa informação de comentário
            String[] partesLinha = linhaAssembly.split(";");
            String comando = partesLinha[0].trim();  //Label, Operação, Operandos
            if (partesLinha.length > 1) {
                comentario = partesLinha[1].trim();  //Caso tenha ; atualiza o comentário
            }
                //Separar label, operação e operandos
            String[] partesComando = comando.split("\\s+");  // Divide por espaços em branco
            switch (partesComando.length) {
                case 4:
                    rotulo = partesComando[0];
                    operador = partesComando[1];
                    operando1 = partesComando[2];
                    operando2 = partesComando[3];
                    break;
                case 3:
                    switch (partesComando[0]) {
                        case "COPY":
                        rotulo = " ";
                        operador = partesComando[0];
                        operando1 = partesComando[1];
                        operando2 = partesComando[2].trim();
                        break;

                        default:
                            rotulo = partesComando[0];
                            operador = partesComando[1];
                            operando1 = partesComando[2];
                            operando2 = " ";
                            break;}
                    break;
                case 2:
                    rotulo = " ";
                    operador = partesComando[0];
                    operando1 = partesComando[1];
                    operando2 = " ";
                    break;
                case 1:
                    rotulo = " ";
                    operador = partesComando[0];
                    operando1 = " ";
                    operando2 = " ";
                    break;
            
                default:
                    throw new IOException("Impossível ler instrucao");
            }

            // [ [<label>] <opcode> [ <operand1> [ <operand2> ]]] [ <comentário> ]
            // [ [<linhaAssemblyQuebrada[0]>] <linhaAssemblyQuebrada[1]>
            // [<linhaAssemblyQuebrada[2]> [<linhaAssemblyQuebrada[3]>]]]
            // [<linhaAssemblyQuebrada[4]>]

            if (!operando1.equals(" ")) { // se tiver op1
                if (!operando2.equals(" ")) { // se tiver op1 e op2
                    printWriter2.println(
                        convertFormatMachineCodeString(Integer.toString(adressCounter2)) + //endereço
                        " " + getLength(InstructionsTable, operador) + //tamanho da operação
                        " " + getMachineCode(InstructionsTable, operador) + //código de máquina da operação
                        " " + mapTabelaRotulos2.get(operando1) + // código de máquina do operando 1
                        " " + mapTabelaRotulos2.get(operando2)); // código de máquina do operando 2
                        // pega os valores associados à chave (label) da tabela hash
                } else //apenas op1
                    if (operador.equals("CONST")) {
                            printWriter2.println(
                            convertFormatMachineCodeString(Integer.toString(adressCounter2)) + //endereço
                            " " + getLength(InstructionsTable, operador) + //tamanho da operação
                            " " + convertFormatMachineCodeString(operando1)); // código da constante operando 1
                    } else //se não for CONST e tiver op1
                        printWriter2.println(
                            convertFormatMachineCodeString(Integer.toString(adressCounter2)) + //endereço
                            " " + getLength(InstructionsTable, operador) + //tamanho da operação
                            " " + getMachineCode(InstructionsTable, operador) + //código de máquina da operação
                            " " + mapTabelaRotulos2.get(operando1)); // código de máquina do operando 1

            } else { // se tiver apenas operação
                if (!operador.equals("SPACE"))
                    printWriter2.println(
                        convertFormatMachineCodeString(Integer.toString(adressCounter2)) + //endereço
                        " " + getLength(InstructionsTable, operador) + //tamanho da operação
                        " " + getMachineCode(InstructionsTable, operador)); //código de máquina da operação
                    else //se a instrução for SPACE, adiciona XX (equivalente a 1 byte separado)
                        printWriter2.println(
                            convertFormatMachineCodeString(Integer.toString(adressCounter2)) + 
                            " " + getLength(InstructionsTable, operador) + 
                            " XX");
            }
            adressCounter2 += getLength(InstructionsTable, operador); //incrementa contador de endereços

        }

        //-----------------------> imprime a tabela de definições ---------------------------------->
        printWriter2.println(";");

        hashDefiniMap2.forEach((key, value) -> {
            // se não tiver sido definido por constante ou reservado por space
            if (value == 0){
                if (key.equals("INTOMUL")) printWriter2.println(key + " " + convertFormatMachineCodeString(mapTabelaRotulos2.get(key)) + " a");
                else printWriter2.println(key + " " + convertFormatMachineCodeString(mapTabelaRotulos2.get(key)) + " r");
            }
        });

        printWriter2.println(";");                      
                
        //-----------------------> imprime a tabela de uso ----------------------------------------->
        
        reader1.close();
        printWriter1.close();
        reader2.close();
        printWriter2.close();
        
        //-->isso aqui printa toda a tabela hash pra debug
        // mapTabelaRotulos.forEach((key, value) -> System.out.println("Chave: " + key + ", Valor: " + value)); 
        // imprimirErros(linhalst, errosdolst);
    }

    public static Scanner abrirArquivo(File file) throws IOException {
        Scanner reader = new Scanner(file);
        return reader;
    }

    public static int getLength(File file, String alvo) throws IOException {
        Scanner reader = abrirArquivo(file); 
        while (reader.hasNextLine()) { // !EOF
            String linha = reader.nextLine(); // pega a linha inteira
            // System.out.println(linha + "\n");
            String[] campo = linha.split(";"); // separa pelo ponto e virgula
            if (campo[0].equals(alvo)) { // se a instrução buscada estiver no arquivo
                try {
                    reader.close();
                    return Integer.valueOf(campo[2]); // tenta retornar o tamanho como inteiro
                } catch (NumberFormatException e) {
                    System.out.println("Erro ao converter '" + campo[2] + "' para um número.");
                    throw e; // ou retorne um valor padrão ou trate a exceção
                }
            }
        }
        // caso contrario, o arquivo chega no fim sem retornar valor >0
        throw new IOException();
    }

    public static String getMachineCode(File file, String alvo) throws IOException {
        Scanner reader = abrirArquivo(file); 
        while (reader.hasNextLine()) { // !EOF
            String linha = reader.nextLine(); // pega a linha inteira
            String[] campo = linha.split(";"); // separa pelo ponto e virgula
            if (campo[0].equals(alvo)) { // se a instrução buscada estiver no arquivo
                reader.close();
                return campo[1]; // retorna seu codigo de maquina em string
            }
        }
        // caso contrario, o arquivo chega no fim sem retornar valor >0
        throw new IOException();
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str); // Tenta converter a string para um inteiro
            return true; // Se conseguir, retorna true
        } catch (NumberFormatException e) {
            return false; // Se houver erro, retorna false
        }
    }

    public static String convertFormatMachineCodeString(String str) {
        if (str.length() < 2) {
            return "0" + str;
        } else
            return str;
    }   

}
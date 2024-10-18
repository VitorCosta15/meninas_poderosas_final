package com.maquina.ligador;
import java.io.File; // Import the File class
//import java.io.FileWriter;
//import java.io.IOException;
import java.io.FileNotFoundException; // Import this class to handle errors
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner; // Import the Scanner class to read text files

public class Ligador {
    public static void main(String[] args) {
        LinkedList<Modulo> modulos = new LinkedList<Modulo>();
        LinkedList<TabGlobais> tabelaSimbolosGlobais = new LinkedList<TabGlobais>();
        LinkedList<String> commands = new LinkedList<String>();
        int tamPilha = 0;
        File folder = new File("src/main/java/com/maquina/ligador/entradas");
        File[] listOfFiles = folder.listFiles();
        String[] arquivosObj = new String[listOfFiles.length];

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                arquivosObj[i] = listOfFiles[i].getPath();
            }
        }
        String fileName = "src/main/java/com/maquina/carregador/mod1.hpx"; 
        lerModulos(arquivosObj, modulos, tabelaSimbolosGlobais);
        firstPass(modulos,tabelaSimbolosGlobais,commands,arquivosObj);
        secondPass(tabelaSimbolosGlobais,commands,arquivosObj);
        tamPilha = sizeCommands(commands);
        fileCreator(commands, fileName, tabelaSimbolosGlobais, tamPilha); //É pra ta funcionando mas ta dando erro por não ter a lista dos comandos ainda, quando fizer a lista de comandos só tirar o comentario e testar
    }

    public static void lerModulos(String[] arquivosObj, LinkedList<Modulo> modulos, LinkedList<TabGlobais> tabelaSimbolosGlobais) {
        for (String nomeArquivo : arquivosObj) {
            Modulo modulo = new Modulo(); // criando o módulo que armazenara as informações encontradas
            File arquivoAtual = new File(nomeArquivo); // o arquivo atual é o que esta sendo lido do vetor de String
            try {
                Scanner leitor = new Scanner(arquivoAtual);
                if (leitor.hasNextLine()) { // ve se tem uma linha disponível
                    String linha = leitor.nextLine();
                    while (!hasSemicolon(linha) && leitor.hasNextLine()) { // se tiver, percorre o código de máquina até encontrar o ';'
                        linha = leitor.nextLine();
                    }
                    if (leitor.hasNextLine()) { // se possuir uma linha depois do ';', atualiza a linha
                        linha = leitor.nextLine();
                        while (!hasSemicolon(linha) && !linha.isEmpty() && leitor.hasNextLine()) { // verfica se não tem ';'
                            String simbolo = linha.substring(0, FirstSpaceIdentifier(linha));
                            int endereco = Integer.parseInt(
                                    linha.substring(FirstSpaceIdentifier(linha) + 1, SecondSpaceIdentifier(linha)));
                            String realoc = linha.substring(SecondSpaceIdentifier(linha) + 1);
                            TabDef linhaTabDef = new TabDef(simbolo, endereco, realoc); // criando uma linha da tabela e definições
                            modulo.linhasTabelaDef.add(linhaTabDef); // adicionando a linha à tabela de definição dentro do módulo
                            if (leitor.hasNextLine()) {
                                linha = leitor.nextLine();
                            } else {
                                break;
                            }
                        }
                    }
                    if (leitor.hasNextLine()) {
                        linha = leitor.nextLine();
                        while (!linha.isEmpty()) {
                            String simbolo = linha.substring(0, FirstSpaceIdentifier(linha));
                            int endereco = Integer.parseInt(linha.substring(FirstSpaceIdentifier(linha) + 1, SecondSpaceIdentifier(linha)));
                            String sinal = linha.substring(SecondSpaceIdentifier(linha) + 1);
                            TabUso linhaTabUso = new TabUso(simbolo, endereco, sinal);
                            modulo.linhasTabelaUso.add(linhaTabUso);
                            if (leitor.hasNextLine()) {
                                linha = leitor.nextLine();
                            } else {
                                break;
                            }
                        }
                    }
                    
                    modulos.add(modulo);
                }
                leitor.close();
            } catch (FileNotFoundException e) {
                System.out.println("Erro! O arquivo " + nomeArquivo + " não pode ser lido.");
            }
        }
    } // funcionando! função que le os módulos dos arquivos obj e guarda as tabelas
    
    public static void firstPass(LinkedList<Modulo> modulos, LinkedList<TabGlobais> tabelaSimbolosGlobais, LinkedList<String> comandos, String[] arquivosObj) {

        //ajustar os comandos de máquina agora 
        ajustarComandos(arquivosObj, comandos);

        LinkedList<Modulo> copyModulos = new LinkedList<Modulo>(); //criando uma cópia dos módulos para não alterar os módulos
        for (Modulo mod : modulos){
            Modulo copia = mod;
            copyModulos.add(copia);
        }

        Modulo primeiroModulo = copyModulos.peek(); //pega o primeiro módulo, mas não remove, e copia pra tabela de simbolos globais
        if (primeiroModulo != null) { 
            for (TabDef linhaAtual : primeiroModulo.linhasTabelaDef) { //passando por cada linha do primeiro módulo
                String simbolo = linhaAtual.getSimbolo();
                int endereco = linhaAtual.getEndereco();
                String realoc = linhaAtual.getRealoc();
                if(realoc.equals("a")){
                    realoc = "r";
                    adicionarOuAtualizarSimbolo(simbolo, endereco, realoc, tabelaSimbolosGlobais ); //função que adiciona os simbolos a tabela
                }else{
                    adicionarOuAtualizarSimbolo(simbolo, endereco, realoc, tabelaSimbolosGlobais ); //função que adiciona os simbolos a tabela
                }
                
            }
        }
        int ultimoEnd = sizeCommands(comandos);
        ultimoEnd = ultimoEnd - 3; //não sei pq mas deu certo
        
        //while para ajeitar a tabela de simbolos globais
        while (!copyModulos.isEmpty()) {
            Modulo moduloAtual = copyModulos.pop(); //remove o primeiro módulo tornando-o atual
            Modulo nextModulo = copyModulos.peek(); //apenas verifica o proximo modulo
            if (nextModulo != null) { 
                for (TabDef nextLine: nextModulo.linhasTabelaDef) { //percorre  as linhas da tabela de definição do prox modulo
                    int proxEndereco = nextLine.getEndereco() + ultimoEnd;
                    String simbolo = nextLine.getSimbolo();
                    boolean sai = false;
                    for (TabDef linhaDef : moduloAtual.linhasTabelaDef){
                        if(linhaDef.getSimbolo().equals(simbolo)){
                            System.out.println("Erro! O simbolo "+simbolo+" ja foi definido no endereço "+linhaDef.getEndereco());
                            sai = true;
                        }
                    }
                    if (!sai){
                        String reloc = nextLine.getRealoc();
                        if(reloc.equals("a")){
                            reloc = "r";
                        }
                        adicionarOuAtualizarSimbolo(simbolo, proxEndereco, reloc, tabelaSimbolosGlobais);
                    }
                    
                    
                }
            } else {
                break;
            }
        }

    }
    
    public static void adicionarOuAtualizarSimbolo(String simbolo, int endereco, String realoc, LinkedList<TabGlobais> tabelaSimbolosGlobais) {
        boolean simboloExiste = false;
    
        for (TabGlobais linha : tabelaSimbolosGlobais) { //percorre as linhas da tabela de simbolos globais 
            if (linha.getSimbolo().equals(simbolo)) { //se a o simbolo da linha atual é igual so simbolo passado, atualiza o booleano
                simboloExiste = true;
                if (linha.getEndereco() < endereco) { //se o endereço atual for menor que o passado, atualiza ele
                    linha.setEndereco(endereco);
                }
                break;
            }
        }
    
        if (!simboloExiste) { //se o simbolo ainda não existe, o adiciona
            TabGlobais novaLinha = new TabGlobais(simbolo, endereco, realoc);
            tabelaSimbolosGlobais.add(novaLinha);
        }
    }
    
    public static void secondPass(LinkedList<TabGlobais> tabelaSimbolosGlobais, LinkedList<String> comandos,String[] arquivosObj) {
        LinkedList <TabUso> usosGlobais = new LinkedList<>();// cria uma tabela dos usos globais, os endereços de uso estão ajustados aqui
        int firstAddress=0;
        int lastAddress=0;
        for (String nomeArquivo : arquivosObj) {//circula por todos arquivos, o for passa por todos nomes de arquivos de entradas
            File arquivoAtual = new File(nomeArquivo);//cria a leitura do arquivo atual do for
            int semiCounter=0;//conta os ;
            try{
                Scanner leitor = new Scanner(arquivoAtual);//scanner para passar pelas linhas do arquivo
                String anterior="";
                int opsAnterior=0;
                while (semiCounter!=1) {
                    String linha = leitor.nextLine();
                    if(linha.contains(";")){//se acha o primeiro ; sai do loop
                        semiCounter++;
                    }else{//guarda a linha anterior ao primeiro ;
                        anterior = linha;
                    }
                }
                firstAddress = lastAddress;//atualiza o primeiro endereço do obj como o ultimo do obj anterior
                lastAddress = lastAddress + (Integer.parseInt(anterior.substring(0,FirstSpaceIdentifier(anterior)))) + (Integer.parseInt(anterior.substring(FirstSpaceIdentifier(anterior)+1,SecondSpaceIdentifier(anterior))));//atualiza o ultimo endereço do obj atual
                while (semiCounter!=2) {//só passa reto pela parte das definições
                    String linha = leitor.nextLine();
                    if(linha.contains(";")){
                        semiCounter++;
                    }
                }
                while(leitor.hasNextLine()){
                    String linha = leitor.nextLine();
                    String symbol = linha.substring(0,FirstSpaceIdentifier(linha));
                    int address=firstAddress+ Integer.parseInt(linha.substring(FirstSpaceIdentifier(linha)+1,SecondSpaceIdentifier(linha)));
                    String signal = linha.substring(SecondSpaceIdentifier(linha)+1);
                    TabUso global = new TabUso(symbol,address, signal);//cria o objeto do uso
                    usosGlobais.add(global);//adiciona o objeto a tabela global de usos
                }
            }catch (FileNotFoundException e) {
                System.out.println("Erro! O arquivo " + nomeArquivo + " não pode ser lido.");
            }
        }
        for(String string: comandos){//percorre todas linhas de comandos
            int address=Integer.parseInt(string.substring(0,FirstSpaceIdentifier(string)));
            int ops=Integer.parseInt(string.substring(FirstSpaceIdentifier(string)+1,SecondSpaceIdentifier(string)))-1;
            int sum=address+ops;
            int newAddress=0;
            for(TabUso usos : usosGlobais){//percorre a tabela de usos globais
                if(usos.getEndereco()>=address && usos.getEndereco()<=address+ops){//identifica se o endereço do uso está contido no endereço daquele comando
                    for(TabGlobais globalSymbols : tabelaSimbolosGlobais){//percorre a tabela de simbolos para achar o novo endereço
                        if(globalSymbols.getSimbolo().equals(usos.getSimbolo())){//acha dois simbolos iguais
                            newAddress = globalSymbols.getEndereco();//guarda o novo endereço
                        }
                    }
                    int aux=0;
                    while(address+aux<sum){//identifica qual operando é necessario substituir
                        if(address+aux==usos.getEndereco()){
                            sum=sum+address+aux;//sai do while no caso de achar qual o operando correto
                        }
                        aux++;
                    }
                    String newString="";
                    if(aux==1){//caso de ser o primeiro operando
                        if(SpaceAmount(string)==3){//caso do comando ter apenas 1 operando
                            newString = string.substring(0,ThirdSpaceIdentifier(string))+" "+Integer.toString(newAddress);
                        }else if(SpaceAmount(string)==4){//caso do comando ter 2 operandos
                            newString = string.substring(0,ThirdSpaceIdentifier(string))+" "+Integer.toString(newAddress)+string.substring(FourthSpaceIdentifier(string));
                        }
                    }else if(aux==2){//caso de ser o segundo operando
                        newString = string.substring(0,FourthSpaceIdentifier(string))+" "+Integer.toString(newAddress);
                    }
                    int index=comandos.indexOf(string);
                    comandos.set(index,newString);
                }
            }
        }
    }

    public static void fileCreator(LinkedList<String> commands, String fileName, LinkedList<TabGlobais> tabelaSimbolosGlobais, int tamanho) {
            try {
            FileWriter myWriter = new FileWriter(fileName);    //nome do primeiro arquivo de entrada .hpx (pegar o nome que fica na variável ali em cima)
            String escrita = "";  
            String endInicial = commands.getFirst().substring(0, FirstSpaceIdentifier(commands.getFirst())); //pegar o endereço inicial do módulo
            myWriter.write("Endereco Inicial:" + endInicial + "\n");
            myWriter.write("Tamanho da Pilha:" + tamanho + "\n");
            for (String comando : commands) {      //percorre a lista de comandos e escreve no arquivo
                escrita = comando + "\n";
                myWriter.write(escrita);
            }
            escrita = "";
            myWriter.write(";\n");
            for (TabGlobais linha : tabelaSimbolosGlobais) {        //pega a tabela de simbolos globais e percorre ela para escrever no arquivo
                escrita += linha.simbolo + " " + linha.endereco + " " + linha.modoReloc + "\n";
            }
            myWriter.write(escrita);
            myWriter.write(";\n");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    public static boolean hasSemicolon(String linha) {
        return linha.contains(";");

    }
    
    public static int dotIdentifier(String linha) {
        int index = 0;
        while (linha.charAt(index) != '.') {
            index++;
        }
        return index;
    }

    public static int FirstSpaceIdentifier(String linha) {
        int index = 0;
        while (linha.charAt(index) != ' ') {
            index++;
        }
        return index;
    }// identifica o indice do primeiro espaço na string

    public static int SecondSpaceIdentifier(String linha) {
        int index = FirstSpaceIdentifier(linha) + 1;
        while (linha.charAt(index) != ' ') {
            index++;
        }
        return index;
    }// identifica o indice do segundo espaço na strin

    public static int ThirdSpaceIdentifier(String linha) {
        int index = SecondSpaceIdentifier(linha)+1;
        while (linha.charAt(index) != ' ') {
            index++;
        }
        return index;
    }// identifica o indice do terceiro espaço na string

    public static int FourthSpaceIdentifier(String linha) {
        int index = ThirdSpaceIdentifier(linha)+1;
        while (linha.charAt(index) != ' ') {
            index++;
        }
        return index;
    }// identifica o indice do quarto espaço na string

    public static int SpaceAmount(String linha) {
        int counter=0;
        int aux=0;
        while(aux<linha.length()){
            if(linha.charAt(aux)==' '){
                counter++;
            }
            aux++;
        }
        return counter;
    }

    public static int sizeCommands(LinkedList<String> commands){
        
        return commands.size();
    }

    public static int tamPrimeiraPilha(String[] arquivosObj){
        File primeiroArquivo = new File(arquivosObj[0]);
        int tamPilha = 0;
        try{
            Scanner leitor = new Scanner(primeiroArquivo);
            String linha = leitor.nextLine();
            while(!hasSemicolon(linha)){
                tamPilha = tamPilha + 1;
                linha = leitor.nextLine();
            }
            leitor.close();
        }catch(FileNotFoundException e){
            System.out.println("Erro! O arquivo " + primeiroArquivo + " não pode ser lido.");
        }

        return tamPilha;
    }

    private static void ajustarComandos(String[] arquivosObj, LinkedList<String> commands){
        File primeiroArquivo = new File(arquivosObj[0]);
        Integer ultimoEnd = 0;
        try{ 
            Scanner leitor = new Scanner(primeiroArquivo);
            String linha = leitor.nextLine();
            while (!hasSemicolon(linha)) {
                commands.add(linha);
                linha = leitor.nextLine();
            }
            leitor.close();
        }catch(FileNotFoundException e){
           System.out.println("Erro! O arquivo " + primeiroArquivo + " não pode ser lido.");
        }

        String ultimoComando = commands.getLast();
        ultimoEnd = Integer.parseInt(ultimoComando.substring(0,FirstSpaceIdentifier(ultimoComando)));
        ultimoEnd = ultimoEnd + 1;       
        for(int i = 1; i < arquivosObj.length; i++){
            File arquivoAtual = new File(arquivosObj[i]);
            try{
                Scanner leitor = new Scanner(arquivoAtual);
                String linha = leitor.nextLine();
                while(!hasSemicolon(linha)){
                    String endString = linha.substring(0,FirstSpaceIdentifier(linha));  
                        int novoEndereco = Integer.parseInt(endString);
                        novoEndereco = novoEndereco + ultimoEnd;
                        String linhaAtualizada = novoEndereco + linha.substring(FirstSpaceIdentifier(linha));
                        commands.add(linhaAtualizada);
                        linha = leitor.nextLine();
                    
                }
                leitor.close();
            }catch(FileNotFoundException e){
                System.out.println("Erro! O arquivo " + primeiroArquivo + " não pode ser lido.");
            }
        }

       
        
    }
}

    

package com.maquina.front;


import java.net.URL;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

import com.application.globals;
import com.maquina.Memoria;
import com.maquina.montador.Montador;
import com.maquina.ligador.Ligador;
import com.maquina.mid_end.MemoryDisplay;
import com.maquina.registradores.ACC;
import com.maquina.registradores.PC;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


public class MachineController implements Initializable {
    public static String valorAtualTextField = "";
    public boolean confirmouInput = false;
    public static ObservableList<MemoryDisplay> conjunto_instrucoes_dados = FXCollections.observableArrayList();
    public StringProperty outputLabelProperty = new SimpleStringProperty("");
    public StringProperty programCounterLabelProperty = new SimpleStringProperty(String.valueOf(PC.getPC()));
    public StringProperty listCounterLabelProperty = new SimpleStringProperty("");
    public StringProperty accumulatorLabelProperty = new SimpleStringProperty(String.valueOf(ACC.getACC()));
    public StringProperty enderecoLabelProperty = new SimpleStringProperty("");
    public StringProperty operando1LabelProperty = new SimpleStringProperty("0");
    public StringProperty operando2LabelProperty = new SimpleStringProperty("0");
    @Override
    public void initialize(URL arg0, ResourceBundle arg1)  {
        Memoria.inicializarMemoria();

        //Teste individual de algumas instrucoes
        // Memoria.setDataOnMemory(0, 12, true); //00: READ
        // Memoria.setDataOnMemory(1, -1, false); //01: Operando do READ
        // Memoria.setDataOnMemory(2, 3, true); //02: LOAD
        // Memoria.setDataOnMemory(3, 6, false); //03: Operando do Load
        // Memoria.setDataOnMemory(4, 2, true); //04: ADD
        // Memoria.setDataOnMemory(5, 4, false); //05: Operando do ADD
        // Memoria.setDataOnMemory(6, 7, true); //06: STORE
        // Memoria.setDataOnMemory(7, -999, false); //07: Operando do Store
        // Memoria.setDataOnMemory(8, 10, true); //08: DIVIDE
        // Memoria.setDataOnMemory(9, 10, false); //09: Operando do DIVIDE
        // Memoria.setDataOnMemory(10, 11, true); //10: STOP

        //Teste com desvios (Itera reduzindo um número até 0)
        // macroProcess.executeMacro();
        // try {
        //     Montador.executeMontador();
        // } catch (IOException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
        
        
        try{
            //Macros.execute();
            Montador.executeMontador();
            Ligador.main(null);
            File arquivo_memoria = new File("src/main/java/com/maquina/carregador/mod1.hpx");
            Scanner reader = new Scanner(arquivo_memoria);
            reader.nextLine(); 
            reader.nextLine();
            int operando1;
            int operando2;
            int opcode;
            int endereco;
            while (reader.hasNextLine()){               
                String line = reader.nextLine();
                if (line.equals(";")){                
                    break;
                }                
                String[] fragmentos = line.split(" ");
                endereco = Integer.parseInt(fragmentos[0]);
                opcode = Integer.parseInt(fragmentos[2]);                
                Memoria.setDataOnMemory(endereco, opcode, true);
                if (fragmentos.length > 3){
                    operando1 = Integer.parseInt(fragmentos[3]);
                    Memoria.setDataOnMemory(endereco+1, operando1, false);
                }
                if (fragmentos.length > 4){
                    operando2 = Integer.parseInt(fragmentos[4]);
                    Memoria.setDataOnMemory(endereco+2, operando2, false);
                }
            }
            // Carregador.loadPrograma("src/main/java/com/maquina/carregador/mod1.hpx");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Memoria.setDataOnMemory(0, 3, true); //00: LOAD
        // Memoria.setDataOnMemory(1, 5, false); //01: Operando do LOAD -> 5
        // Memoria.setDataOnMemory(2, 2, true); //02: ADD
        // Memoria.setDataOnMemory(3, -1, false); //03: Operando do ADD -> -1
        // Memoria.setDataOnMemory(4, 4, true); //04: BRZERO
        // Memoria.setDataOnMemory(5, 100, false); //05: Operando do BRZERO -> 100 (se cair no desviio volta vai para instrução armazenada no endereço 100)
        // Memoria.setDataOnMemory(6, 0, true); //06: BR
        // Memoria.setDataOnMemory(7, 2, false); //07 Operando do BR -> 0 (Pula incondicionalmente pra instrução armazenada no endereço 0)
        // Memoria.setDataOnMemory(100, 11, true); //100: STOP
        
        

        textFieldOutput.setDisable(true);
        outputLabel.textProperty().bind(outputLabelProperty);
        programCounterLabel.textProperty().bind(programCounterLabelProperty);
        listCounterLabel.textProperty().bind(listCounterLabelProperty);
        accumulatorLabel.textProperty().bind(accumulatorLabelProperty);
        enderecoLabel.textProperty().bind(enderecoLabelProperty);
        operando1Label.textProperty().bind(operando1LabelProperty);
        operando2Label.textProperty().bind(operando2LabelProperty);
        for (int i=0; i < 1000; i++){
            if (Memoria.getIsInstrucao(i)){
                MemoryDisplay aux = MemoryDisplay.opcodeToMnemonic(Memoria.getDataFromMemory(i), i, true);
                conjunto_instrucoes_dados.add(aux);
            } else {
                MemoryDisplay aux2 = MemoryDisplay.opcodeToMnemonic(Memoria.getDataFromMemory(i), i, false);
                conjunto_instrucoes_dados.add(aux2);
            }
        }
        enderecoMemoria.setCellValueFactory(new PropertyValueFactory<>("enderecoInstrucao"));
        listaMnemonicos.setCellValueFactory(new PropertyValueFactory<>("mnemonico"));

        campoEndereco.setItems(conjunto_instrucoes_dados);

        campoEndereco.getSelectionModel().select(PC.getPC());//Highlightar a instrução A SER executada

    }

    @FXML
    private Label accumulatorLabel;

    @FXML
    private Label enderecoLabel;

    @FXML
    private Label listCounterLabel;

    @FXML
    private Label outputLabel;

    @FXML
    private Label operando2Label;

    @FXML
    private Label programCounterLabel;


    @FXML
    public Button botaoInput;

    @FXML
    private Button botaoRun;

    @FXML
    private Button botaoSteps;

    @FXML
    private TableView<MemoryDisplay> campoEndereco;

    @FXML
    public TextField campoInput;

    @FXML
    private TextField textFieldOutput;

    @FXML
    private TableColumn<MemoryDisplay, ?> enderecoMemoria;

   

    @FXML
    private TableColumn<MemoryDisplay, ?> listaMnemonicos;

    @FXML
    private Label operando1Label;



    @FXML
    void inputData(ActionEvent event) {
        confirmouInput = true;
    }

    @FXML
    void runStepByStep(ActionEvent event) {
        if(conjunto_instrucoes_dados.get(PC.getPC()).getIsInstrucao()){
        String mnemonico_a_ser_executado = conjunto_instrucoes_dados.get(PC.getPC()).getMnemonico();
        if (mnemonico_a_ser_executado == "READ"){//Para parar o sistema caso seja uma instrução que dependa do input.
            if (!confirmouInput){
                System.out.println("Por favor, confirme o Input, apertando no botão OK :)");
                campoInput.requestFocus();
                return; //Sai da função sem incrementar PC, usuário precisa clicar no ok. 
            } else {
                confirmouInput = false; //Uma vez que ele tenha clicado no botão OK, o input será consumido pelo read, e caso haja um novo read, ele vair ter que confirmar o input dnv.
            }
        }
        valorAtualTextField = campoInput.getText();
        operando1LabelProperty.set(String.valueOf(Memoria.getDataFromMemory(PC.getPC()+1)));
        MemoryDisplay.mnemonicToExecution(mnemonico_a_ser_executado); //O PC+1 é feito dentro de cada uma das instruções, pois o número de operandos varia.
        // campoEndereco.getSelectionModel().select(PC.getPC()); //HIGHLIGHTAR A CELULA CERTA 
        outputLabelProperty.set(globals.out);
        programCounterLabelProperty.set(String.valueOf(PC.getPC()));
        accumulatorLabelProperty.set(String.valueOf(ACC.getACC()));
        refreshMemoria();


    } else {
        PC.setPC(PC.getPC()+1);
    }
    }

    @FXML
    void runTudodeUmaVez(ActionEvent event) {
    int tamanhoDoPrograma = conjunto_instrucoes_dados.size();
        for (int i = 0; i < tamanhoDoPrograma; i++){
        if(conjunto_instrucoes_dados.get(PC.getPC()).getIsInstrucao()){
            String mnemonico_a_ser_executado = conjunto_instrucoes_dados.get(PC.getPC()).getMnemonico();
        if (mnemonico_a_ser_executado == "STOP"){
            System.err.println("O Programa encerrou, entretanto, acreditamos que o usuário gostaria de ver como ficou os registradores");
            return;
        }
        if (mnemonico_a_ser_executado == "READ"){//Para parar o sistema caso seja uma instrução que dependa do input.
            if (!confirmouInput){
                System.out.println("Por favor, confirme o Input, apertando no botão OK :)");
                campoInput.requestFocus();
                return; //Sai da função sem incrementar PC, usuário precisa clicar no ok. 
            } else {
                confirmouInput = false; //Uma vez que ele tenha clicado no botão OK, o input será consumido pelo read, e caso haja um novo read, ele vair ter que confirmar o input dnv.
            }
        }
        valorAtualTextField = campoInput.getText();
        operando1LabelProperty.set(String.valueOf(Memoria.getDataFromMemory(PC.getPC()+1)));
        MemoryDisplay.mnemonicToExecution(mnemonico_a_ser_executado);
        // campoEndereco.getSelectionModel().select(PC.getPC());
        outputLabelProperty.set(globals.out);
        programCounterLabelProperty.set(String.valueOf(PC.getPC()));
        accumulatorLabelProperty.set(String.valueOf(ACC.getACC()));
        refreshMemoria();
        }
    }
    }
    void refreshMemoria(){
        for (int i=0; i < 1000; i++){
            if (Memoria.getIsInstrucao(i)){
                MemoryDisplay aux = MemoryDisplay.opcodeToMnemonic(Memoria.getDataFromMemory(i), i, true);
                conjunto_instrucoes_dados.set(i, aux);
            } else {
                MemoryDisplay aux2 = MemoryDisplay.opcodeToMnemonic(Memoria.getDataFromMemory(i), i, false);
                conjunto_instrucoes_dados.set(i, aux2);
            }
        }
        campoEndereco.getSelectionModel().select(PC.getPC());
    }
}




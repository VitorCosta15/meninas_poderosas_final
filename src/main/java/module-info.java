module com.application {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base; //Colocar qualquer lib importada

    // opens com.maquina to javafx.fxml
    opens com.application to javafx.fxml;  //Colocar todos os packages d trabalho
    exports com.application;

    // package da interface
    opens com.maquina.front;
    opens com.maquina.mid_end;
    opens com.maquina.instrucoes;
    exports com.maquina to javafx.graphics;
    
   
    
}

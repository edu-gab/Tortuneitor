package com.mycompany.tortuneitorpro;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class PrimaryController {

    @FXML
    private TextField nombreTextField;

    @FXML
    private void switchToSecondary() throws IOException {
        String nombre = nombreTextField.getText();
        if (nombre == null || nombre.trim().isEmpty()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Campo vacío");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, ingrese su nombre para continuar.");
            alert.showAndWait();
        } else {
            App.setRoot("secondary");
            SecondaryController controller = App.getFXMLLoader().getController();
            controller.setSaludo(nombre);
        }
    }
}

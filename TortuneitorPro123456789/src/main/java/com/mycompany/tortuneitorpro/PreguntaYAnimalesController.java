/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tortuneitorpro;

import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author USUARIO DELL
 */
public class PreguntaYAnimalesController {

    @FXML
    private TextField numeroPreguntasTextField;

    @FXML
    private Label rangoPreguntasLabel;  // Cambiamos a rangoPreguntasLabel para mayor claridad

    @FXML
    private Label animalesLabel;

    private int maxPreguntas;
    private List<String> posiblesAnimales;

    public void setMaxPreguntas(int maxPreguntas) {
        this.maxPreguntas = maxPreguntas;
        // Actualiza el texto del Label para incluir el rango máximo de preguntas
        rangoPreguntasLabel.setText("Ingrese la cantidad de preguntas (1 - " + maxPreguntas + "):");
    }

    public void setPosiblesAnimales(List<String> animales) {
        this.posiblesAnimales = animales;
        actualizarAnimalesLabel();
    }

    private void actualizarAnimalesLabel() {
        StringBuilder animalesText = new StringBuilder("Posibles animales:\n");
        for (String animal : posiblesAnimales) {
            animalesText.append("- ").append(animal).append("\n");
        }
        animalesLabel.setText(animalesText.toString());
    }

    @FXML
    private void confirmar() {
        try {
            int numPreguntas = Integer.parseInt(numeroPreguntasTextField.getText());
            if (numPreguntas > 0 && numPreguntas <= maxPreguntas) {
                // Retorna el valor ingresado y cierra la ventana
                Stage stage = (Stage) numeroPreguntasTextField.getScene().getWindow();
                stage.setUserData(numPreguntas);
                stage.close();
            } else {
                mostrarError("Por favor, ingresa un número entre 1 y " + maxPreguntas);
            }
        } catch (NumberFormatException e) {
            mostrarError("Por favor, ingresa un número válido.");
        }
    }

    @FXML
    private void cancelar() {
        Stage stage = (Stage) numeroPreguntasTextField.getScene().getWindow();
        stage.close();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de Validación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

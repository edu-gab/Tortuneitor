/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tortuneitorpro;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class AgregarAnimalController {

    @FXML
    private Label preguntaLabel;

    @FXML
    private TextField nombreAnimalTextField;

    @FXML
    private Button siButton;
    

    @FXML
    private Button noButton;
    
    @FXML
    private Button regresarButton;

    private List<String> preguntas;
    private List<String> respuestas = new ArrayList<>();
    private int currentIndex = 0;
    private String archivoDestino;
    private boolean animalAgregado = false; 
    private String nuevoAnimal;
    
    private void resetButtonColors() {
        siButton.setStyle(""); 
        noButton.setStyle(""); 
    }

    @FXML
    private void handleRegresar() {
        if (currentIndex > 0) {
            currentIndex--;
            preguntaLabel.setText(preguntas.get(currentIndex));
            respuestas.remove(respuestas.size() - 1);
            siButton.setDisable(false);
            noButton.setDisable(false);
            siButton.setVisible(true);
            noButton.setVisible(true);
        }
    }

    
    public void setPreguntas(List<String> preguntas, String archivoDestino) {
        // Filtrar las preguntas para eliminar las líneas vacías
        this.preguntas = preguntas.stream()
                                  .filter(pregunta -> pregunta != null && !pregunta.trim().isEmpty())
                                  .collect(Collectors.toList());

        this.archivoDestino = archivoDestino;

        if (!this.preguntas.isEmpty()) {
            preguntaLabel.setText(this.preguntas.get(currentIndex));
            if (currentIndex == 0) {
                regresarButton.setDisable(true); // Deshabilitar "Regresar" si es la primera pregunta
            }
        } else {
            preguntaLabel.setText("No hay más preguntas.");
            siButton.setDisable(true);
            noButton.setDisable(true);
            regresarButton.setDisable(true);
        }
    }

    @FXML
    private void handleSi() {
        if (currentIndex < preguntas.size()) {
            respuestas.add("si");
            currentIndex++;
            if (currentIndex < preguntas.size()) {
                preguntaLabel.setText(preguntas.get(currentIndex));
                regresarButton.setDisable(false); // Habilitar "Regresar" después de la primera pregunta
            } else {
                preguntaLabel.setText("No hay más preguntas.");
                siButton.setDisable(true);
                noButton.setDisable(true);
                siButton.setVisible(false);
                noButton.setVisible(false);
                regresarButton.setDisable(true); // Deshabilitar "Regresar" al final
            }
        }
    }

    @FXML
    private void handleNo() {
        if (currentIndex < preguntas.size()) {
            respuestas.add("no");
            currentIndex++;
            if (currentIndex < preguntas.size()) {
                preguntaLabel.setText(preguntas.get(currentIndex));
                regresarButton.setDisable(false); // Habilitar "Regresar" después de la primera pregunta
            } else {
                preguntaLabel.setText("No hay más preguntas.");
                siButton.setDisable(true);
                noButton.setDisable(true);
                siButton.setVisible(false);
                noButton.setVisible(false);
                regresarButton.setDisable(true); // Deshabilitar "Regresar" al final
            }
        }
    }


    @FXML
    private void handleAgregar() {
        try {
            String nombreAnimal = nombreAnimalTextField.getText().trim();
            if (nombreAnimal.isEmpty() || respuestas.size() != preguntas.size()) {
                showError("Por favor, responde todas las preguntas y asegúrate de ingresar un nombre para el animal.");
                return;
            }

            agregarAnimalAlArchivo(nombreAnimal, respuestas);
            nuevoAnimal = nombreAnimal;
            animalAgregado = true;

            Stage stage = (Stage) preguntaLabel.getScene().getWindow();
            stage.close();

        } catch (IOException e) {
            showError("Error al guardar el animal: " + e.getMessage());
        }
    }

    private void agregarAnimalAlArchivo(String nombreAnimal, List<String> respuestas) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoDestino, true))) {
            writer.newLine();  // Agregar una nueva línea antes de escribir el nuevo animal
            writer.write(nombreAnimal + " " + String.join(" ", respuestas));
        }
    }

    public boolean isAnimalAgregado() {
        return animalAgregado;
    }

    public String getNuevoAnimal() {
        return nuevoAnimal;
    }

    private void showError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de Validación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

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

    private List<String> preguntas;
    private List<String> respuestas = new ArrayList<>();
    private int currentIndex = 0;
    private String archivoDestino;
    private boolean animalAgregado = false; 
    private String nuevoAnimal;

    public void setPreguntas(List<String> preguntas, String archivoDestino) {
        this.preguntas = preguntas;
        this.archivoDestino = archivoDestino;
        if (!preguntas.isEmpty()) {
            preguntaLabel.setText(preguntas.get(currentIndex));
        } else {
            preguntaLabel.setText("No hay más preguntas.");
            siButton.setDisable(true);
            noButton.setDisable(true);
        }
    }

    @FXML
    private void handleSi() {
        if (currentIndex < preguntas.size()) {
            respuestas.add("si");
            currentIndex++;
            if (currentIndex < preguntas.size()) {
                preguntaLabel.setText(preguntas.get(currentIndex));
            } else {
                preguntaLabel.setText("No hay más preguntas.");
                siButton.setDisable(true);
                noButton.setDisable(true);
                siButton.setVisible(false);
                noButton.setVisible(false);
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
            } else {
                preguntaLabel.setText("No hay más preguntas.");
                siButton.setDisable(true);
                noButton.setDisable(true);
                siButton.setVisible(false);
                noButton.setVisible(false);
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


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

    @FXML
    private Button siguienteButton; // Botón para avanzar a la siguiente pregunta

    private List<String> preguntas;
    private List<String> respuestas = new ArrayList<>();
    private int currentIndex = 0;
    private String archivoDestino;
    private boolean animalAgregado = false; 
    private String nuevoAnimal;
    private String respuestaActual;

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
            resetButtonColors(); // Restablecer colores cuando se regresa
            siButton.setDisable(false);
            noButton.setDisable(false);
            siButton.setVisible(true);
            noButton.setVisible(true);
            siguienteButton.setDisable(true); // Deshabilitar el botón "Siguiente" al regresar

            if (currentIndex == 0) {
                regresarButton.setDisable(true); // Deshabilitar "Regresar" si es la primera pregunta
            }
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
            siguienteButton.setDisable(true);
            regresarButton.setDisable(true);
        }
    }

    @FXML
    private void handleSi() {
        respuestaActual = "si";
        siButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        noButton.setStyle(""); // Restablecer el color del botón "No"
        siguienteButton.setDisable(false); // Habilitar el botón "Siguiente"
    }

    @FXML
    private void handleNo() {
        respuestaActual = "no";
        noButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        siButton.setStyle(""); // Restablecer el color del botón "Sí"
        siguienteButton.setDisable(false); // Habilitar el botón "Siguiente"
    }

    @FXML
    private void handleSiguiente() {
        if (respuestaActual != null) {
            respuestas.add(respuestaActual);
            currentIndex++;
            if (currentIndex < preguntas.size()) {
                preguntaLabel.setText(preguntas.get(currentIndex));
                resetButtonColors(); // Restablecer los colores de los botones para la siguiente pregunta
                siguienteButton.setDisable(true); // Deshabilitar el botón "Siguiente" hasta que se seleccione "Sí" o "No"
                regresarButton.setDisable(false); // Habilitar "Regresar" cuando se avanza a la siguiente pregunta
            } else {
                preguntaLabel.setText("No hay más preguntas.");
                siButton.setDisable(true);
                noButton.setDisable(true);
                siButton.setVisible(false);
                noButton.setVisible(false);
                regresarButton.setDisable(true); // Deshabilitar "Regresar" al final
                siguienteButton.setDisable(true); // Deshabilitar "Siguiente" al final
            }
        } else {
            showError("Por favor, seleccione una respuesta antes de continuar.");
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

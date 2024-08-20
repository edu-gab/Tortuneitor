/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tortuneitorpro;

import java.io.BufferedReader;
import java.io.FileReader;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;

public class ResultadosController {

    @FXML
    private Label resultadoLabel;

    @FXML
    private ListView<String> animalesListView;

    @FXML
    private ListView<String> historialListView;

    @FXML
    private Button finalizarButton;

    @FXML
    private Button jugarDeNuevoButton;

    @FXML
    private Button agregarAnimalButton;
    
    @FXML
    private ImageView triste;
   

    private List<String> posiblesAnimales;
    private List<String> historialPreguntasRespuestas;
    private String archivoDestino;

    public void setResultados(List<String> posiblesAnimales, List<String> historialPreguntasRespuestas, String archivoDestino) {
        this.posiblesAnimales = posiblesAnimales;
        this.historialPreguntasRespuestas = historialPreguntasRespuestas;
        this.archivoDestino = archivoDestino;
        historialListView.setItems(FXCollections.observableArrayList(historialPreguntasRespuestas));

        if (posiblesAnimales.isEmpty()) {
            resultadoLabel.setText("No contamos con ese animal.");
            triste.setVisible(true);
            animalesListView.setVisible(false);
            agregarAnimalButton.setVisible(true);
        } else if (posiblesAnimales.size() == 1) {
            resultadoLabel.setText("El animal es: " + posiblesAnimales.get(0));
            triste.setVisible(false);
            animalesListView.setVisible(false);
            agregarAnimalButton.setVisible(false);
        } else {
            resultadoLabel.setText("Los posibles animales son:");
            animalesListView.setItems(FXCollections.observableArrayList(posiblesAnimales));
            animalesListView.setVisible(true);
            agregarAnimalButton.setVisible(false);
            triste.setVisible(false);
        }
    }

    @FXML
    private void finalizar() {
        Stage stage = (Stage) finalizarButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void jugarDeNuevo() throws IOException {
        Stage stage = (Stage) jugarDeNuevoButton.getScene().getWindow();
        stage.close();
        App.setRoot("secondary");
    }

    @FXML
    private void agregarNuevoAnimal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("agregar_animal.fxml"));
            Parent root = loader.load();
            AgregarAnimalController controller = loader.getController();

            // Cargar preguntas desde el archivo correcto
            List<String> preguntas = loadQuestions(archivoDestino.replace("animales.txt", "preguntas.txt"));
            controller.setPreguntas(preguntas, archivoDestino);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Agregar Nuevo Animal");
            stage.showAndWait();

            if (controller.isAnimalAgregado()) {
                posiblesAnimales.add(controller.getNuevoAnimal());
                animalesListView.setItems(FXCollections.observableArrayList(posiblesAnimales));
            }
        } catch (IOException e) {
            mostrarError("Error al intentar agregar un nuevo animal: " + e.getMessage());
        }
    }



    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    private List<String> loadQuestions(String filePath) throws IOException {
    List<String> questions = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = reader.readLine()) != null) {
            questions.add(line.trim());
        }
    }
    return questions;
}

}

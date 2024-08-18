/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tortuneitorpro;

import java.io.BufferedReader;
import java.io.FileReader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameControllerUsuario {

    
    @FXML
    private Label preguntaLabel;
    @FXML
    private Button siguienteButton;
    @FXML
    private Button siButton;
    @FXML
    private Button noButton;
    @FXML
    private Button regresarButton;

    private DecisionTree decisionTree;
    private TreeNode currentNode;
    private int questionsAsked = 0;
    private int maxQuestions;
    private String respuestaActual;
    private List<String> preguntasYRespuestas;
    private List<TreeNode> nodoHistorial = new ArrayList<>();

    @FXML
    public void initialize() {
        decisionTree = new DecisionTree();
        preguntasYRespuestas = new ArrayList<>();
        String questionsFilePath = "src/main/resources/Usuario/preguntas.txt";
        String animalsFilePath = "src/main/resources/Usuario/animales.txt";
        try {
            List<String> questions = loadQuestions(questionsFilePath);
            List<Animal> animals = loadAnimals(animalsFilePath, questions);
            decisionTree.loadTreeFromQuestionsAndAnimals(questions, animals);
            currentNode = decisionTree.getRoot();

            if (currentNode == null) {
                showError("Error: El árbol de decisiones no se pudo cargar correctamente.");
                return;
            }

            updatePreguntaLabel();
            regresarButton.setDisable(true); // Deshabilitar "Regresar" al comienzo
        } catch (IOException e) {
            showError("Error cargando los archivos de preguntas o animales.");
        }
    }

    public void startGame(int numQuestions) {
        this.maxQuestions = numQuestions;
        this.questionsAsked = 0;
        preguntasYRespuestas.clear();
        nodoHistorial.clear();
        resetButtonColors();
        currentNode = decisionTree.getRoot();
        updatePreguntaLabel();
    }

    @FXML
    private void handleSi() {
        respuestaActual = "si";
        siButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        noButton.setStyle(""); // Resetea el color del botón "No"
    }

    @FXML
    private void handleNo() {
        respuestaActual = "no";
        noButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        siButton.setStyle(""); // Resetea el color del botón "Sí"
    }

    @FXML
    private void siguientePregunta() {
        if (currentNode == null) {
            showError("No hay más preguntas disponibles.");
            return;
        }

        nodoHistorial.add(currentNode);  // Almacenar el nodo actual en el historial
        preguntasYRespuestas.add("Pregunta: " + currentNode.getPregunta() + " - Respuesta: " + respuestaActual);

        if (respuestaActual.equals("si")) {
            currentNode = currentNode.getSi();
        } else if (respuestaActual.equals("no")) {
            currentNode = currentNode.getNo();
        }

        questionsAsked++;

        if (questionsAsked >= maxQuestions || decisionTree.isLeaf(currentNode)) {
            try {
                mostrarResultado();
            } catch (IOException e) {
                showError("Error mostrando los resultados.");
            }
        } else {
            resetButtonColors();
            updatePreguntaLabel();
            regresarButton.setDisable(false); // Habilitar "Regresar" después de la primera pregunta
        }
    }

    @FXML
    private void handleRegresar() {
        if (questionsAsked > 0) {
            currentNode = retrocederEnArbol(); // Método que retrocede en el árbol de decisiones
            preguntaLabel.setText(currentNode.getPregunta());
            respuestaActual = null;
            resetButtonColors();
            questionsAsked--;
        }
    }

    private TreeNode retrocederEnArbol() {
        if (!nodoHistorial.isEmpty()) {
            TreeNode previo = nodoHistorial.remove(nodoHistorial.size() - 1);
            preguntasYRespuestas.remove(preguntasYRespuestas.size() - 1); // Remover la última respuesta del historial
            return previo;
        } else {
            return currentNode; // Si no hay historial, quedarse en el nodo actual
        }
    }

    private void resetButtonColors() {
        siButton.setStyle(""); 
        noButton.setStyle(""); 
    }

    private void updatePreguntaLabel() {
        if (currentNode != null) {
            preguntaLabel.setText(currentNode.getPregunta());
        } else {
            preguntaLabel.setText("No hay más preguntas.");
        }
    }

    @FXML
    private void mostrarResultado() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("resultados.fxml"));
        Parent root = loader.load();
        ResultadosController controller = loader.getController();

        String archivoDestino = "src/main/resources/Usuario/animales.txt"; // Define aquí la ruta del archivo de destino

        controller.setResultados(decisionTree.getPossibleAnimals(currentNode), preguntasYRespuestas, archivoDestino);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Resultados");
        stage.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private List<String> loadQuestions(String filePath) throws IOException {
        List<String> questions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    questions.add(line);
                }
            }
        }
        return questions;
    }

    private List<Animal> loadAnimals(String filePath, List<String> questions) throws IOException {
        List<Animal> animals = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(" ");
                    if (parts.length == questions.size() + 1) {
                        String name = parts[0].trim();
                        List<String> responses = new ArrayList<>();
                        for (int i = 1; i < parts.length; i++) {
                            responses.add(normalizeResponse(parts[i].trim()));
                        }
                        animals.add(new Animal(name, responses));
                    }
                }
            }
        }
        return animals;
    }

    private String normalizeResponse(String response) {
        response = response.toLowerCase();
        response = response.replace("á", "a")
                           .replace("é", "e")
                           .replace("í", "i")
                           .replace("ó", "o")
                           .replace("ú", "u");

        if (response.equals("si") || response.equals("sí")) {
            return "si";
        }
        if (response.equals("no")) {
            return "no";
        }
        return response;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tortuneitorpro;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.io.BufferedReader;
import java.io.FileReader;
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

    private DecisionTree decisionTree;
    private TreeNode currentNode;
    private int questionsAsked = 0;
    private int maxQuestions;
    private String respuestaActual;
    private List<String> preguntasYRespuestas;

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
        } catch (IOException e) {
            showError("Error cargando los archivos de preguntas o animales.");
        }
    }

    private String normalizeResponse(String response) {
        // Convertir la respuesta a minúsculas y eliminar acentos
        response = response.toLowerCase();

        // Reemplazar caracteres acentuados por sus equivalentes sin acento
        response = response.replace("á", "a")
                           .replace("é", "e")
                           .replace("í", "i")
                           .replace("ó", "o")
                           .replace("ú", "u");

        // Si la respuesta es afirmativa, devolver "si"
        if (response.equals("si") || response.equals("sí")) {
            return "si";
        }
        // Si la respuesta es negativa, devolver "no"
        if (response.equals("no")) {
            return "no";
        }

        // Si no es ni "si" ni "no", devolver la respuesta original (esto depende de tu lógica)
        return response;
    }

    
    public void startGame(int numQuestions) {
        this.maxQuestions = numQuestions;
        this.questionsAsked = 0;
        preguntasYRespuestas.clear();
        try {
            String questionsFilePath = "src/main/resources/Usuario/preguntas.txt";
            String animalsFilePath = "src/main/resources/Usuario/animales.txt";
            List<String> questions = loadQuestions(questionsFilePath);
            List<Animal> animals = loadAnimals(animalsFilePath, questions);
            decisionTree.loadTreeFromQuestionsAndAnimals(questions, animals);
            currentNode = decisionTree.getRoot();
            updatePreguntaLabel();
        } catch (IOException e) {
            showError("Error cargando los archivos de preguntas o animales.");
        }
    }

    @FXML
    private void handleSi() {
        respuestaActual = "si";
        siButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        noButton.setStyle(null);
    }

    @FXML
    private void handleNo() {
        respuestaActual = "no";
        noButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        siButton.setStyle(null);
    }

    @FXML
    private void siguientePregunta() {
        if (currentNode == null) {
            showError("No hay más preguntas disponibles.");
            return;
        }

        if (respuestaActual == null) {
            showError("Por favor, selecciona una respuesta antes de continuar.");
            return;
        }

        // Guardar la pregunta actual y la respuesta
        preguntasYRespuestas.add("Pregunta: " + currentNode.getPregunta() + " - Respuesta: " + respuestaActual);

        // Avanzar al siguiente nodo dependiendo de la respuesta actual
        if (respuestaActual.equals("si")) {
            currentNode = currentNode.getSi();
        } else if (respuestaActual.equals("no")) {
            currentNode = currentNode.getNo();
        }

        questionsAsked++;

        // Comprobar si se llegó a una hoja o si se alcanzó el límite de preguntas
        if (decisionTree.isLeaf(currentNode) || questionsAsked >= maxQuestions) {
            mostrarResultado();
        } else {
            resetButtonColors();
            updatePreguntaLabel();
        }
    }

    private void updatePreguntaLabel() {
        if (currentNode != null && !decisionTree.isLeaf(currentNode)) {
            preguntaLabel.setText(currentNode.getPregunta());
        } else {
            preguntaLabel.setText("No hay más preguntas.");
        }
    }

    private void mostrarResultado() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Resultado");

        StringBuilder resultado = new StringBuilder();
        for (String preguntaYRespuesta : preguntasYRespuestas) {
            resultado.append(preguntaYRespuesta).append("\n");
        }

        List<String> posiblesAnimales = decisionTree.getPossibleAnimals(currentNode);

        if (posiblesAnimales.isEmpty()) {
            resultado.append("\nLo siento, no contamos con un animal que cumpla esas características.");
        } else if (posiblesAnimales.size() == 1) {
            resultado.append("\n¡El animal es: ").append(posiblesAnimales.get(0)).append("!");
        } else {
            resultado.append("\nLos posibles animales son: ").append(String.join(", ", posiblesAnimales));
        }

        alert.setContentText(resultado.toString());
        alert.showAndWait();
    }

    private void resetButtonColors() {
        siButton.setStyle(null);
        noButton.setStyle(null);
        respuestaActual = null;
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
                questions.add(line.trim());
            }
        }
        return questions;
    }

    private List<Animal> loadAnimals(String filePath, List<String> questions) throws IOException {
        List<Animal> animals = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
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
        return animals;
    }
}

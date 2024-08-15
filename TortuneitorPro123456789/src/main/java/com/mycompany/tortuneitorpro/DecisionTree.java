/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tortuneitorpro;

/**
 *
 * @author Cristhian
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DecisionTree {
    private TreeNode root;

    public DecisionTree() {
        this.root = null;
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public boolean isLeaf(TreeNode node) {
        if (node == null) {
            return true;  // Si el nodo es nulo, considerarlo como una hoja
        }
        return node.getSi() == null && node.getNo() == null;
    }

    // Cargar el árbol desde un archivo
    public void loadTreeFromFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        root = buildTree(reader);
        reader.close();
    }

    // Método recursivo para construir el árbol
    private TreeNode buildTree(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if (line == null || line.equals("#")) { // El símbolo "#" representa un nodo vacío
            return null;
        }

        TreeNode node = new TreeNode(line.trim());
        node.setSi(buildTree(reader)); // Construye el subárbol "sí"
        node.setNo(buildTree(reader)); // Construye el subárbol "no"
        return node;
    }

    // Método para iniciar el proceso de preguntas con un número máximo de preguntas
    public List<String> startAsking(int maxQuestions) {
        Scanner scanner = new Scanner(System.in);
        TreeNode currentNode = root;
        List<String> posiblesAnimales = new ArrayList<>();
        int questionsAsked = 0;

        while (!isLeaf(currentNode) && questionsAsked < maxQuestions) {
            System.out.println(currentNode.getPregunta());
            String answer = scanner.nextLine().trim().toLowerCase();

            if (answer.equals("si") || answer.equals("yes")) {
                currentNode = currentNode.getSi();
            } else if (answer.equals("no")) {
                currentNode = currentNode.getNo();
            } else {
                System.out.println("Respuesta no válida. Por favor, responda 'si' o 'no'.");
                continue; // No aumentar el contador si la respuesta no es válida
            }

            questionsAsked++;
        }

        if (isLeaf(currentNode)) {
            posiblesAnimales.add(currentNode.getPregunta());
        } else {
            posiblesAnimales = getPossibleAnimals(currentNode);
        }

        scanner.close();
        return posiblesAnimales;
    }

    // Obtener todos los animales posibles a partir del nodo actual
    public List<String> getPossibleAnimals(TreeNode node) {
        List<String> animals = new ArrayList<>();

        if (node == null) {
            return animals;
        }

        if (isLeaf(node)) {
            if (node.getAnimales() != null && !node.getAnimales().isEmpty()) {
                animals.addAll(node.getAnimales());
            } else {
                animals.add(node.getPregunta());
            }
        } else {
            if (node.getSi() != null) {
                animals.addAll(getPossibleAnimals(node.getSi()));
            }
            if (node.getNo() != null) {
                animals.addAll(getPossibleAnimals(node.getNo()));
            }
        }

        return animals;
    }

    public TreeNode getRoot() {
        return this.root;
    }

    public void loadTreeFromQuestionsAndAnimals(List<String> questions, List<Animal> animals) {
        root = buildTree(questions, animals, 0);  // Empezamos desde la primera pregunta (índice 0)
    }

    private TreeNode buildTree(List<String> questions, List<Animal> animals, int questionIndex) {
        if (questionIndex >= questions.size() || animals.isEmpty()) {
            // Crear un nodo hoja y añadir los nombres de los animales
            TreeNode leafNode = new TreeNode("");
            List<String> nombresAnimales = new ArrayList<>();
            for (Animal animal : animals) {
                nombresAnimales.add(animal.getName());
            }
            leafNode.setAnimales(nombresAnimales);
            return leafNode;
        }

        String currentQuestion = questions.get(questionIndex);
        TreeNode node = new TreeNode(currentQuestion);

        List<Animal> yesAnimals = filterAnimals(animals, true, questionIndex);
        List<Animal> noAnimals = filterAnimals(animals, false, questionIndex);

        if (!yesAnimals.isEmpty()) {
            node.setSi(buildTree(questions, yesAnimals, questionIndex + 1));
        }
        if (!noAnimals.isEmpty()) {
            node.setNo(buildTree(questions, noAnimals, questionIndex + 1));
        }

        return node;
    }

    private List<Animal> filterAnimals(List<Animal> animals, boolean yesResponse, int questionIndex) {
        List<Animal> filtered = new ArrayList<>();
        for (Animal animal : animals) {
            if (animal.getResponses().size() > questionIndex && animal.getResponses().get(questionIndex).equals(yesResponse ? "si" : "no")) {
                filtered.add(animal);
            }
        }
        System.out.println("Animales filtrados para respuesta " + (yesResponse ? "Sí" : "No") + ": " + filtered);
        return filtered;
    }
}

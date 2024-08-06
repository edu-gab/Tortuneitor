package com.mycompany.tortuneitor;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {
    
    private DecisionTree<String> decisionTree;

    @Override
    public void start(Stage stage) {
        
        createDecisionTree();
        
        Label questionLabel = new Label(decisionTree.getPregunta());
        Button yesButton = new Button("Si");
        Button noButton = new Button("No");
        
        yesButton.setOnAction(event -> handleAnswer(true, questionLabel, yesButton, noButton));
        noButton.setOnAction(event -> handleAnswer(false, questionLabel, yesButton, noButton));
        
        VBox layout = new VBox(20, questionLabel, yesButton, noButton);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        
        Scene scene = new Scene(layout, 640, 480);
        stage.setScene(scene);
        stage.setTitle("Árbol de Decisión");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
    
    private void createDecisionTree(){
        decisionTree = new DecisionTree<>("El animal tiene pelo?", true);
        decisionTree.getRoot().setSi(new DecisionTree<>("El animal da leche?", true));
        decisionTree.getRoot().setNo(new DecisionTree<>("El animal tiene plumas?", true));
        
        decisionTree.getRoot().getSi().getRoot().setSi(new DecisionTree<>("Es un ser humano", false));
        decisionTree.getRoot().getSi().getRoot().setNo(new DecisionTree<>("Es un mamifero", false));
        
        decisionTree.getRoot().getNo().getRoot().setSi(new DecisionTree<>("Es un ave", false));
        decisionTree.getRoot().getNo().getRoot().setNo(new DecisionTree<>("Es un reptil", false));
        
    }
    
    
    private void handleAnswer(boolean respuesta, Label questionLabel, Button yesButton, Button noButton){
        if(respuesta){
            decisionTree = decisionTree.getRoot().getSi();
        } else{
            decisionTree = decisionTree.getRoot().getNo();
        }
        
        if(decisionTree.isLeaf()){
            questionLabel.setText("Respuesta: " + decisionTree.getPregunta());
            yesButton.setVisible(false);
            noButton.setVisible(false);
            
        } else{
            questionLabel.setText(decisionTree.getPregunta());
        }
    }
}
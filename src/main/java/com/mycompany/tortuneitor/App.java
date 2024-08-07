package com.mycompany.tortuneitor;

import java.util.Optional;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {
    
    private DecisionTree<String> decisionTree;
    private int remainingQuestions;

    @Override
    public void start(Stage stage) {
        
        createDecisionTree();
        
        remainingQuestions = getNumber();
        
        Label questionLabel = new Label(decisionTree.getPregunta());
        Button yesButton = new Button("Si");
        Button noButton = new Button("No");
        
        yesButton.setOnAction(event -> handleAnswer(true, questionLabel, yesButton, noButton));
        noButton.setOnAction(event -> handleAnswer(false, questionLabel, yesButton, noButton));
        
        VBox vBox = new VBox(10, questionLabel, yesButton, noButton);
        vBox.setAlignment(javafx.geometry.Pos.CENTER); 
        
        StackPane stackPane = new StackPane(vBox); 
        stackPane.setAlignment(javafx.geometry.Pos.CENTER);
        
        Scene scene = new Scene(stackPane, 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
    
    private void createDecisionTree(){
        decisionTree = new DecisionTree<>("¿El animal tiene pelo?");
        decisionTree.getRoot().setSi(new DecisionTree<>("¿El animal da leche?"));
        decisionTree.getRoot().setNo(new DecisionTree<>("¿El animal tiene plumas?"));

        DecisionTree<String> leftTree = decisionTree.getRoot().getSi();
        leftTree.getRoot().setSi(new DecisionTree<>("¿El animal es un mamífero marino?"));
        leftTree.getRoot().setNo(new DecisionTree<>("¿El animal es un mamífero terrestre?"));

        leftTree.getRoot().getSi().getRoot().setSi(new DecisionTree<>("Es una ballena"));
        leftTree.getRoot().getSi().getRoot().setNo(new DecisionTree<>("Es un delfín"));

        leftTree.getRoot().getNo().getRoot().setSi(new DecisionTree<>("Es un ser humano"));
        leftTree.getRoot().getNo().getRoot().setNo(new DecisionTree<>("Es un mamífero terrestre común"));

        DecisionTree<String> rightTree = decisionTree.getRoot().getNo();
        rightTree.getRoot().setSi(new DecisionTree<>("¿El animal puede volar?"));
        rightTree.getRoot().setNo(new DecisionTree<>("¿El animal es un reptil?"));

        rightTree.getRoot().getSi().getRoot().setSi(new DecisionTree<>("Es un águila"));
        rightTree.getRoot().getSi().getRoot().setNo(new DecisionTree<>("Es un pingüino"));

        rightTree.getRoot().getNo().getRoot().setSi(new DecisionTree<>("Es una serpiente"));
        rightTree.getRoot().getNo().getRoot().setNo(new DecisionTree<>("Es un cocodrilo"));      
         
    }
    
    
    private void handleAnswer(boolean answer, Label questionLabel, Button yesButton, Button noButton){
        if(remainingQuestions <= 0){
            questionLabel.setText("Numero de preguntas alcanzado");
            showPossibleAnswers(questionLabel);
            yesButton.setVisible(false);
            noButton.setVisible(false);
            
            return;
        }
        
        if(answer){
            decisionTree = decisionTree.getRoot().getSi();
        } else{
            decisionTree = decisionTree.getRoot().getNo();
        }
        
        remainingQuestions--;
        
        if(decisionTree.isLeaf()){
            questionLabel.setText("Respuesta " + decisionTree.getPregunta());
            yesButton.setVisible(false);
            noButton.setVisible(false);
        } else{
            questionLabel.setText(decisionTree.getPregunta());
        }
        
        if(remainingQuestions <= 0 ){
            showPossibleAnswers(questionLabel);
            yesButton.setVisible(false);
            noButton.setVisible(false);
        }
    }
    
    private void showPossibleAnswers(Label questionLabel){
        StringBuilder possibleAnswers = new StringBuilder("Posibles Respuestas: \n");
        
        collectPossibleAnswers(decisionTree, possibleAnswers);
        questionLabel.setText(possibleAnswers.toString());
    }
    
    private void collectPossibleAnswers(DecisionTree<String> tree, StringBuilder answers){
        if(tree.isLeaf()){
            answers.append("- ").append(tree.getPregunta()).append("\n");
        } else{
            if(tree.getRoot().getSi() != null){
                collectPossibleAnswers(tree.getRoot().getSi(), answers);
            }
            
            if(tree.getRoot().getNo() != null){
                collectPossibleAnswers(tree.getRoot().getNo(), answers);
            }
        }
    }
    
    private int getNumber(){
        TextInputDialog dialog = new TextInputDialog();
        
        dialog.setTitle("Numero de Preguntas");
        dialog.setHeaderText("Ingrese el numero de preguntas que desea responder");
        dialog.setContentText("Numero de preguntas: ");
        
        Optional<String> result;
        int numberQuestions = -1;
        
        while(numberQuestions < 1){
            result = dialog.showAndWait();
            if(result.isPresent()){
                try{
                    numberQuestions = Integer.parseInt(result.get());
                    
                    if(numberQuestions < 1){
                        showError("Por favor, ingrese un numero mayor que 0.");
                        numberQuestions = -1;
                    }
                } catch(NumberFormatException e){
                    showError("Por favor, ingrese un numero valido");
                }
            } else{
                showError("Debe ingresar un valor");
            }
        }
        
        return numberQuestions;
    }
    
    private void showError(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
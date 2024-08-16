package com.mycompany.tortuneitorpro;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SecondaryController {

    @FXML
    private Label saludoLabel;

    @FXML
    private Label fotoLabel;

    public void setSaludo(String nombre) {
        saludoLabel.setText("Hola " + nombre + "! Bienvenid@");
    }

    @FXML
    private void regresar() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    private void subirArchivo() throws IOException {
        App.setRoot("subirArchivos"); // Cambia a la nueva ventana con los cuatro botones
    }

   @FXML
    private void jugar() throws IOException {
        // Usar el archivo de preguntas para contar el número de preguntas y el archivo de animales para mostrar la lista de animales
        int numPreguntas = solicitarNumeroPreguntas("src/main/resources/Archivos/preguntas.txt", "src/main/resources/Archivos/animales.txt");

        if (numPreguntas > 0) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Juego.fxml"));
            Parent root = loader.load();

            GameController controller = loader.getController();
            controller.startGame(numPreguntas);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }


    private int solicitarNumeroPreguntas(String preguntasPath, String animalesPath) {
        List<String> posiblesAnimales = obtenerPosiblesAnimales(animalesPath);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("pregunta_y_animales.fxml"));
            Parent root = loader.load();

            PreguntaYAnimalesController controller = loader.getController();
            int maxPreguntas = contarPreguntasDisponibles(preguntasPath);
            controller.setMaxPreguntas(maxPreguntas);
            controller.setPosiblesAnimales(posiblesAnimales);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Número de Preguntas");
            stage.showAndWait();

            Object result = stage.getUserData();
            if (result != null && result instanceof Integer) {
                return (int) result;
            }

        } catch (IOException e) {
            mostrarError("No se pudo abrir la ventana de preguntas.");
        }
        return -1;
    }



    private void mostrarError(String mensaje) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error de Validación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private int contarPreguntasDisponibles(String path) {
        int count = 0;
        try {
            Path archivoPreguntas = Paths.get(path);
            try (BufferedReader reader = Files.newBufferedReader(archivoPreguntas)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();  // Añade esta línea
                    if (!line.isEmpty()) {  // Añade esta línea
                        count++;
                    }
                }
            }
        } catch (IOException e) {
            mostrarError("No se pudo leer el archivo de preguntas.");
        }
        return count;
    }

    
    private List<String> obtenerPosiblesAnimales(String filePath) {
    List<String> animales = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(" ");
            if (parts.length > 0) {
                // Asegúrate de tomar el primer elemento como el nombre del animal
                animales.add(parts[0].trim());
            }
        }
    } catch (IOException e) {
        mostrarError("Error al cargar la lista de posibles animales.");
    }
    return animales;
}


    
}

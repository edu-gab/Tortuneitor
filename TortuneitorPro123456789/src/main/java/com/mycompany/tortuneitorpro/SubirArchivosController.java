/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tortuneitorpro;


import java.io.BufferedReader;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TextInputDialog;

public class SubirArchivosController {

    @FXML
    private void subirPreguntas() {
        subirArchivo(".txt", "Subir Preguntas", "preguntas.txt");
    }

    @FXML
    private void subirAnimales() {
        subirArchivo(".txt", "Subir Animales", "animales.txt");
    }

    private void subirArchivo(String extension, String title, String nombreFinal) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de Texto", "*" + extension));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            try {
                // Ruta donde se guardarán los archivos en el paquete Archivos.Usuario
                Path destino = Paths.get("src/main/resources/Usuario/" + nombreFinal);
                // Reemplazar el archivo existente
                Files.copy(file.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Archivo Subido");
                alert.setHeaderText(null);
                alert.setContentText("Archivo subido exitosamente como " + nombreFinal);
                alert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Hubo un error al subir el archivo.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No se seleccionó archivo");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, seleccione un archivo " + extension);
            alert.showAndWait();
        }
    }

    @FXML
    private void continuar() throws IOException {
        // Usar el archivo de preguntas personalizado y el archivo de animales personalizado
        int numPreguntas = solicitarNumeroPreguntas("src/main/resources/Usuario/preguntas.txt", "src/main/resources/Usuario/animales.txt");

        if (numPreguntas > 0) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Juego1.fxml"));
            Parent root = loader.load();

            GameControllerUsuario controller = loader.getController();
            controller.startGame(numPreguntas);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }



    @FXML
    private void regresar() throws IOException {
        App.setRoot("secondary"); // Regresa a la ventana anterior
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

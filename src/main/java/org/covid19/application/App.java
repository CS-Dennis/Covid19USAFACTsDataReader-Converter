package org.covid19.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;


public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/covid19/application/Main.fxml"));
        scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setMaxHeight(630);
        stage.setMaxWidth(800);
        stage.setMinHeight(320);
        stage.setMinWidth(460);
        stage.setTitle("US Covid 19 Data Reader and Exporter");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
package com.smarttask;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Ouvre login.fxml en premier
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/smarttask/view/login.fxml"));
        Scene scene = new Scene(loader.load(), 400, 500);
        stage.setTitle("SmartTask — Connexion");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
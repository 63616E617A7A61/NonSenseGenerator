package com.g63616e617a7a61.nonsensegenerator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Load the FXML file for the initial screen
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/initScreen/init-screen.fxml"));  
        Scene scene = new Scene(fxmlLoader.load());

        // Set the minimum height and width for the stage
        stage.setMinWidth(1000);
        stage.setMinHeight(650);

        // Other stage properties
        stage.setTitle("NonSense Generator");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}

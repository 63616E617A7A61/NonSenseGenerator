package com.g63616e617a7a61.nonsensegenerator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main JavaFX application class that launches the NonSense Generator UI.
 * It loads the initial FXML layout, sets stage properties, and displays the primary window.
 * 
 * @author Nicola Zillio
 */
public class App extends Application {

    /**
     * Called when the JavaFX application starts.
     * Loads the initial screen from FXML, configures the stage, and shows it.
     *
     * @param stage The primary stage for this application.
     * @throws Exception If loading the FXML file fails.
     */
    @Override
    public void start(Stage stage) throws Exception {
        // Load the FXML file for the initial screen
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/view/initScreen/init-screen.fxml"));  
        Scene scene = new Scene(fxmlLoader.load());

        // Set the minimum height and width for the stage
        stage.setMinWidth(1000);
        stage.setMinHeight(675);

        // Other stage properties
        stage.setTitle("NonSense Generator");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Main method serving as the entry point of the application.
     * Launches the JavaFX application lifecycle.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        launch();
    }

}

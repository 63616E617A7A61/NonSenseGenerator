package com.g63616e617a7a61.nonsensegenerator.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class InitController {
    @FXML
    private BorderPane main; 

    @FXML
    public void initialize() {
        // Unfocus TextField
        Platform.runLater(() -> main.requestFocus());

        main.setOnMousePressed(event -> {
            main.requestFocus(); 
        });
    }
}

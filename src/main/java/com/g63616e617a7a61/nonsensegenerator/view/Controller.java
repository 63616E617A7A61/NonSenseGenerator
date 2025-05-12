package com.g63616e617a7a61.nonsensegenerator.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class Controller {
    @FXML
    private BorderPane main; 

    @FXML 
    private TextField sentenceInput; 

    @FXML 
    private Button generateBtn; 

    @FXML
    public void initialize() {
        // Unfocus TextField
        Platform.runLater(() -> main.requestFocus());

        main.setOnMousePressed(event -> {
            main.requestFocus(); 
        });

        // Enter kew pressed in input sentence text field
        sentenceInput.setOnKeyPressed(event -> {
            if (event.getCode().getName().equals("Enter")) {
                // if sentenceInput is empty, do nothing
                if(sentenceInput.getText().isEmpty()) {
                    return;
                }   
                //generateSentence(sentenceInput.getText());
                // clear sentenceInput text field
                sentenceInput.clear();
                // unfocus TextField
                main.requestFocus();
            }
        });
        
        // generateBtn pressed
        generateBtn.setOnAction(event -> {
            // if sentenceInput is empty, do nothing
            if(sentenceInput.getText().isEmpty()) {
                return;
            }   
            //generateSentence(sentenceInput.getText());
            // clear sentenceInput text field
            sentenceInput.clear();
        });

    }

}

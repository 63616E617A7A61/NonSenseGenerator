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

        // Unfocus TextFiel 
        // Necessary to set focus on the main BorderPane and not on the TextField
        Platform.runLater(() -> main.requestFocus());
        
        // Set focus on the main BorderPane when clicked 
        // This is implemented to unfocus the TextField when the user clicks on the main BorderPane
        main.setOnMousePressed(event -> {
            main.requestFocus(); 
        });



        // ---------------------- INPUT TEXT FIELD AND GENERATE BUTTON ----------------------
        // if the user presses Enter while the TextField is focused
        sentenceInput.setOnKeyPressed(event -> {
            if (event.getCode().getName().equals("Enter")) {
                submitInputSentenceHandler();
            }
        });
        
        // generateBtn pressed
        generateBtn.setOnAction(event -> {
            submitInputSentenceHandler();
        });




    }




    /* Method to handle the input sentence when the user presses Enter 
    while the TextField is focused or when the generate button is pressed */
    public void submitInputSentenceHandler(){
        // check that the TextField is not empty
        if(!sentenceInput.getText().isEmpty()) {
            // TODO
        }   

        // clear sentenceInput text field
        sentenceInput.clear();
        // unfocus TextField
        main.requestFocus();
    }

}

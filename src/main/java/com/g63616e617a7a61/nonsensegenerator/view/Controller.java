// Controller class for the init-screen.fxml file

package com.g63616e617a7a61.nonsensegenerator.view;

import java.io.IOException;

import com.g63616e617a7a61.nonsensegenerator.view.components.sentenceCard.SentenceCardController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Controller {
    private static int sentenceCount = 0; // number of sentences generated

    @FXML
    private BorderPane main; 

    @FXML 
    private TextField sentenceInput; 

    @FXML 
    private Button generateBtn; 

    @FXML
    private VBox titleSect; 

    @FXML 
    private VBox sentenceCardSect;




    @FXML
    public void initialize() {

        // Hide the sentence card section at the beginning, it will be shown when a sentence is generated
        sentenceCardSect.setVisible(false);
        sentenceCardSect.setManaged(false);

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
            // remove the title section if visibile
            if (titleSect.isVisible()) {
                titleSect.setVisible(false);
                titleSect.setManaged(false);
                sentenceCardSect.setVisible(true);
                sentenceCardSect.setManaged(true);
            }

            // increment the sentence count
            sentenceCount++;

            // get the nonsense sentence generated
            String genSentence = "the black cat jumped over the lazy dog"; // DEBUG 

            // add the sentence card in the init screen
            try {
                addSentenceCard(sentenceCount, genSentence);
            } catch (IOException e) {
                e.printStackTrace();
            } 
        } else {
            // if the TextField is empty, show an error message
            System.out.println("Please enter a sentence.");
        }   

        // clear sentenceInput text field
        sentenceInput.clear();
        // unfocus TextField
        main.requestFocus();
    }



    /* Method that load a sentence card in the init screen when a new
       sentence is generated.*/
    public void addSentenceCard(int genSentenceCount, String genSentence) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/g63616e617a7a61/nonsensegenerator/view/components/sentenceCard/sentence-card.fxml"));
        HBox newSentenceCard = loader.load();
        SentenceCardController controller = loader.getController();
        controller.setContent(genSentenceCount, genSentence);;
        sentenceCardSect.getChildren().add(newSentenceCard);
    }

}

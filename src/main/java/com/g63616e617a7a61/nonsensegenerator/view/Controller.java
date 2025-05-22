package com.g63616e617a7a61.nonsensegenerator.view;

import java.io.IOException;

import com.g63616e617a7a61.nonsensegenerator.view.components.sentenceCard.SentenceCardController;
import com.g63616e617a7a61.nonsensegenerator.model.Template;
import com.g63616e617a7a61.nonsensegenerator.controller.SentenceController;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class Controller {
    private static int sentenceCount = 0; // number of sentences generated
    private static final int ANIMATION_DURATION_MS = 200; // animation duration in milliseconds

    simplenlg.features.Tense tense = simplenlg.features.Tense.PRESENT; // default tense
    Template template = null; // default template
    boolean save = false; // default save value

    @FXML
    private BorderPane main; 

    @FXML
    private VBox mainCenter;

    @FXML 
    private TextArea sentenceInput; 

    @FXML 
    private Button generateBtn; 

    @FXML
    private VBox inputSect;

    @FXML
    private VBox titleSect; 

    @FXML 
    private VBox sentenceCardSect;

    @FXML 
    private ScrollPane sentenceCardSectScroll; 

    @FXML
    private VBox inputField; 

    @FXML
    private ComboBox<simplenlg.features.Tense> tenseSelector; 

    @FXML 
    private ComboBox<String> templateSelector; 

    @FXML 
    private CheckBox saveCheckbox;


    @FXML
    public void initialize() {
        
        // ---------------------- INITIALIZATION ----------------------
        /*Force the rendering of the sentence card component in initialization 
        in order to avoid the delay when the component is first shown*/
        forceSentenceCardRendering();

        // Hide the sentence card section at the beginning, it will be shown when a sentence is generated
        sentenceCardSectScroll.setVisible(false);
        sentenceCardSectScroll.setManaged(false);

        // Unfocus TextFiel 
        // Necessary to set focus on the main BorderPane and not on the TextField
        Platform.runLater(() -> main.requestFocus());
        
        // Set focus on the main BorderPane when clicked 
        // This is implemented to unfocus the TextField when the user clicks on the main BorderPane
        main.setOnMousePressed(event -> {
            main.requestFocus(); 
        });

        // COMBOBOX 
        tenseSelector.getItems().addAll(simplenlg.features.Tense.PAST, simplenlg.features.Tense.PRESENT, simplenlg.features.Tense.FUTURE);
        tenseSelector.setValue(simplenlg.features.Tense.PRESENT); // set default value to present tense
        tenseSelector.setOnAction(event -> {
            tense = tenseSelector.getValue(); // get the selected value
        });

        SentenceController sc = new SentenceController(""); 
        templateSelector.getItems().add("Casual"); // add default value to the list
        templateSelector.getItems().addAll(sc.getTemplateList());
        templateSelector.setValue("Casual"); // set default value to "Select a template"
        templateSelector.setOnAction(event -> {
            String selectedTemplate = templateSelector.getValue();
            if (!selectedTemplate.equals("Casual")) {
                template = new Template(selectedTemplate); // get the selected value
            } else {
                template = null; // set to null if "Select a template" is selected
            }
        });

        // CHECKBOX
        saveCheckbox.setSelected(false); // set default value to false
        saveCheckbox.setOnAction(event -> {
            save = saveCheckbox.isSelected(); // get the selected value
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
                mainCenter.setAlignment(javafx.geometry.Pos.TOP_LEFT);
                titleSect.setVisible(false);
                titleSect.setManaged(false);
                sentenceCardSectScroll.setVisible(true);
                sentenceCardSectScroll.setManaged(true);
            }

            // increment the sentence count
            sentenceCount++;
            try {
                addSentenceCard(sentenceCount, sentenceInput.getText(), save, template, tense);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // clear sentenceInput text field
        sentenceInput.clear();

        // clear other options 
        templateSelector.setValue("Casual");
        template = null; // set to null if "Select a template" is selected
        tenseSelector.setValue(simplenlg.features.Tense.PRESENT); // set default value to present tense
        tense = simplenlg.features.Tense.PRESENT; // default tense
        saveCheckbox.setSelected(false); // set default value to false
        save = false; // default save value
        
        // unfocus TextField
        main.requestFocus();
    }




    
    // ---------------------- OTHER METHODS ----------------------
    /*SentenceCard rendering forcing
    This is done to avoid the delay when the component is first shown*/
    public void forceSentenceCardRendering(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/components/sentenceCard/sentence-card.fxml"));
            VBox dummyCard = loader.load();
            dummyCard.setVisible(false); 
            dummyCard.setManaged(false); 
            sentenceCardSect.getChildren().add(dummyCard); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    /* Method that load a sentence card in the init screen when a new
       sentence is generated. With a fadeIn animation*/
    public void addSentenceCard(int sentenceCount, String inputSentence, boolean save, Template template, simplenlg.features.Tense tense) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/components/sentenceCard/sentence-card.fxml"));
        VBox newSentenceCard = loader.load();
        SentenceCardController controller = loader.getController();

        controller.generateSentence(sentenceCount, inputSentence, save, template, tense);

        newSentenceCard.setOpacity(0);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(ANIMATION_DURATION_MS), newSentenceCard);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        sentenceCardSect.getChildren().add(newSentenceCard);

        ParallelTransition animation = new ParallelTransition(fadeIn);
        animation.play();
    }
}

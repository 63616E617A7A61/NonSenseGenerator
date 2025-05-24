package com.g63616e617a7a61.nonsensegenerator.view.components.sentenceCard;

import java.io.IOException;

import com.g63616e617a7a61.nonsensegenerator.controller.SentenceController;
import com.g63616e617a7a61.nonsensegenerator.view.components.syntaxTree.SyntaxTreeController;
import com.g63616e617a7a61.nonsensegenerator.model.Template;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Controller for the SentenceCard component.
 * Handles the display, sentence generation, and syntax tree logic for each card.
 */
public class SentenceCardController {

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML 
    private Region progressIndicatorSpacer; 
    
    @FXML 
    private Label sentenceCount;
    
    @FXML 
    private Label genSentence; 

    @FXML 
    VBox sentenceCard;

    @FXML
    private Button sentenceCardInfoBtn;
    
    @FXML 
    private Label toxicityRate; 

    
    // ------------------ VARIABLES ------------------
    private String generatedSentence = ""; // contains the generated sentence 
    private String inputSentence = ""; // contains the input sentence
    private boolean isSyntaxTreeVisible = false; // Flag to check if the syntax tree is already visible
    private VBox cachedSyntaxTree; // Load the syntax tree the first time, the next times use the cachedSyntaxTree
    private SentenceController sc; 



    // ------------------ INITIALIZATION ------------------
    /**
     * Initializes the controller.
     * Sets the action for the info button to toggle the syntax tree visibility.
     */
    @FXML
    public void initialize() {
        // ------------------ INFO BUTTON CLICK ------------------
        // When the info button is clicked, show the syntax tree
        sentenceCardInfoBtn.setOnAction(event -> {
            if(!isSyntaxTreeVisible) {
                addSyntaxTree();
                isSyntaxTreeVisible = true;
            } else {
                // If the syntax tree is already visible, remove it
                removeSyntaxTree();
                isSyntaxTreeVisible = false; 
            }
        });

        sentenceCardInfoBtn.setDisable(true); // Disable the info button by default
        sentenceCardInfoBtn.setOpacity(0.5); // Set the opacity to indicate it's disabled
    }




    // ------------------ METHODS ------------------
   /**
     * Updates the content of the sentence card with the given sentence data.
     *
     * @param count             the index number of the sentence.
     * @param generatedSentence the generated sentence text.
     * @param toxicity          the calculated toxicity percentage (0–100).
     */
    public void setContent(int count, String generatedSentence, double toxicity) {
        this.generatedSentence = generatedSentence;
        sentenceCount.setText("Sentence " + Integer.toString(count));
        genSentence.setText(generatedSentence);
        toxicityRate.setText(String.format("%.2f", toxicity) + "%");
    }





    /**
     * Loads and displays the syntax tree in the sentence card.
     * If already cached, uses the cached tree.
     * Shows a loading animation during the asynchronous load.
     */
    private void addSyntaxTree() {
        // Mostra l'animazione di caricamento
        FXMLLoader loadingAnimationLoader = new FXMLLoader(getClass().getResource("/view/components/syntaxTree/syntax-tree-loading.fxml"));

        try {
            Node loadingNode = loadingAnimationLoader.load();
            sentenceCard.getChildren().add(loadingNode);

            // Esegui il caricamento in un Task (thread separato)
            Task<Parent> loadSyntaxTreeTask = new Task<>() {
                @Override
                protected Parent call() throws Exception {
                    if (cachedSyntaxTree == null) {
                        FXMLLoader syntaxTreeLoader = new FXMLLoader(getClass().getResource("/view/components/syntaxTree/syntax-tree.fxml"));
                        VBox syntaxTree = syntaxTreeLoader.load();

                        SyntaxTreeController controller = syntaxTreeLoader.getController();
                        controller.generateTree(sc);

                        cachedSyntaxTree = syntaxTree;
                    }
                    return cachedSyntaxTree;
                }
            };

            loadSyntaxTreeTask.setOnSucceeded(event -> {
                sentenceCard.getChildren().remove(loadingNode);
                Region spacer = new Region();
                spacer.setPrefHeight(20);
                spacer.setId("SyntaxTreeSpacer");
                sentenceCard.getChildren().add(spacer);
                sentenceCard.getChildren().add(loadSyntaxTreeTask.getValue());
            });

            loadSyntaxTreeTask.setOnFailed(event -> {
                sentenceCard.getChildren().remove(loadingNode);
                Throwable e = loadSyntaxTreeTask.getException();
                e.printStackTrace();
                // Qui puoi anche mostrare un messaggio d'errore visivo
            });

            new Thread(loadSyntaxTreeTask).start(); // Avvia il thread
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





     /**
     * Removes the syntax tree view from the sentence card.
     */
    private void removeSyntaxTree() {
        sentenceCard.getChildren().remove(cachedSyntaxTree); 
        sentenceCard.getChildren().removeIf(node -> node.getId() != null && node.getId().equals("SyntaxTreeSpacer"));
    }
    




    /**
     * Asynchronously generates a new sentence and updates the sentence card UI.
     * Shows a loading indicator while generating.
     *
     * @param sentenceCount the index number of the sentence.
     * @param inputSentence the original input sentence.
     * @param save          flag indicating whether to save the sentence.
     * @param template      an optional sentence template (nullable).
     * @param tense         an optional verb tense for generation (nullable).
     */
    public void generateSentence(int sentenceCount, String inputSentence, boolean save, Template template, simplenlg.features.Tense tense) {
        this.inputSentence = inputSentence; 
        // Set Loading... when the sentence is generating 
        setContent(sentenceCount, "Loading...", 0);
        progressIndicator.setVisible(true);
        progressIndicator.setManaged(true);

        // Generation of the sentence process 
        Task<Object[]> generateSentenceTask = new Task<>() {
            @Override
            protected Object[] call() throws Exception {
                if(template == null && tense == null) {
                    sc = new SentenceController(inputSentence, save);
                } else if(template == null) {
                    sc = new SentenceController(inputSentence, save, tense);
                } else if(tense == null) {
                    sc = new SentenceController(inputSentence, save, new Template(SentenceController.getRawTemplate(template.getTemplate())));
                } else {
                    sc = new SentenceController(inputSentence, save, tense, new Template(SentenceController.getRawTemplate(template.getTemplate())));
                }
                return new Object[] {sc.getOutputSentence(), sc.getToxicity()*100};
            }
        }; 
        
        // When the generation is completed update the sentence card content
        generateSentenceTask.setOnSucceeded(event -> {
            String generatedText = (String) generateSentenceTask.getValue()[0];
            double toxicity = (double) generateSentenceTask.getValue()[1];
            setContent(sentenceCount, generatedText, toxicity);
            progressIndicatorSpacer.setPrefWidth(0);
            progressIndicator.setVisible(false);
            progressIndicator.setManaged(false);
            sentenceCardInfoBtn.setDisable(false); // Enable the info button
            sentenceCardInfoBtn.setOpacity(1.0); // Set the opacity to indicate it's enabled
        });

        // Handle errors 
        generateSentenceTask.setOnFailed(event -> {
            setContent(sentenceCount, "An error occurred!", Double.NaN);
            progressIndicatorSpacer.setPrefWidth(0);
            progressIndicator.setVisible(false);
            progressIndicator.setManaged(false);
        });
        
        // Start the generation task in a new thread
        Thread thread = new Thread(generateSentenceTask);
        thread.setDaemon(true);
        thread.start();
    }

}
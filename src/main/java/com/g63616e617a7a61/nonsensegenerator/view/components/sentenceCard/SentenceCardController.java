package com.g63616e617a7a61.nonsensegenerator.view.components.sentenceCard;

import java.io.IOException;

import com.g63616e617a7a61.nonsensegenerator.model.InputSentence;
import com.g63616e617a7a61.nonsensegenerator.model.OutputSentence;
import com.g63616e617a7a61.nonsensegenerator.view.components.syntaxTree.SyntaxTreeController;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;


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
    
    // ------------------ VARIABLES ------------------
    private String generatedSentence = ""; // contains the generated sentence 
    private String inputSentence = ""; // contains the input sentence
    private boolean isSyntaxTreeVisible = false; // Flag to check if the syntax tree is already visible
    private ScrollPane cachedSyntaxTree; // Load the syntax tree the first time, the next times use the cachedSyntaxTree




    // ------------------ INITIALIZATION ------------------
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
    }




    // ------------------ METHODS ------------------
    // When this method is caalled, it sets the content of the sentence card (or updates it)
    public void setContent(int count, String generatedSentence) {
        this.generatedSentence = generatedSentence;
        sentenceCount.setText("Sentence " + Integer.toString(count));
        genSentence.setText(generatedSentence);
    }





    /* When the user clicks the info button, this method is called 
     * Then it loads the syntax tree from the FXML file and adds it to the sentence card
     * This is the steps: 
     * 1. Load the syntax tree from the FXML file
     * 2. Generate the syntax tree using the input sentence (if the syntax tree is not already generated)
     * 3. While the syntax tree is loading, show the loading animation
    */
    private void addSyntaxTree() {
        // Mostra l'animazione di caricamento
        FXMLLoader loadingAnimationLoader = new FXMLLoader(getClass().getResource(
            "/com/g63616e617a7a61/nonsensegenerator/view/components/syntaxTree/syntax-tree-loading.fxml"
        ));

        try {
            Node loadingNode = loadingAnimationLoader.load();
            sentenceCard.getChildren().add(loadingNode);

            // Esegui il caricamento in un Task (thread separato)
            Task<Parent> loadSyntaxTreeTask = new Task<>() {
                @Override
                protected Parent call() throws Exception {
                    if (cachedSyntaxTree == null) {
                        FXMLLoader syntaxTreeLoader = new FXMLLoader(getClass().getResource(
                            "/com/g63616e617a7a61/nonsensegenerator/view/components/syntaxTree/syntax-tree.fxml"
                        ));
                        ScrollPane syntaxTree = syntaxTreeLoader.load();

                        SyntaxTreeController controller = syntaxTreeLoader.getController();
                        controller.generateTree(inputSentence);

                        cachedSyntaxTree = syntaxTree;
                    }
                    return cachedSyntaxTree;
                }
            };

            loadSyntaxTreeTask.setOnSucceeded(event -> {
                sentenceCard.getChildren().remove(loadingNode);
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





    // Remove syntax tree from sentence card
    private void removeSyntaxTree() {
        sentenceCard.getChildren().remove(cachedSyntaxTree); 
    }
    




    /* Generation of the sentence (this method is called from init screen for creating a new sentence card) 
     * This is the steps:
     * 1. Set the content of the sentence card to "Loading..." (and show the progress indicator)
     * 2. Create a new task to generate the sentence (this is done in a separate thread to avoid blocking the UI)
     * 3. When the task is completed (setOnSucceeded()), update the content of the sentence card with the generated sentence
     * 4. If an error occurs (setOnFailed()), set the content of the sentence card to "An error occurred!"
    */
    public void generateSentence(int sentenceCount, String inputSentence){
        this.inputSentence = inputSentence; 
        // Set Loading... when the sentence is generating 
        setContent(sentenceCount, "Loading...");
        progressIndicator.setVisible(true);
        progressIndicator.setManaged(true);

        // Generation of the sentence process 
        /*Task<String> generateSentenceTask = new Task<>() {
            @Override
            protected String call() throws Exception {
                OutputSentence genSentence = new OutputSentence(new InputSentence(inputSentence)); // USARE FRASECONTROLLER
                return genSentence.toString();
            }
        }; 
        
        // When the generation is completed update the sentence card content
        generateSentenceTask.setOnSucceeded(event -> {
            String generatedText = generateSentenceTask.getValue();
            setContent(sentenceCount, generatedText);
            progressIndicatorSpacer.setPrefWidth(0);
            progressIndicator.setVisible(false);
            progressIndicator.setManaged(false);
        });

        // Handle errors 
        generateSentenceTask.setOnFailed(event -> {
            setContent(sentenceCount, "An error occurred!");
            progressIndicatorSpacer.setPrefWidth(0);
            progressIndicator.setVisible(false);
            progressIndicator.setManaged(false);
        });
        
        // Start the generation task in a new thread
        Thread thread = new Thread(generateSentenceTask);
        thread.setDaemon(true);
        thread.start();*/
    }

}
package com.g63616e617a7a61.nonsensegenerator.view.components.sentenceCard;

import java.io.IOException;

import com.g63616e617a7a61.nonsensegenerator.model.InputSentence;
import com.g63616e617a7a61.nonsensegenerator.model.OutputSentence;
import com.g63616e617a7a61.nonsensegenerator.view.components.syntaxTree.SyntaxTreeController;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
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
    
    private String outputSentence = ""; 
    private String inputSentence = "";
    private boolean isSyntaxTreeVisible = false; // Flag to check if the syntax tree is already visible
    private Pane cachedSyntaxTree; // Load the syntax tree the first time, the next times use the cachedSyntaxTree

    @FXML
    public void initialize() {
        // ------------------ BUTTON CLICK ------------------
        sentenceCardInfoBtn.setOnAction(event -> {
            if(!isSyntaxTreeVisible) {
                try {
                    addSyntaxTree();
                    isSyntaxTreeVisible = true; // Set the flag to true after adding the syntax tree
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // If the syntax tree is already visible, remove it
                removeSyntaxTree();
            }
        });
    }




    public void setContent(int count, String outputSentence) {
        this.outputSentence = outputSentence;
        sentenceCount.setText("Sentence " + Integer.toString(count));
        genSentence.setText(outputSentence);
    }




    // Add syntax tree to sentence card 
    private void addSyntaxTree() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/g63616e617a7a61/nonsensegenerator/view/components/syntaxTree/syntax-tree.fxml"));
            if(cachedSyntaxTree == null){
                cachedSyntaxTree = loader.load();
                SyntaxTreeController controller = loader.getController();
                controller.generateTree(inputSentence);
            }
            sentenceCard.getChildren().add(cachedSyntaxTree);
    }

    // Remove syntax tree from sentence card
    private void removeSyntaxTree() {
        sentenceCard.getChildren().remove(cachedSyntaxTree); 
        isSyntaxTreeVisible = false; // Reset the flag
    }
    
    // Generation of the sentence
    public void generateSentence(int sentenceCount, String inputSentence){
        this.inputSentence = inputSentence; 
        // Set Loading... when the sentence is generating 
        setContent(sentenceCount, "Loading...");
        progressIndicatorSpacer.setPrefWidth(10);
        progressIndicator.setVisible(true);
        progressIndicator.setManaged(true);

        // Generation of the sentence process 
        Task<String> generateSentenceTask = new Task<>() {
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

        // Start the generation
        Thread thread = new Thread(generateSentenceTask);
        thread.setDaemon(true);
        thread.start();
    }

}
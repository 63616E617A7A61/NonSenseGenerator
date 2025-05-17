package com.g63616e617a7a61.nonsensegenerator.view.components.sentenceCard;

import java.io.IOException;

import com.g63616e617a7a61.nonsensegenerator.Model.InputSentence;
import com.g63616e617a7a61.nonsensegenerator.Model.OutputSentence;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
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

    private String sentence = ""; 
    private boolean isSyntaxTreeVisible = false; // Flag to check if the syntax tree is already visible

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

    public void setContent(int count, String sentence) {
        this.sentence = sentence;
        sentenceCount.setText("Sentence " + Integer.toString(count));
        genSentence.setText(sentence);
    }




    // Add syntax tree to sentence card 
    private void addSyntaxTree() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/g63616e617a7a61/nonsensegenerator/view/components/syntaxTree/syntax-tree.fxml"));
            StackPane syntaxTree = loader.load();
            syntaxTree.setId("syntaxTree");
            //SyntaxTreeController controller = loader.getController();
            //controller.generateTree(sentence);
            sentenceCard.getChildren().add(syntaxTree);
    }

    //  Remove syntax tree from sentence card
    private void removeSyntaxTree() {
        sentenceCard.getChildren().removeIf(node -> node.getId() != null && node.getId().equals("syntaxTree"));
        isSyntaxTreeVisible = false; // Reset the flag
    }
    
    // Generation of the sentence
    public void generateSentence(int sentenceCount, String inputSentence){
        //setContent(sentenceCount, inputSentence); // PER EVITARE DI USARE LA API 
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
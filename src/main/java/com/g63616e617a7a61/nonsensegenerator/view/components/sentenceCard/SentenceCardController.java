package com.g63616e617a7a61.nonsensegenerator.view.components.sentenceCard;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class SentenceCardController {
    
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

}
package com.g63616e617a7a61.nonsensegenerator.view.components.sentenceCard;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class SentenceCardController {
    
    @FXML 
    private Label sentenceCount; 

    @FXML 
    private Label genSentence; 

    @FXML HBox sentenceCard;

    @FXML
    public void initialize() {
        
    }

    public void setContent(int count, String sentence) {
        sentenceCount.setText("Sentence " + Integer.toString(count));
        genSentence.setText(sentence);
    }

}

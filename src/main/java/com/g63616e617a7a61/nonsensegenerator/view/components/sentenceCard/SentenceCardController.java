package com.g63616e617a7a61.nonsensegenerator.view.components.sentenceCard;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SentenceCardController {
    
    @FXML 
    private Label sentenceCount; 

    @FXML 
    private Label genSentence; 

    @FXML
    public void initialize() {
        
    }

    public void setContent(int count, String sentence) {
        sentenceCount.setText("Sentence " + Integer.toString(count));
        genSentence.setText(sentence);
    }

}

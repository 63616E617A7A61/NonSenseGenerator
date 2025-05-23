package com.g63616e617a7a61.nonsensegenerator.model;

import com.g63616e617a7a61.nonsensegenerator.controller.ApiController;
import java.io.IOException;
import java.util.List;

/**
 * Represents an input sentence containing its syntactic components.
 * This class extracts and stores the grammatical elements (nouns, verbs, adjectives)
 * from the input text and provides access to its syntax tree structure.
 * 
 * @author Enrico Giacomel
 */

public class InputSentence {
    private Syntagm[] syntagms;
    private String value;

    /**
     * Constructs an InputSentence from a string without saving extracted words.
     * @param s The input sentence text to analyze
     * @throws RuntimeException if there's an error analyzing the sentence syntax
     */
    public InputSentence(String s) {
        value = s;
        try {
            syntagms = ApiController.extract(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructs an InputSentence with option to save extracted words to vocabulary.
     * @param s The input sentence text to analyze
     * @param save If true, saves extracted words to their respective vocabularies
     * @throws RuntimeException if there's an error analyzing the sentence syntax
     */
    public InputSentence(String s, boolean save) {
        value = s;
        try {
            syntagms = ApiController.extract(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(save){
            for(Syntagm syn : syntagms){
                if(syn instanceof Verb){
                    Verb.save(syn.getValue());
                }else if(syn instanceof Noun){
                    Noun.save(syn.getValue());
                }else{
                    Adjective.save(syn.getValue());
                }
            }
        }
    }

    /**
     * Gets the array of syntactic elements extracted from this sentence.
     * @return Array of Syntagm objects (nouns, verbs, adjectives)
     */
    public Syntagm[] extract(){
        return syntagms;
    }

    /**
     * Generates and returns the syntax tree structure of this sentence.
     * @return List of SyntaxElement objects representing the syntax tree
     * @throws RuntimeException if there's an error generating the syntax tree
     */
    public List<SyntaxElement> getSyntaxTree(){
        List<SyntaxElement> seList = null;
        try {
            seList = ApiController.getSyntaxTree(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return seList;
    }

    /**
     * Returns the original string value of this sentence.
     * @return The original sentence text
     */
    @Override
    public String toString() {
        return value;
    }
}

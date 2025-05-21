package com.g63616e617a7a61.nonsensegenerator.model;

import com.g63616e617a7a61.nonsensegenerator.controller.ApiController;
import java.io.IOException;
import java.util.List;

public class InputSentence {
    private Syntagm[] syntagms;
    private String value;

    public InputSentence(String s) {
        value = s;
        try {
            syntagms = ApiController.extract(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Syntagm[] extract(){
        return syntagms;
    }

    public List<SyntaxElement> getSyntaxTree(){
        List<SyntaxElement> seList = null;
        try {
            seList = ApiController.getSyntaxTree(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return seList;
    }

    @Override
    public String toString() {
        return value;
    }
}

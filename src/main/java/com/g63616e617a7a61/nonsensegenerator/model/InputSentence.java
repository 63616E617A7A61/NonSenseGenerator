package com.g63616e617a7a61.nonsensegenerator.model;

import com.g63616e617a7a61.nonsensegenerator.controller.ApiController;
import java.io.IOException;
import java.util.List;

public class InputSentence {
    private String value;
    private Syntagm[] syntagms;

    public InputSentence(String s) {
        value = s;
        syntagms = extract();
    }

    public Syntagm[] extract(){
        try {
            return ApiController.extract(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getSyntaxTree(){
        try {
            List<SyntaxElement> seList = ApiController.getSyntaxTree(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "";
    }

    public String toString() {
        return value;
    }
}

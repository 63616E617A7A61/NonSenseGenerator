package com.g63616e617a7a61.nonsensegenerator.controller;

import com.g63616e617a7a61.nonsensegenerator.model.InputSentence;
import com.g63616e617a7a61.nonsensegenerator.model.OutputSentence;

public class SentenceController {
    private InputSentence in;
    private OutputSentence out;

    public SentenceController(String s){
        in = new InputSentence(s);
        out = new OutputSentence(in);
    }

    public String getOutputSentence() {
        return out.toString();
    }
    public String getSyntaxTree() {
        return in.getSyntaxTree();
    }
    public double getToxicity() {
        return out.getToxicity();
    }
}

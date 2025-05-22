package com.g63616e617a7a61.nonsensegenerator.controller;

import java.util.ArrayList;
import java.util.List;

import com.g63616e617a7a61.nonsensegenerator.model.InputSentence;
import com.g63616e617a7a61.nonsensegenerator.model.OutputSentence;
import com.g63616e617a7a61.nonsensegenerator.model.SyntaxElement;
import com.g63616e617a7a61.nonsensegenerator.model.Template;

public class SentenceController {
    private InputSentence in;
    private OutputSentence out;

    public SentenceController(String s){
        in = new InputSentence(s);
        out = new OutputSentence(in);
    }

    public SentenceController(String s, boolean flag){
        in = new InputSentence(s, flag);
        out = new OutputSentence(in);
    }

    public String getOutputSentence() {
        return out.toString();
    }
    
    public List<SyntaxElement> getSyntaxTree() {
        return in.getSyntaxTree();
    }

    public double getToxicity() {
        return out.getToxicity();
    }

    public ArrayList<String> getTemplateList(){
        ArrayList<String> out = Template.getTemplates();
        // Template.getTemplates();
        for(String i : out){
            int index = 0;
            while (true) {
                index = i.indexOf("%", index);
                if (index == -1) {
                    break; 
                }
                String substring = i.substring(index, index+3);
                String word = "";
                switch (substring) {
                    case "%na":
                        word = "\\'name\\'";
                        break;
                    case "%ad":
                        word = "\\'adjective\\'";
                        break;
                    case "%np":
                        word = "\\'plural name\\'";
                        break;
                    case "%ve":
                        word = "\\'verb\\'";
                        break;
                    default:
                        word = substring;
                        break;
                }
                i = i.replaceFirst(substring, word);
                index += substring.length();
            }  
        } 
        return out;
    }
}

package com.g63616e617a7a61.nonsensegenerator.controller;

import java.util.ArrayList;
import java.util.List;

import com.g63616e617a7a61.nonsensegenerator.model.InputSentence;
import com.g63616e617a7a61.nonsensegenerator.model.OutputSentence;
import com.g63616e617a7a61.nonsensegenerator.model.SyntaxElement;
import com.g63616e617a7a61.nonsensegenerator.model.Template;

import simplenlg.features.Tense;

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

    public SentenceController(String s, boolean flag, Tense t){
        in = new InputSentence(s, flag);
        out = new OutputSentence(in, t);
    }

    public SentenceController(String s, boolean flag, Template tmpl){
        in = new InputSentence(s, flag);
        out = new OutputSentence(in, tmpl);
    }

    public SentenceController(String s, boolean flag, Tense t, Template tmpl){
        in = new InputSentence(s, flag);
        out = new OutputSentence(in, tmpl, t);
    }

    public SentenceController(String s, Tense t, Template tmpl){
        in = new InputSentence(s);
        out = new OutputSentence(in, tmpl, t);
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
        ArrayList<String> out = new ArrayList<>();
        for(String i : Template.getTemplates()){
            int index = 0;
            String nt = i;
            while (true) {
                index = nt.indexOf("%", index);
                if (index == -1) {
                    break; 
                }
                String substring = nt.substring(index, index+3);
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
                nt = nt.replaceFirst(substring, word);
                index += substring.length();
            }  
            out.add(nt);
        } 
        return out;
    }

    public String getRawTemplate(String input) {
        String nt = input;
        int index = 0;
        while (true) {
            index = nt.indexOf("'", index);
            if (index == -1 || index + 1 >= nt.length()) {
                break;
            }
            int end = nt.indexOf("'", index + 1);
            if (end == -1) {
                break;
            }
            String substring = nt.substring(index, end + 1);
            String word = "";
            switch (substring) {
                case "'name'":
                    word = "%na";
                    break;
                case "'adjective'":
                    word = "%ad";
                    break;
                case "'plural name'":
                    word = "%np";
                    break;
                case "'verb'":
                    word = "%ve";
                    break;
                default:
                    word = substring;
                    break;
            }
            nt = nt.replaceFirst(java.util.regex.Pattern.quote(substring), word);
            index += word.length();
        }
        return nt;
    }
}

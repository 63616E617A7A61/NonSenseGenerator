package com.g63616e617a7a61.nonsensegenerator.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import org.atteo.evo.inflector.English;
import com.g63616e617a7a61.nonsensegenerator.controller.ApiController;

public class OutputSentence {
    private String value;
    private Template t;
    private double toxicity;

    public OutputSentence(InputSentence in){
        t = new Template();
        toxicity = 0;
        value = "";
        generate(in);
    }

    public OutputSentence(InputSentence in, Template t){
        this.t = t;
        toxicity = 0;
        value = "";
        generate(in);
    }

    private void generate(InputSentence in){
        ArrayList<Verb> v = new ArrayList<>();
        ArrayList<Noun> n = new ArrayList<>();
        ArrayList<Adjective> a = new ArrayList<>();
        for (Syntagm s : in.extract()) {
            if (s instanceof Verb) {
                v.add((Verb) s);
            } else if(s instanceof Noun) {
                n.add((Noun) s);
            }else{
                a.add((Adjective) s);
            }
        }
        // ho diviso le varie parti sintattiche che ci interessano

        value = t.getTemplate(); // copio il template nella frase di output

        int diff = countOccurrences("%v") - v.size();
        if(diff > 0){
            for (int i = 0; i < diff; i++) {
                v.add(new Verb());
            }
        }
        diff = countOccurrences("%n") - n.size();
        if(diff > 0){
            for (int i = 0; i < diff; i++) {
                n.add(new Noun());
            }
        }
        diff = countOccurrences("%a") - a.size();
        if(diff > 0){
            for (int i = 0; i < diff; i++) {
                a.add(new Adjective());
            }
        }
        // ho controllato se servivano altri termini se non ne abbiamo abbastanza li ho pescati dal dizionario
        
        // faccio shuffle degli arraylist prima di riempire il template
        Collections.shuffle(n);
        Collections.shuffle(v);
        Collections.shuffle(a);

        int index = 0;

        while (true) {
            index = value.indexOf("%", index);
            if (index == -1) {
                break; 
            }
            String substring = value.substring(index, index+3);
            String word = "";
            switch (substring) {
                case "%n ":
                    word = English.plural(n.removeFirst().toString(), 1);
                    break;
                case "%v ":
                    word = v.removeFirst().toString();
                    break;
                case "%a ":
                    word = a.removeFirst().toString();
                    break;
                case "%ns":
                    word = English.plural(n.removeFirst().toString());
                    break;
                default:
                    break;
            }
            value = value.replaceFirst(substring, word);
            index += substring.length();
        }
        // cerco e sostituisco tutti i placeholder
        try {
            toxicity = ApiController.getToxicity(value);
        } catch (IOException e) {
            toxicity = 0;
        }
    }

    private int countOccurrences(String substring){
        if (value == null || substring == null || substring.isEmpty()) {
            return 0;
        }

        int count = 0;
        int index = 0;

        while (true) {
            index = value.indexOf(substring, index);
            if (index == -1) {
                break; 
            }
            count++;
            index += substring.length();
        }

        return count;
    }

    public double getToxicity(){
        return toxicity;
    }

    public String toJson(){
        return "{"+value+"}";
    }

    public String toString(){
        return value;
    }
}
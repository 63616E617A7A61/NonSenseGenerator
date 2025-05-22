package com.g63616e617a7a61.nonsensegenerator.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import org.atteo.evo.inflector.English;
import com.g63616e617a7a61.nonsensegenerator.controller.ApiController;

import simplenlg.features.Feature;
import simplenlg.features.NumberAgreement;
import simplenlg.features.Person;
import simplenlg.features.Tense;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.english.Realiser;

public class OutputSentence {
    private String value;
    private Template t;
    private double toxicity;
    private Tense tense;

    public OutputSentence(InputSentence in){
        t = new Template();
        toxicity = 0;
        value = "";
        tense = Tense.PRESENT;
        generate(in);
    }

    public OutputSentence(InputSentence in, Template t){
        this.t = t;
        toxicity = 0;
        value = "";
        tense = Tense.PRESENT;
        generate(in);
    }

    public OutputSentence(InputSentence in, Tense t){
        this.t = new Template();
        toxicity = 0;
        value = "";
        tense = t;
        generate(in);
    }

    public OutputSentence(InputSentence in, Template t, Tense tense){
        this.t = t;
        toxicity = 0;
        value = "";
        this.tense = tense;
        generate(in);
    }

    public static void main(String[] args) {
        OutputSentence o = new OutputSentence(new InputSentence("Tom"), new Template("The %ad %np %ve but the %ad %na %ve"), Tense.FUTURE);
        System.out.println(o.toString());
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

        int diff = countOccurrences("%ve") - v.size();
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
        diff = countOccurrences("%ad") - a.size();
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
                case "%na":
                    word = English.plural(n.removeFirst().toString(), 1);
                    break;
                case "%ad":
                    word = a.removeFirst().toString();
                    break;
                case "%np":
                    String tmp = n.removeFirst().toString();
                    while (Character.isUpperCase(tmp.charAt(0))) {  //finchè è un nome proprio
                        tmp = new Noun().getValue();                //cerco di generare un nome che possa essere pluralizzato
                    }
                    word = English.plural(tmp);
                    
                    break;
                default:
                    word = substring;
                    break;
            }
            value = value.replaceFirst(substring, word);
            index += substring.length();
        }
        // cerco e sostituisco tutti i placeholder di nomi e aggettivi

        while (true) {
            index = value.indexOf("%", index);
            if (index == -1) {
                break; 
            }
            String substring = value.substring(index, index+3);
            String word = v.removeFirst().toString();
            value = value.replaceFirst(substring, word);
            SyntaxElement subj;
            try {
                subj = ApiController.getSubject(value, word);
            } catch (IOException e) {
                subj = null;
            }

            value = value.replaceFirst(word, changeTense(subj, word));
            
            index += substring.length();
        }
        // cerco e sostituisco tutti i verbi correggendone il tempo verbale
        try {
            toxicity = ApiController.getToxicity(value);
        } catch (IOException e) {
            toxicity = 0;
        }
    }

    private String changeTense(SyntaxElement subj, String verb){
        Realiser realiser = new Realiser(Lexicon.getDefaultLexicon());

        SPhraseSpec sentence = new NLGFactory(Lexicon.getDefaultLexicon()).createClause();
        
        sentence.setVerb(verb);
        if(subj != null){
            sentence.setFeature(Feature.PERSON, (subj.getPerson().equals("FIRST")) ? Person.FIRST : (subj.getPerson().equals("SECOND") ? Person.SECOND : Person.THIRD));
            sentence.setFeature(Feature.NUMBER, (subj.getNumber().equals("PLURAL")) ? NumberAgreement.PLURAL : NumberAgreement.SINGULAR);
        }
        sentence.setFeature(Feature.TENSE, tense);
        String output = realiser.realiseSentence(sentence);
        output = output.replace(".", "").toLowerCase();
        
        return output;
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
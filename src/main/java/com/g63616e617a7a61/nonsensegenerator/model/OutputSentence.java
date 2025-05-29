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

/**
 * Represents a generated output sentence constructed from an input sentence and template.
 * This class handles the generation of grammatically correct sentences by replacing placeholders
 * with appropriate words, adjusting verb tenses, and evaluating sentence toxicity.
 * 
 * @author Tommaso Rovoletto
 */

public class OutputSentence {
    private String value;
    private Template t;
    private double toxicity;
    private Tense tense;

    /**
     * Constructs an OutputSentence with random template and present tense.
     * @param in the InputSentence used as source for generation
     */
    public OutputSentence(InputSentence in){
        t = new Template();
        toxicity = 0;
        value = "";
        tense = Tense.PRESENT;
        generate(in);
    }

    /**
     * Constructs an OutputSentence with specified template and default present tense.
     * @param in the InputSentence used as source for generation
     * @param t the Template to use for sentence structure
     */
    public OutputSentence(InputSentence in, Template t){
        this.t = t;
        toxicity = 0;
        value = "";
        tense = Tense.PRESENT;
        generate(in);
    }

    /**
     * Constructs an OutputSentence with random template and specified tense.
     * @param in the InputSentence used as source for generation
     * @param t the Tense to use for verbs
     */
    public OutputSentence(InputSentence in, Tense t){
        this.t = new Template();
        toxicity = 0;
        value = "";
        tense = t;
        generate(in);
    }

    /**
     * Constructs an OutputSentence with specified template and tense.
     * @param in the InputSentence used as source for generation
     * @param t the Template to use for sentence structure
     * @param tense the Tense to use for verbs
     */
    public OutputSentence(InputSentence in, Template t, Tense tense){
        this.t = t;
        toxicity = 0;
        value = "";
        this.tense = tense;
        generate(in);
    }

    /**
     * Generates the output sentence by processing the input sentence and template.
     * @param in the InputSentence containing source words
     */
    private void generate(InputSentence in){
        ArrayList<Verb> v = new ArrayList<>();
        ArrayList<Noun> n = new ArrayList<>();
        ArrayList<Adjective> a = new ArrayList<>();

        //I divide the various syntactic parts that interest us
        for (Syntagm s : in.extract()) {
            if (s instanceof Verb) {
                v.add((Verb) s);
            } else if(s instanceof Noun) {
                n.add((Noun) s);
            }else{
                a.add((Adjective) s);
            }
        }

        value = t.getTemplate(); //I copy the template into the output sentence

        //I check if other terms are needed, if we don't have enough I take them from the "dictionary"
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
        
        //I shuffle arraylists before filling the template
        Collections.shuffle(n);
        Collections.shuffle(v);
        Collections.shuffle(a);

        int index = 0;

        //I search and replace all noun and adjective placeholders
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
                    while (Character.isUpperCase(tmp.charAt(0))) {  //As long as it is a proper name
                        tmp = new Noun().getValue();                //I try to generate a name that can be pluralized
                    }
                    word = English.plural(tmp);
                    
                    break;
                default:
                    word = substring;
                    break;
            }
            if (index == 0) {  //if it's the first word put a capital letter
                word = word.substring(0, 1).toUpperCase() + word.substring(1);
            }
            value = value.replaceFirst(substring, word);
            index += substring.length();
        }

        //I search and replace all the verbs, correcting their verb tense
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

            String newVerb = changeTense(subj, word);
            if (index == 0) {  //if it's the first word put a capital letter
                newVerb = newVerb.substring(0, 1).toUpperCase() + newVerb.substring(1);
            }
            value = value.replaceFirst(word, newVerb);
            
            index += substring.length();
        }
        //Now that we have the sentence I can analyze its toxicity
        try {
            toxicity = ApiController.getToxicity(value);
        } catch (IOException e) {
            toxicity = 0;
        }
    }

    /**
     * Adjusts the tense of a verb based on subject properties.
     * @param subj the subject SyntaxElement containing person/number info
     * @param verb the verb to conjugate
     * @return the properly conjugated verb form
     */
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

    /**
     * Counts occurrences of a substring in the current sentence value.
     * @param substring the pattern to search for
     * @return number of occurrences found
     */
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

    /**
     * Gets the toxicity rate of the generated sentence.
     * @return toxicity rate between 0 (non-toxic) and 1 (highly toxic)
     */
    public double getToxicity(){
        return toxicity;
    }

    /**
     * Converts the sentence to JSON format.
     * @return JSON representation of the sentence
     */
    public String toJson(){
        return "{\""+value+"\"}";
    }

    /**
     * Gets the template used for generating this sentence.
     * @return the Template instance
     */
    public Template getTemplate() {
        return t;
    }

    /**
     * Returns the string representation of the generated sentence.
     * @return the generated sentence
     */
    public String toString(){
        return value;
    }
}
package com.g63616e617a7a61.nonsensegenerator.controller;

import java.util.ArrayList;
import java.util.List;

import com.g63616e617a7a61.nonsensegenerator.model.InputSentence;
import com.g63616e617a7a61.nonsensegenerator.model.OutputSentence;
import com.g63616e617a7a61.nonsensegenerator.model.SyntaxElement;
import com.g63616e617a7a61.nonsensegenerator.model.Template;

import simplenlg.features.Tense;

/**
 * Controller class for managing sentence generation and transformation processes.
 * Handles the conversion of input sentences to output sentences using templates,
 * tense transformations, and vocabulary management. Automatically uploads results
 * to cloud storage.
 * 
 * @author Enrico Giacomel
 */

public class SentenceController {
    private InputSentence in;
    private OutputSentence out;

    /**
     * Constructs a controller with default settings.
     * @param s The input sentence text
     */
    public SentenceController(String s){
        in = new InputSentence(s);
        out = new OutputSentence(in);
        if (ApiController.uploadJsonToBucket(out.toJson())) {
            System.out.println("Operation on bucket completed successfully!");
        } 
    }

    /**
     * Constructs a controller with vocabulary saving option.
     * @param s The input sentence text
     * @param flag If true, saves extracted words to vocabulary
     */
    public SentenceController(String s, boolean flag){
        in = new InputSentence(s, flag);
        out = new OutputSentence(in);
        if (ApiController.uploadJsonToBucket(out.toJson())) {
            System.out.println("Operation on bucket completed successfully!");
        } 
    }

    /**
     * Constructs a controller with vocabulary saving and specified tense.
     * @param s The input sentence text
     * @param flag If true, saves extracted words to vocabulary
     * @param t The tense to use for output sentence
     */
    public SentenceController(String s, boolean flag, Tense t){
        in = new InputSentence(s, flag);
        out = new OutputSentence(in, t);
        if (ApiController.uploadJsonToBucket(out.toJson())) {
            System.out.println("Operation on bucket completed successfully!");
        } 
    }

    /**
     * Constructs a controller with vocabulary saving and specified template.
     * @param s The input sentence text
     * @param flag If true, saves extracted words to vocabulary
     * @param tmpl The template to use for sentence generation
     */
    public SentenceController(String s, boolean flag, Template tmpl){
        in = new InputSentence(s, flag);
        out = new OutputSentence(in, tmpl);
        if (ApiController.uploadJsonToBucket(out.toJson())) {
            System.out.println("Operation on bucket completed successfully!");
        } 
    }

    /**
     * Constructs a controller with vocabulary saving, specified tense and template.
     * @param s The input sentence text
     * @param flag If true, saves extracted words to vocabulary
     * @param t The tense to use for output sentence
     * @param tmpl The template to use for sentence generation
     */
    public SentenceController(String s, boolean flag, Tense t, Template tmpl){
        in = new InputSentence(s, flag);
        out = new OutputSentence(in, tmpl, t);
        if (ApiController.uploadJsonToBucket(out.toJson())) {
            System.out.println("Operation on bucket completed successfully!");
        } 
    }

    /**
     * Constructs a controller with specified tense and template.
     * @param s The input sentence text
     * @param t The tense to use for output sentence
     * @param tmpl The template to use for sentence generation
     */
    public SentenceController(String s, Tense t, Template tmpl){
        in = new InputSentence(s);
        out = new OutputSentence(in, tmpl, t);
        if (ApiController.uploadJsonToBucket(out.toJson())) {
            System.out.println("Operation on bucket completed successfully!");
        } 
    }

    /**
     * Gets the generated output sentence.
     * @return The transformed output sentence as string
     */
    public String getOutputSentence() {
        return out.toString();
    }
    
    /**
     * Gets the syntax tree of the input sentence.
     * @return List of SyntaxElements representing the sentence structure
     */
    public List<SyntaxElement> getSyntaxTree() {
        return in.getSyntaxTree();
    }

    /**
     * Gets the toxicity score of the output sentence.
     * @return Toxicity score between 0.0 (non-toxic) and 1.0 (highly toxic)
     */
    public double getToxicity() {
        return out.getToxicity();
    }

    /**
     * Gets the list of available templates with placeholders replaced by descriptions.
     * @return ArrayList of template strings with human-readable placeholders
     */
    public static ArrayList<String> getTemplateList() {
        ArrayList<String> out = new ArrayList<>();
        for (String template : Template.getTemplates()) {
            out.add(rewriteTemplate(template));
        }
        return out;
    }

    /**
     * Converts a template with programmatic placeholders to a human-readable format.
     * @param template Template string with programmatic placeholders (%na, %ad, etc.)
     * @return Template string with human-readable placeholders ('name', 'adjective', etc.)
     */
    public static String rewriteTemplate(String template) {
        int index = 0;
        String result = template;
        while (true) {
            index = result.indexOf("%", index);
            if (index == -1 || index + 3 > result.length()) {
                break;
            }
            String substring = result.substring(index, index + 3);
            String replacement;
            switch (substring) {
                case "%na":
                    replacement = "\\'name\\'";
                    break;
                case "%ad":
                    replacement = "\\'adjective\\'";
                    break;
                case "%np":
                    replacement = "\\'plural name\\'";
                    break;
                case "%ve":
                    replacement = "\\'verb\\'";
                    break;
                default:
                    replacement = substring;
                    break;
            }
            result = result.substring(0, index) + replacement + result.substring(index + 3);
            index += replacement.length(); // move past the replacement
        }
        return result;
    }

    /**
     * Converts a template with descriptive placeholders back to raw format.
     * @param input Template string with human-readable placeholders
     * @return Template string with programmatic placeholders (%na, %ve, etc.)
     */
    public static String getRawTemplate(String input) {
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

    /**
     * Gets the OutputSentence object generated by this controller.
     * @return The OutputSentence instance containing the transformed sentence and metadata
     */
    public OutputSentence getOutputSentenceObject() {
        return out;
    }
}

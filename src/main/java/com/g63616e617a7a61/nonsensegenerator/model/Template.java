package com.g63616e617a7a61.nonsensegenerator.model;

import java.util.ArrayList;

/**
 * Represents the template construct used in the nonsense generator.
 * This class manages templates that serve as structures for generating the output sentences.
 * It is similar in structure to Syntagm
 *
 * @author Leonardo Sivori
 */

public class Template
{
    private static ArrayList<String> data;
    private static final String DATAPATH = "data/Templates.txt";

    private final String template;

    /**
     * Create a random Template object
     * If the Constructor is unable to load the file containing the list of templates, the value field will be set to "null"
     */
    public Template() {
        if (data == null) {
            data = DataUtils.load(DATAPATH);    //loads Templates.txt into data
        }

        template = DataUtils.getRandom(data); //get random template from data
    }

    /**
     * Create a Templatea object
     * @param t     the template <code>this.template</code> will be set to
     */
    public Template(String t) {
        this.template = t;
    }

    /**
     * Returns the template represented by the object as a string
     * 
     * @return The string value of the template
     */
    public String getTemplate() {
        return template;
    }
}

package com.g63616e617a7a61.nonsensegenerator.model;

import java.util.ArrayList;

/**
 * Subclass of Syntagm representing an Adjective Object
 * 
 * @author Leonardo Sivori
 */

public class Adjective extends Syntagm
{
    private static ArrayList<String> data;
    private static final String DATAPATH = "data/Adjectives.txt";

    /**
     * Create a random Adjective object
     * If the Constructor is unable to load the file containing the list of adjectives, the value field will be set to "null"
     */
    public Adjective() {
        if (data == null) {
            data = DataUtils.load(DATAPATH);    //loads Adjectives.txt into data
        }

        value = DataUtils.getRandom(data); //get random adjective from data
    }

    /**
     * Create an Adjective object
     * @param value     the word <code>this.value</code> will be set to
     */
    public Adjective(String value) {
        this.setValue(value);
    }

    /**
     * Adds a new adjective to the data array
     * @param s     the new adjective to add
     */
    public static void save(String s){
        if(data != null && data.indexOf(s) == -1){
            data.add(s);
            DataUtils.append(s, DATAPATH);
        }
    }
}

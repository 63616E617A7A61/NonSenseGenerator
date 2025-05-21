package com.g63616e617a7a61.nonsensegenerator.model;

import java.util.ArrayList;

/**
 * Subclass of Syntagm representing a Verb Object
 * 
 * @author Leonardo Sivori
 */

public class Verb extends Syntagm
{
    private static ArrayList<String> data;
    private static final String DATAPATH = "data/Verbs.txt";

    /**
     * Create a random Verb object
     * If the Constructor is unable to load the file containing the list of verbs, the value field will be set to "null"
     */
    public Verb() {
        if (data == null) {
            data = DataUtils.load(DATAPATH);    //loads Verbs.txt into data
        }

        value = DataUtils.getRandom(data); //get random verb from data
    }

    /**
     * Create a Verb object
     * @param value     the word <code>this.value</code> will be set to
     */
    public Verb(String value) {
        this.setValue(value);
    }
}

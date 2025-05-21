package com.g63616e617a7a61.nonsensegenerator.model;

import java.util.ArrayList;

/**
 * Subclass of Syntagm representing a Noun Object
 * 
 * @author Leonardo Sivori
 */

public class Noun extends Syntagm
{
    private static ArrayList<String> data;
    private static final String DATAPATH = "data/Nouns.txt";

    /**
     * Create a random Noun object
     * If the Constructor is unable to load the file containing the list of nouns, the value field will be set to "null"
     */
    public Noun() {
        if (data == null) {
            data = DataUtils.load(DATAPATH);    //loads Nouns.txt into data
        }

        value = DataUtils.getRandom(data); //returns random noun from data
    }

    /**
     * Create a Noun object
     * @param value     the word <code>this.value</code> will be set to
     */
    public Noun(String value) {
        this.setValue(value);
    }
}

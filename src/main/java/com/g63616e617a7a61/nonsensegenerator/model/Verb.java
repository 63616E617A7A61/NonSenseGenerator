package com.g63616e617a7a61.nonsensegenerator.model;

import java.util.ArrayList;


/**
 * Hello world!
 *
 */
public class Verb extends Syntagm
{
    private static ArrayList<String> data;
    private static final String DATAPATH = "data/Verbs.txt";

    public Verb() {
        if (data == null) {
            data = DataUtils.load(DATAPATH);
        }

        value = DataUtils.getRandom(data); //get random word from database
    }

    public Verb(String value) {
        this.setValue(value);
    }
}

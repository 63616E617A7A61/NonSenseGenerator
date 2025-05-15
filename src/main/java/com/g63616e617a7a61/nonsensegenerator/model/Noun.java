package com.g63616e617a7a61.nonsensegenerator.model;

import java.util.ArrayList;


/**
 * Hello world!
 *
 */
public class Noun extends Syntagm
{
    private static ArrayList<String> data;
    private static final String DATAPATH = "data/Nouns.txt";

    public Noun() {
        if (data == null) {
            data = DataUtils.load(DATAPATH);
        }

        value = DataUtils.getRandom(data); //get random word from database
    }

    public Noun(String value) {
        this.setValue(value);
    }
}

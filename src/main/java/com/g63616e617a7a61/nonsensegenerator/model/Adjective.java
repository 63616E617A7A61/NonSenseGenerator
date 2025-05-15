package com.g63616e617a7a61.nonsensegenerator.model;

import java.util.ArrayList;


/**
 * Hello world!
 *
 */
public class Adjective extends Syntagm
{
    private static ArrayList<String> data;
    private static final String DATAPATH = "data/Adjectives.txt";

    public Adjective() {
        if (data == null) {
            data = DataUtils.load(DATAPATH);
        }

        value = DataUtils.getRandom(data); //get random word from database
    }

    public Adjective(String value) {
        this.setValue(value);
    }
}

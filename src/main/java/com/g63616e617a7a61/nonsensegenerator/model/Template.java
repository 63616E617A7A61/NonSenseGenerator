package com.g63616e617a7a61.nonsensegenerator.model;

import java.util.ArrayList;


/**
 * Hello world!
 *
 */
public class Template
{
    private static ArrayList data;
    private static final String DATAPATH = "data/Templates.txt";

    private final String template;

    public Template() {
        if (data == null) {
            data = DataUtils.load(DATAPATH);
        }

        template = DataUtils.getRandom(data); //get random template from database
    }

    public String getTemplate() {
        return template;
    }
}

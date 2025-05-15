package com.g63616e617a7a61.nonsensegenerator.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * Hello world!
 *
 */
public class Template
{
    private static ArrayList data;
    private static final String DATAPATH = "data/Templates.txt";

    private String template;

    public Template() {
        if (data == null) {
            try {
                load();
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
                template = "null";
            }
        }

        template = data.get((int)(Math.random() * data.size())).toString(); //get template word from database
    }

    private void load() throws FileNotFoundException {
        data = new ArrayList();

        try (Scanner file = new Scanner(new FileReader(DATAPATH))) {
            String str;
            while(file.hasNext()) {
                str = file.nextLine();
                data.add(str);
            }

            file.close();
        }
    }

    public String getTemplate() {
        return template;
    }
}

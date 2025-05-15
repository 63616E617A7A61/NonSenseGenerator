package com.g63616e617a7a61.nonsensegenerator.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * Hello world!
 *
 */
public class Adjective extends Syntagm
{
    private static ArrayList data;
    private static final String DATAPATH = "data/Adjectives.txt";

    public Adjective() {
        if (data == null) {
            try {
                load();
            } catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
                this.setValue("null");
            }
        }

        this.setValue(data.get((int)(Math.random() * data.size())).toString()); //get random word from database
    }

    public Adjective(String value) {
        this.setValue(value);
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
}

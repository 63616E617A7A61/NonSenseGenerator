package com.g63616e617a7a61.nonsensegenerator;

import java.util.ArrayList;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Dictionary {
    private ArrayList<String> verbs, adjectives, nouns;

    private static final String VERBS_PATH = "Words/Verbs.txt",
            ADJECTIVES_PATH = "Words/Adjectives.txt",
            NOUNS_PATH = "Words/Nouns.txt";

    Dictionary() {
        verbs = loadList(VERBS_PATH);
        adjectives = loadList(ADJECTIVES_PATH);
        nouns = loadList(NOUNS_PATH);
    }

    public String getRandVerb() { return verbs.get(pickRandom(verbs.size()));}
    public String getRandAdjective() { return adjectives.get(pickRandom(adjectives.size()));}
    public String getRandNoun() { return nouns.get(pickRandom(nouns.size()));}

    // Returns an int from 0 to length-1
    private int pickRandom(int length) { return new Random().nextInt(length);}

    // Loads an ArrayList of Strings from a specified .txt file
    private ArrayList<String> loadList (String filePath) {
        ArrayList<String> al = new ArrayList<String>();
        String nextLine;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((nextLine = br.readLine()) != null) { al.add(nextLine);}
        }
        catch (IOException e) {e.printStackTrace();}

        return al;
    }
}

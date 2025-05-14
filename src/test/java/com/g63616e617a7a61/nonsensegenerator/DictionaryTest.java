package com.g63616e617a7a61.nonsensegenerator;

import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DictionaryTest extends TestCase {

    Dictionary testDictionary = new Dictionary();

    // Proof that the generated verb is an actual word from the Verb.txt file.
    public void testGetRandVerb() {
        String s = testDictionary.getRandVerb();
        String nextLine = "", filePath = "Words/Verbs.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((nextLine = br.readLine()) != null) {
                if (nextLine.equals(s)) {break;}
            }
        }
        catch (IOException e) {e.printStackTrace();}

        assertEquals(s, nextLine);
    }

    public void testGetRandAdjective() {
        String s = testDictionary.getRandAdjective();
        String nextLine = "", filePath = "Words/Adjectives.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((nextLine = br.readLine()) != null) {
                if (nextLine.equals(s)) {break;}
            }
        }
        catch (IOException e) {e.printStackTrace();}

        assertEquals(s, nextLine);
    }

    public void testGetRandNoun() {
        String s = testDictionary.getRandNoun();
        String nextLine = "", filePath = "Words/Nouns.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((nextLine = br.readLine()) != null) {
                if (nextLine.equals(s)) {break;}
            }
        }
        catch (IOException e) {e.printStackTrace();}

        assertEquals(s, nextLine);
    }
}
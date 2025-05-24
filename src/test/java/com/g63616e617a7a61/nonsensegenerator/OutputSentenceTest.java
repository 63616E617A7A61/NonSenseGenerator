package com.g63616e617a7a61.nonsensegenerator;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.g63616e617a7a61.nonsensegenerator.model.*;

import simplenlg.features.Tense;

/**
 * Unit test for OutputSentence class.
 * 
 * @author Tommaso Rovoletto
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OutputSentenceTest {
    private InputSentence i;
    private Template t;
    private OutputSentence o;

    @BeforeAll
    public void setUp(){
        i = new InputSentence("The table is big");
        t = new Template("The %ad %ve a %na");
        o = new OutputSentence(i, t);
    }
    
    @Test
    public void testGenerate(){
        assertEquals("The big is a table", o.toString());
    }
    
    @Test
    public void testToxicity(){
        o = new OutputSentence(i, t);
        assertTrue("0.029414838".equals(String.format(Locale.US, "%.9f", o.getToxicity())));
    }

    @Test
    public void testVerbTense(){
        o = new OutputSentence(i, t, Tense.PAST);
        assertEquals("The big was a table", o.toString());
        o = new OutputSentence(i, t, Tense.FUTURE);
        assertEquals("The big will be a table", o.toString());
    }

    @Test
    public void testPluralNameAndVerbAdjustment(){
        o = new OutputSentence(i, new Template("Those %np %ve %ad"), Tense.PRESENT);
        assertEquals("Those tables are big", o.toString());
    }

    @Test
    public void testPluralizationOfAProperName(){
        String properName = "Tom";
        o = new OutputSentence(new InputSentence(properName), new Template("Those %np %ve %ad"));
        assertFalse(o.toString().contains(properName));
        /*
         * if the input sentence contains a proper name and thisone have to go on the template where 
         * a plural name is required, given that a proper name isn't pluralizable, so the program go 
         * to catch a random name on the dictionary until he finds a pluralizable one
         */
    }
    
}
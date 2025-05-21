package com.g63616e617a7a61.nonsensegenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import com.g63616e617a7a61.nonsensegenerator.model.Adjective;
import com.g63616e617a7a61.nonsensegenerator.model.Noun;
import com.g63616e617a7a61.nonsensegenerator.model.Verb;

/**
 * Unit test for Syntagm class and subclasses.
 * 
 * @author Leonardo Sivori
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SyntagmTest
{
    private Noun randomNoun;
    private Adjective randomAdjective;
    private Verb randomVerb;
    
    private Noun customNoun;
    private Adjective customAdjective;
    private Verb customVerb;
    
    @BeforeAll
    public void setUp()
    {
        // Initialize objects with random values from data files
        randomNoun = new Noun();
        randomAdjective = new Adjective();
        randomVerb = new Verb();
        
        // Initialize objects with custom values
        customNoun = new Noun("n");
        customAdjective = new Adjective("a");
        customVerb = new Verb("v");
    }

    @Test
    public void randomObjectShouldNotResultInNull()
    {
        // Test that random initialization doesn't result in "null" values
        assertFalse(randomNoun.getValue().equals("null"));
        assertFalse(randomAdjective.getValue().equals("null"));  
        assertFalse(randomVerb.getValue().equals("null")); 
    }

    @Test
    public void getterShouldNotReturnNull()
    {
        // Test that getValue() doesn't return null for any syntagm type
        assertNotNull(randomNoun.getValue());
        assertNotNull(randomAdjective.getValue());
        assertNotNull(randomVerb.getValue());
    }

    @Test
    public void toStringShouldBeAliasGetter()
    {
        // Test that toString() and getValue() are equivalent
        assertTrue(customNoun.getValue().equals(customNoun.toString()));
        assertTrue(customAdjective.getValue().equals(customAdjective.toString()));
        assertTrue(customVerb.getValue().equals(customVerb.toString()));
    }

    @Test
    public void setterShouldSet()
    {
        // Test that constructor with parameter correctly sets the value
        assertEquals("n", customNoun.getValue());
        assertEquals("a", customAdjective.getValue());
        assertEquals("v", customVerb.getValue());
    }
}

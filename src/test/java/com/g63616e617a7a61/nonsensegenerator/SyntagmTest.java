package com.g63616e617a7a61.nonsensegenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import com.g63616e617a7a61.nonsensegenerator.model.Adjective;
import com.g63616e617a7a61.nonsensegenerator.model.Noun;
import com.g63616e617a7a61.nonsensegenerator.model.Verb;

/**
 * Unit test for simple App.
 */
public class SyntagmTest
{

    @Test
    public void loadShouldCreateData()
    {
        Noun n = new Noun();
        Adjective a = new Adjective();
        Verb v = new Verb();
        assertFalse(n.getValue().equals("null"));
        assertFalse(a.getValue().equals("null"));  
        assertFalse(v.getValue().equals("null")); 
    }

    @Test
    public void getterShouldNotReturnNull()
    {
        Noun n = new Noun();
        Adjective a = new Adjective();
        Verb v = new Verb();
        assertNotNull(n.getValue());
        assertNotNull(a.getValue());
        assertNotNull(v.getValue());
    }

    @Test
    public void setterShouldSet()
    {
        Noun n = new Noun("n");
        Adjective a = new Adjective("a");
        Verb v = new Verb("v");
        assertEquals("n", n.getValue());
        assertEquals("a", a.getValue());
        assertEquals("v", v.getValue());
    }
}

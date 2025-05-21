package com.g63616e617a7a61.nonsensegenerator;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.g63616e617a7a61.nonsensegenerator.model.InputSentence;
import com.g63616e617a7a61.nonsensegenerator.model.OutputSentence;
import com.g63616e617a7a61.nonsensegenerator.model.Template;

public class OutputSentenceTest {
    static InputSentence i;
    static Template t;
    static OutputSentence o;

    @BeforeAll
    public static void init(){
        i = new InputSentence("The table is big");
        t = new Template("The %a  %v  a %n ");
        o = new OutputSentence(i, t);
    }
    
    @Test
    public void testGenerate(){
        assertEquals("The big is a table", o.toString());
    }
    
    @Test
    public void testToxicity(){
        assertTrue("0.029414838".equals(String.format(Locale.US, "%.9f", o.getToxicity())));
    }
}
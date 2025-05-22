package com.g63616e617a7a61.nonsensegenerator;



import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.g63616e617a7a61.nonsensegenerator.model.*;

public class OutputSentenceTest {
    
    @Test
    public void testGenerate(){
        InputSentence i = new InputSentence("The table is big");
        Template t = new Template("The %a  %v  a %n ");
        OutputSentence o = new OutputSentence(i, t);
        assertEquals("The big is a table", o.toString());
    }
    
    @Test
    public void testToxicity(){
        InputSentence i = new InputSentence("The table is big");
        Template t = new Template("The %a  %v  a %n ");
        OutputSentence o = new OutputSentence(i, t);
        assertEquals(0.029414838179945946, o.getToxicity());
    }
}
package com.g63616e617a7a61.nonsensegenerator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import com.g63616e617a7a61.nonsensegenerator.controller.ApiController;
import com.g63616e617a7a61.nonsensegenerator.model.Adjective;
import com.g63616e617a7a61.nonsensegenerator.model.Noun;
import com.g63616e617a7a61.nonsensegenerator.model.Syntagm;
import com.g63616e617a7a61.nonsensegenerator.model.SyntaxElement;
import com.g63616e617a7a61.nonsensegenerator.model.Verb;

public class ApiControllerTest {
    @Test
    public void testExtract(){
        Syntagm[] syntagms = new Syntagm[3];
        Noun n = new Noun("table");
        Verb v = new Verb("is");
        Adjective a = new Adjective("big");
        syntagms[0] = n;
        syntagms[1] = v;
        syntagms[2] = a;

        try {
            assertArrayEquals(syntagms, ApiController.extract("The table is big"));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetToxicity(){
        try {
            assertEquals(0.029414838179945946, ApiController.getToxicity("The table is big"));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetSyntaxTree(){
        List<SyntaxElement> tree = new ArrayList<>();
        tree.add(new SyntaxElement("The", "DET", 0));
        SyntaxElement buff  = new SyntaxElement("table", "NSUBJ", 1);
        ArrayList<Integer> tmp = new ArrayList<>();
        tmp.add(0);
        buff.setEdges(tmp);
        tree.add(buff);
        buff = new SyntaxElement("is", "ROOT", 2);
        tmp = new ArrayList<>();
        tmp.add(1);
        tmp.add(3);
        buff.setEdges(tmp);
        tree.add(buff);
        tree.add(new SyntaxElement("big", "ACOMP", 3));

        try {
            assertTrue(tree.equals(ApiController.getSyntaxTree("The table is big")));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
}

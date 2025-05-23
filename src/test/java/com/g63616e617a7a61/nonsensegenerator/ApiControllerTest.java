package com.g63616e617a7a61.nonsensegenerator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

import com.g63616e617a7a61.nonsensegenerator.model.*;
import com.g63616e617a7a61.nonsensegenerator.controller.ApiController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Unit test for ApiController class.
 * 
 * @author Tommaso Rovoletto
 */

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
            assertTrue("0.029414838".equals(String.format(Locale.US, "%.9f", ApiController.getToxicity("The table is big"))));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetSyntaxTree(){
        List<SyntaxElement> tree = new ArrayList<>();
        tree.add(new SyntaxElement("The", "DET", 0, "PERSON_UNKNOWN", "NUMBER_UNKNOWN"));
        SyntaxElement buff  = new SyntaxElement("table", "NSUBJ", 1, "PERSON_UNKNOWN", "SINGULAR");
        ArrayList<Integer> tmp = new ArrayList<>();
        tmp.add(0);
        buff.setEdges(tmp);
        tree.add(buff);
        buff = new SyntaxElement("is", "ROOT", 2, "THIRD", "SINGULAR");
        tmp = new ArrayList<>();
        tmp.add(1);
        tmp.add(3);
        buff.setEdges(tmp);
        tree.add(buff);
        tree.add(new SyntaxElement("big", "ACOMP", 3, "PERSON_UNKNOWN", "NUMBER_UNKNOWN"));

        try {
            assertTrue(tree.equals(ApiController.getSyntaxTree("The table is big")));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetSubject(){
        try {
            assertTrue("table".equals(ApiController.getSubject("The table is big", "is").getValue()));
        } catch (IOException e) {
            fail(e.getMessage());
        }
        try {
            assertNull(ApiController.getSubject("The table is big", "was"));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
}
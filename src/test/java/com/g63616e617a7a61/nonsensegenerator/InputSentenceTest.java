package com.g63616e617a7a61.nonsensegenerator;

import com.g63616e617a7a61.nonsensegenerator.model.Noun;
import com.g63616e617a7a61.nonsensegenerator.model.Verb;
import com.g63616e617a7a61.nonsensegenerator.model.Adjective;
import com.g63616e617a7a61.nonsensegenerator.model.Syntagm;
import com.g63616e617a7a61.nonsensegenerator.model.InputSentence;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class InputSentenceTest {

    @Test
    public void testExtract() {
        InputSentence in = new InputSentence("The table is big");
        Syntagm[] sy = in.extract();
        assertInstanceOf(Noun.class, sy[0]);
        assertInstanceOf(Verb.class, sy[1]);
        assertInstanceOf(Adjective.class, sy[2]);
    }

    @Test
    public void testGetSyntaxTree() {
    }
}
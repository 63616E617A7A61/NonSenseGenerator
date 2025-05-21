package com.g63616e617a7a61.nonsensegenerator;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.g63616e617a7a61.nonsensegenerator.model.DataUtils;

/**
 * Unit tests for DataUtils class
 * 
 * @author Leonardo Sivori
 */

public class UtilsTest
{
    @Test
    public void getRandomShouldReturnNullIfListIsNull()
    {
        ArrayList<String> list = null;
        assertTrue(DataUtils.getRandom(list).equals("null"));
    }

    @Test
    public void getRandomShouldNotReturnNullIfListIsNotNull()
    {
        ArrayList<String> list = new ArrayList<>(Arrays.asList("ciao", "ciao2"));
        assertFalse(DataUtils.getRandom(list).equals("null"));
    }

    @Test
    public void loadShouldReturnNullIfWrongFilePath()
    {
        ArrayList<String> list = DataUtils.load("wrong/path");
        assertNull(list);
    }

    @Test
    public void loadShouldReturnListIfRightPath()
    {
        ArrayList<String> list = DataUtils.load("data/Adjectives.txt");
        assertNotNull(list);
    }
}

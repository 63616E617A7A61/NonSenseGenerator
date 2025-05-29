package com.g63616e617a7a61.nonsensegenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

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
    
    @Test
    public void appendShouldAddToEOF(@TempDir Path tempDir) 
    {
        // tempDir è una DIRECTORY temporanea, non un file
        String fileName = "test.txt";
        String append = "APPENDTEST";
        
        // Crea il file DENTRO la directory temporanea
        Path tempFile = tempDir.resolve(fileName);

        try {
            Files.createFile(tempFile); // Crea il file
            Files.writeString(tempFile, "FirstLine\n");
        } catch (IOException e) {
            assertTrue(false, e.getMessage());
        }

        DataUtils.append(append, tempFile.toString());

        ArrayList<String> test = DataUtils.load(tempFile.toString());
        
        assertEquals(test.size(), 2);
        assertTrue(test.get(test.size()-1).equals(append));
    }
}
package com.g63616e617a7a61.nonsensegenerator.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Utility class providing data handling operations for model.
 * Contains static methods for loading data from files into an ArrayList and selecting random elements from said ArrayList.
 * 
 * @author Leonardo Sivori
 */

public class DataUtils {
    
    /**
     * Loads data from the input file into an ArrayList of strings.
     * Each line in the file becomes one element in the returned list.
     * In case of <code>FileNotFoundException</code>, the exception message is written to the standard error output and the method returns <code>null</code>
     *
     * @param filePath  The path to the file to be loaded
     * @return          An ArrayList containing the lines from the file, or null if the file cannot be loaded
     */
    public static ArrayList<String> load(String filePath) {
        ArrayList<String> list = new ArrayList<>();

        try {
            @SuppressWarnings("ConvertToTryWithResources")
            Scanner file = new Scanner(new FileReader(filePath));
            String str;
            while(file.hasNext()) {
                str = file.nextLine();
                list.add(str);
            }

            file.close();
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
            return null;
        }

        return list;
    }

    public static void append(String s, String filePath){
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
            writer.println(s);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Selects and returns a random element from the provided ArrayList{@literal <}String{@literal >}.
     * If the input list is null, returns the string "null".
     *
     * @param list  The ArrayList from which to select a random element
     * @return      A randomly selected string from the list, or "null" if <code>list</code> is null
     */
    public static String getRandom(ArrayList<String> list) {
        return list==null ? "null" : list.get((int)(Math.random() * list.size()));
    }
}
package com.g63616e617a7a61.nonsensegenerator.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class DataUtils {
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

    public static String getRandom(ArrayList<String> list) {
        return list==null ? "null" : list.get((int)(Math.random() * list.size()));
    }
}
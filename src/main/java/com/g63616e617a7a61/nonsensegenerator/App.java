package com.g63616e617a7a61.nonsensegenerator;

public class App 
{
    public static void main( String[] args ) {

        Dictionary d = new Dictionary();
        System.out.println("Test Dizionario");
        System.out.println(d.getRandVerb());
        System.out.println(d.getRandAdjective());
        System.out.println(d.getRandNoun());

        System.out.println("\nTest classi");
        Verb v = new Verb();
        Noun n = new Noun();
        Adjective a = new Adjective();
        Verb notRandVerb = new Verb("Drink");

        System.out.println(v.getValue());
        System.out.println(n.getValue());
        System.out.println(a.getValue());
        System.out.println(notRandVerb.getValue());
    }
}

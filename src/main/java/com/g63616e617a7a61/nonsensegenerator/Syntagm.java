package com.g63616e617a7a61.nonsensegenerator;

public class Syntagm {
    private static Dictionary d = new Dictionary();
    private String value;

    public Syntagm() { value = "";}
    public Syntagm(String val) { setValue(val);}

    public Dictionary getDictionary() { return d;}
    public String getValue() { return value;}

    public void setValue(String val) { value = val;}
}

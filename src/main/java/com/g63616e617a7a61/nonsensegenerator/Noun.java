package com.g63616e617a7a61.nonsensegenerator;

public class Noun extends Syntagm{
    public Noun() { setValue(getDictionary().getRandNoun());}
    public Noun(String val) { super(val);}
}
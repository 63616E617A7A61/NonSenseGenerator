package com.g63616e617a7a61.nonsensegenerator;

public class Verb extends Syntagm{
    public Verb() { setValue(getDictionary().getRandVerb());}
    public Verb(String val) { super(val);}
}
package com.g63616e617a7a61.nonsensegenerator;

public class Adjective extends Syntagm{
    public Adjective() { setValue(getDictionary().getRandAdjective());}
    public Adjective(String val) { super(val);}
}
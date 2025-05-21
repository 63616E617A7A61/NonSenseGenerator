package com.g63616e617a7a61.nonsensegenerator.model;
/**
 * Hello world!
 *
 */
public abstract class Syntagm 
{
    protected String value;

    public String getValue() {
        return value;
    }

    public void setValue(String x) {
        value = x;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Syntagm)) return false;
        return this.value.equals(((Syntagm) obj).value);
    }

    @Override
    public String toString() {
        return getValue();
    }
}
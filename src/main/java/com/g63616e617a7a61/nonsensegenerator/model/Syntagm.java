package com.g63616e617a7a61.nonsensegenerator.model;

/**
 * Abstract class representing a syntactic unit (Syntagm) in the nonsense generator.
 * A syntagm is the basic linguistic unit that can be used to represent nouns, verbs and adjectives in sentences.
 * 
 * @author Leonardo Sivori
 */

public abstract class Syntagm 
{
    protected String value;

    /**
     * Returns the word represented by the object as a string
     * 
     * @return The string value of the object
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the word represented by the object as being equal to x
     * 
     * @param x     the word the value object field is to be set as
     */
    public void setValue(String x) {
        value = x;
    }


    /**
     * Checks if this.value is equal to obj.value
     * If obj is not an instance of Syntagm, the method returns <code>FALSE</code>
     * 
     * @param obj     the Syntax object <code>this</code> is to be checked against
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Syntagm)) return false;
        return this.value.equals(((Syntagm) obj).value);
    }

    /**
     * Alias for getValue()
     * @return The string value of the object
     * @see #getValue()
     */
    @Override
    public String toString() {
        return getValue();
    }
}
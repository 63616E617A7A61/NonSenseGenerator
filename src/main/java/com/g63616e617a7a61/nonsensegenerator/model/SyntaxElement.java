package com.g63616e617a7a61.nonsensegenerator.model;

import java.util.ArrayList;

/**
 * Represents a syntax element with various linguistic properties and relationships.
 * This class stores information about a word including its value, syntactic category,
 * grammatical person, number, index position, and dependencies to other elements.
 * 
 * @author Tommaso Rovoletto
 */
public class SyntaxElement {
    private String value;
    private String syntax_value;
    private String person;
    private String number;
    private int index;
    private ArrayList<Integer> edges;
    
    /**
     * Constructs a new SyntaxElement with the specified properties.
     * 
     * @param value the text value of the element
     * @param syntax_value the part-of-speech
     * @param index the positional index of this element
     * @param person the grammatical person (e.g., "first", "second", "third")
     * @param number the grammatical number (e.g., "singular", "plural")
     */
    public SyntaxElement(String value, String syntax_value, int index, String person, String number){
        this.value = value;
        this.syntax_value = syntax_value;
        this.index = index;
        this.person = person;
        this.number = number;
        edges = null;
    }
    
    /**
     * Gets the text value of this element.
     * @return the element's textual value
     */
    public String getValue() {
        return value;
    }
    
    /**
     * Gets the part-of-speech of this element.
     * @return the element's syntactic category
     */
    public String getSyntax_value() {
        return syntax_value;
    }

    /**
     * Gets the index position of this element.
     * @return the element's index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Gets the list of edge connections to other elements.
     * @return list of connected element indices, or null if no connections exist
     */
    public ArrayList<Integer> getEdges() {
        return edges;
    }
    
    /**
     * Gets the grammatical number of this element.
     * @return the element's number (e.g., "singular", "plural")
     */
    public String getNumber() {
        return number;
    }
    
    /**
     * Gets the grammatical person of this element.
     * @return the element's person (e.g., "first", "second", "third")
     */
    public String getPerson() {
        return person;
    }

    /**
     * Sets the edge connections for this element.
     * @param edges list of indices representing connected elements
     */
    public void setEdges(ArrayList<Integer> edges) {
        this.edges = edges;
    }

    /**
     * Compares this SyntaxElement to another object for equality.
     * Two SyntaxElements are considered equal if all their properties match.
     * 
     * @param se the object to compare with
     * @return true if the objects are equal SyntaxElements, false otherwise
     */
    @Override
    public boolean equals(Object se) {
        if (!(se instanceof SyntaxElement)) return false;

        SyntaxElement e = (SyntaxElement) se;
        if(edges != null)
            return value.equals(e.value) 
                && syntax_value.equals(e.syntax_value) 
                && index == e.index 
                && person == e.person
                && number == e.number
                && edges.equals(e.edges);
        else
            return value.equals(e.value) 
                && syntax_value.equals(e.syntax_value)
                && person == e.person
                && number == e.number 
                && index == e.index;
    }
}

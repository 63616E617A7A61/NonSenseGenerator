package com.g63616e617a7a61.Model;

import java.util.ArrayList;

public class SyntaxElement {
    private String value;
    private String syntax_value;
    private int index;
    private ArrayList<Integer> edges;
    
    public SyntaxElement(String value, String syntax_value, int index){
        this.value = value;
        this.syntax_value = syntax_value;
        this.index = index;
        edges = null;
    }

    public String getValue() {
        return value;
    }
    
    public String getSyntax_value() {
        return syntax_value;
    }

    public int getIndex() {
        return index;
    }

    public ArrayList<Integer> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<Integer> edges) {
        this.edges = edges;
    }

    @Override
    public boolean equals(Object se) {
        SyntaxElement e = (SyntaxElement) se;
        if(edges != null)
            return value.equals(e.value) 
                && syntax_value.equals(e.syntax_value) 
                && index == e.index 
                && edges.equals(e.edges);
        else
            return value.equals(e.value) 
                && syntax_value.equals(e.syntax_value) 
                && index == e.index;
    }
}

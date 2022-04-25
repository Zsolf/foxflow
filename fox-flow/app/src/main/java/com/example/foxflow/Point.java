package com.example.foxflow;

import java.util.HashMap;

public class Point {

    private String name;
    private HashMap<String,Edge> outputEdges;
    private HashMap<String,Edge> inputEdges;

    public Point(String name, HashMap<String, Edge> outputEdges, HashMap<String, Edge> inputEdges) {
        this.name = name;
        this.outputEdges = outputEdges;
        this.inputEdges = inputEdges;
    }

    public Point() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, Edge> getOutputEdges() {
        return outputEdges;
    }

    public void setOutputEdges(HashMap<String, Edge> outputEdges) {
        this.outputEdges = outputEdges;
    }

    public HashMap<String, Edge> getInputEdges() {
        return inputEdges;
    }

    public void setInputEdges(HashMap<String, Edge> inputEdges) {
        this.inputEdges = inputEdges;
    }

    public boolean hasOutputEdge(){
        if(outputEdges.size() != 0) {
            return true;
        }
        return false;
    }
}

package com.example.foxflow;

public class Edge {

    private int used;
    private int capacity;
    private String name;

    public Edge(Integer used, int capacity, String name) {
        this.used = used;
        this.capacity = capacity;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void use(Integer use){
        this.used += use;
    }
}

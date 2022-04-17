package com.example.foxflow;

public class Edge {

    private Integer used;
    private Integer capacity;
    private String name;

    public Edge(Integer used, Integer capacity, String name) {
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

    public Integer getUsed() {
        return used;
    }

    public void setUsed(Integer used) {
        this.used = used;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public void use(Integer use){
        this.used += use;
    }
}

package com.example.foxflow;

public class Edge {

    private Integer used;
    private Integer capacity;

    public Edge(Integer used, Integer capacity) {
        this.used = used;
        this.capacity = capacity;
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

package com.example.foxflow;

public class Edge {

    private int used;
    private int capacity;
    private String name;
    private boolean isResidual;

    public Edge(Integer used, int capacity, String name, boolean isResidual) {
        this.used = used;
        this.capacity = capacity;
        this.name = name;
        this.isResidual = isResidual;
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

    public boolean isResidual() {
        return isResidual;
    }

    public void setResidual(boolean residual) {
        isResidual = residual;
    }
}

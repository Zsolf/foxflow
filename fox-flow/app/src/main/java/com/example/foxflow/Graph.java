package com.example.foxflow;

import java.util.HashMap;

public class Graph {

    private HashMap<String,Point> points;
    private Point source;
    private Point sink;

    public Graph(HashMap<String, Point> points, Point source, Point sink) {
        this.points = points;
        this.source = source;
        this.sink = sink;
    }

    public HashMap<String, Point> getPoints() {
        return points;
    }

    public void setPoints(HashMap<String, Point> points) {
        this.points = points;
    }

    public Point getSource() {
        return source;
    }

    public void setSource(Point source) {
        this.source = source;
    }

    public Point getSink() {
        return sink;
    }

    public void setSink(Point sink) {
        this.sink = sink;
    }
}

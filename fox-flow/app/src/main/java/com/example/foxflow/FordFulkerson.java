package com.example.foxflow;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class FordFulkerson {

    Graph graph;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void runAlgorithm(Graph graph){
        this.graph = graph;
        boolean flag = true;
        int flowStrength = 0;
            while(flag) {
                Path path = pathToSource(graph.getSource(), new Point("",null,null));
                System.out.println(path.getNodes());
                System.out.println(path.getMinimum());
                if (path.getMinimum() == 0) {
                    flag = false;
                    System.out.println("Strength: " + flowStrength);
                }else {
                    flowStrength += path.getMinimum();
                    useCapacity(path);
                }

            }

    }

    private void useCapacity(Path path){
        for (int i = 0; i + 1 < path.getNodes().size(); i++){
            graph.getPoints().get(path.getNodes().get(i)).getInputEdges().get(path.getNodes().get(i+1)).use(path.getMinimum());
            graph.getPoints().get(path.getNodes().get(i+1)).getOutputEdges().get(path.getNodes().get(i)).use(path.getMinimum());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Path pathToSource(Point node, Point previous){
        for (String key : node.getOutputEdges().keySet()){
            Point next = graph.getPoints().get(key);
            int left = node.getOutputEdges().get(next.getName()).getCapacity() - node.getOutputEdges().get(next.getName()).getUsed();
            if(left != 0) {
                if (next.getName().equals(graph.getSink().getName())) {
                    ArrayList<String> nodes = new ArrayList<String>();
                    nodes.add(next.getName());
                    nodes.add(node.getName());
                    return new Path(left, nodes);
                }

                if(!previous.getName().equals(next.getName())) {
                    Path path = pathToSource(next, node);

                    if (path.getMinimum() > left) {
                        path.setMinimum(left);
                    }
                    path.addNode(node.getName());
                    if (path.getMinimum() != 0) {
                        return path;
                    }
                }
            }

        }
        ArrayList<String> nodes = new ArrayList<String>();
        nodes.add(node.getName());
        return new Path(0,nodes);
    }

    public class Path{
        private int minimum;
        private ArrayList<String> nodes;

        public Path(int minimum, ArrayList<String> nodes) {
            this.minimum = minimum;
            this.nodes = nodes;
        }

        public Path() {
        }

        public void addNode(String name){
            this.nodes.add(name);
        }

        public int getMinimum() {
            return minimum;
        }

        public void setMinimum(int minimum) {
            this.minimum = minimum;
        }

        public ArrayList<String> getNodes() {
            return nodes;
        }

        public void setNodes(ArrayList<String> nodes) {
            this.nodes = nodes;
        }
    }

}

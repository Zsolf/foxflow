package com.example.foxflow;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class FordFulkerson {

    Graph graph;
    boolean finsihed;
    String currentStep;
    int flowStrength;
    ArrayList<Point> pathNodes = new ArrayList<Point>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void runAlgorithm(Graph graph){
        this.graph = graph;
        finsihed = false;
        flowStrength = 0;
        if(!this.isFinsihed()) {
            stepByStep();
        }




    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void stepByStep(){
            ArrayList<Point> visitedNodes = new ArrayList<>();
            visitedNodes.add(new Point("",null,null));
            Path path = pathToSource(graph.getSource(), visitedNodes);
            if (path.getMinimum() == 0) {
                this.setFinsihed(true);
            }else {
                flowStrength += path.getMinimum();
                useCapacity(path);
                String stringPath = "";
                String residuals = "";
                this.pathNodes = new ArrayList<Point>();
                for (int i = path.getNodes().size()-1; i >= 0;i--) {
                    Point point = graph.getPoints().get(path.getNodes().get(i));
                    this.pathNodes.add(point);
                    Point nextPoint = new Point();
                    stringPath += point.getName() + "-";
                    if(i>0){
                        nextPoint = graph.getPoints().get(path.getNodes().get(i-1));
                        if(point.getOutputEdges().get(nextPoint.getName()).isResidual()){
                            residuals += point.getName() +"-" + nextPoint.getName() + ",";
                        }
                    }
                }

                residuals = residuals.length() > 0 ? "{"+ residuals.substring(0,residuals.length()-1) + "}" : "{}";
                stringPath = stringPath.substring(0,stringPath.length()-1);

                this.setCurrentStep("The chosen path is: " + stringPath + " from which: " + residuals + " is a/are residual edge(s) | " + "The minimum capacity is " + path.getMinimum() + " | f=" + flowStrength);
            }


    }

    private void useCapacity(Path path){
        for (int i = 0; i + 1 < path.getNodes().size(); i++){
            graph.getPoints().get(path.getNodes().get(i)).getInputEdges().get(path.getNodes().get(i+1)).use(path.getMinimum());
            if(graph.getPoints().get(path.getNodes().get(i)).getOutputEdges().get(path.getNodes().get(i+1)) != null){
                    if(graph.getPoints().get(path.getNodes().get(i)).getOutputEdges().get(path.getNodes().get(i+1)).isResidual()) {
                        graph.getPoints().get(path.getNodes().get(i)).getOutputEdges().get(path.getNodes().get(i + 1)).use(path.getMinimum() - 2 * path.getMinimum());
                    }
            }else {
                Edge original = graph.getPoints().get(path.getNodes().get(i)).getInputEdges().get(path.getNodes().get(i + 1));
                Edge residual = new Edge(original.getCapacity() - original.getUsed(), original.getCapacity(), path.getNodes().get(i + 1), true);
                graph.getPoints().get(path.getNodes().get(i)).getOutputEdges().put(path.getNodes().get(i + 1), residual);
            }

            graph.getPoints().get(path.getNodes().get(i+1)).getOutputEdges().get(path.getNodes().get(i)).use(path.getMinimum());
            if(graph.getPoints().get(path.getNodes().get(i+1)).getInputEdges().get(path.getNodes().get(i)) != null){
                    if(graph.getPoints().get(path.getNodes().get(i+1)).getInputEdges().get(path.getNodes().get(i)).isResidual()) {
                        graph.getPoints().get(path.getNodes().get(i + 1)).getInputEdges().get(path.getNodes().get(i)).use(path.getMinimum() - 2 * path.getMinimum());
                    }
            }else {
                Edge original = graph.getPoints().get(path.getNodes().get(i+1)).getOutputEdges().get(path.getNodes().get(i));
                Edge residual = new Edge(original.getCapacity() - original.getUsed(), original.getCapacity(), path.getNodes().get(i), true);
                graph.getPoints().get(path.getNodes().get(i+1)).getInputEdges().put(path.getNodes().get(i), residual);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Path pathToSource(Point node, ArrayList<Point> previousNodes){
        for (String key : node.getOutputEdges().keySet()){
            Point next = graph.getPoints().get(key);
            int left = node.getOutputEdges().get(next.getName()).getCapacity() - node.getOutputEdges().get(next.getName()).getUsed();
            if(left != 0 && !next.getName().equals(graph.getSource().getName())) {
                if (next.getName().equals(graph.getSink().getName())) {
                    ArrayList<String> nodes = new ArrayList<String>();
                    nodes.add(next.getName());
                    nodes.add(node.getName());
                    return new Path(left, nodes);
                }

                if(!previousNodes.get(previousNodes.size()-1).getName().equals(next.getName()) &&
                        !previousNodes.contains(next)) {
                    previousNodes.add(node);
                    Path path = pathToSource(next, previousNodes );

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

    public boolean isFinsihed() {
        return finsihed;
    }

    public void setFinsihed(boolean finsihed) {
        this.finsihed = finsihed;
    }

    public String getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(String currentStep) {
        this.currentStep = currentStep;
    }
}

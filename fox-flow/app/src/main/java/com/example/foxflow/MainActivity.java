package com.example.foxflow;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    int counter;
    float[] start;
    MyCanvas myCanvas;
    Integer lastButtonId;
    char alphabet;
    Graph flowGraph;
    String state;


    private String m_Text = "";
    ArrayList<View> views;



    @SuppressLint({"ClickableViewAccessibility", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        counter = 1;
        alphabet = 'A';
        start =  new float[2];
        start[0] = -9999;
        super.onCreate(savedInstanceState);
        views = new ArrayList<>();
        flowGraph = new Graph(new HashMap<>(),null,null);
        state = "edit";

        FordFulkerson ff = new FordFulkerson();

        setContentView(R.layout.activity_main);
        MyCanvas rl = (MyCanvas) findViewById(R.id.rl);
        myCanvas = findViewById(R.id.rl);
        RelativeLayout.LayoutParams bp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        Button doIt = new Button(this);
        doIt.setLayoutParams(bp1);
        doIt.setId(99);
        doIt.setText(new StringBuilder().append("Click here if you're done with editing").toString());
        doIt.setBackgroundColor(Color.parseColor("#90EE90"));
        doIt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if(state.equals("edit") && flowGraph.getPoints().size() > 1){
                    state = "source";
                    counter = 100;
                    Button btn = (Button) findViewById(99);
                    btn.setText(new StringBuilder().append("Please select the source node").toString());
                    btn.setBackgroundColor(Color.parseColor("#00BFFF"));
                }else if(flowGraph.getSource() != null && flowGraph.getSink() != null && ff.graph == null){
                    Button btn = (Button) findViewById(99);
                    btn.setText(new StringBuilder().append("Use the forward button to move the algorithm").toString());
                    btn.setBackgroundColor(Color.parseColor("#00FFFF"));
                    ff.runAlgorithm(flowGraph);
                }
            }
        });
        rl.addView(doIt);
        rl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX()-50;
                float y = event.getY()-80;


                RelativeLayout.LayoutParams bp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                bp.leftMargin = (int) x;
                bp.topMargin = (int) y;

                if(counter<7) {
                    addButton(bp);
                }
                return false;
            }
        });

        FloatingActionButton forward = findViewById(R.id.forward);
        forward.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if(state == "done" && !ff.isFinsihed()) {
                    LinearLayout ll = findViewById(R.id.scrollLin);
                    TextView tv = new TextView(MainActivity.this);
                    tv.setText(new StringBuilder().append(ff.getCurrentStep()).toString());
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(10, 10, 0, 10);
                    tv.setLayoutParams(lp);
                    ll.addView(tv);

                    for(int i = 0; i < ff.pathNodes.size()-1; i++) {
                        int count = rl.getChildCount();
                        View view = null;
                        for (int j = 0; j < count; j++) {
                            view = rl.getChildAt(j);
                            if (view instanceof TextView && view.getTag(R.id.id) != null) {
                                if(view.getTag(R.id.id).equals(ff.pathNodes.get(i).getName()+""+ff.pathNodes.get(i+1).getName())) {
                                    ((TextView) view).setText(new StringBuilder().append(ff.pathNodes.get(i).getOutputEdges().get(ff.pathNodes.get(i + 1).getName()).getCapacity()).append("/").append(ff.pathNodes.get(i).getOutputEdges().get(ff.pathNodes.get(i + 1).getName()).getUsed()).toString());
                                }
                            }
                        }
                    }

                    ff.stepByStep();
                }else if (ff.isFinsihed()){
                    Button btn = (Button) findViewById(99);
                    btn.setText(new StringBuilder().append("The algorithm is finished").toString());
                    btn.setBackgroundColor(Color.parseColor("#FF7F7F"));
                }
            }
        });
    }

    public void addButton(RelativeLayout.LayoutParams bp){
        int localCount = counter;
        MyCanvas rl = (MyCanvas) findViewById(R.id.rl);
        Button btn = new Button(this);
        bp.width = 150;
        bp.height = 150;
        btn.setLayoutParams(bp);
        btn.setText(new StringBuilder().append("").append(alphabet).toString());
        btn.setBackgroundResource(R.mipmap.node_blank);
        btn.setId(localCount);
        btn.setTag(R.id.id,alphabet);
        btn.setTag(R.id.type,"blank");
        Point point = new Point(""+alphabet,new HashMap<String,Edge>(),new HashMap<String,Edge>());
        flowGraph.getPoints().put(point.getName(), point);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    int count = rl.getChildCount();
                    View view = null;
                    for (int i = 0; i < count; i++) {
                        view = rl.getChildAt(i);
                        if (view instanceof Button && view.getId() == localCount) {
                            if (state.equals("edit")) {
                                if (view.getTag(R.id.type) != "green") {
                                    view.setBackgroundResource(R.mipmap.node_green);
                                    view.setTag(R.id.type, "green");
                                    if (start[0] == -9999) {
                                        start[0] = view.getX() + 75;
                                        start[1] = view.getY() + 75;
                                        lastButtonId = view.getId();
                                    } else {
                                        int[] param = determineBorder(start[0], start[1], view.getX() + 75, view.getY() + 75);
                                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                        builder.setTitle("Please set the capacity");
                                        final EditText input = new EditText(MainActivity.this);
                                        input.setInputType(InputType.TYPE_CLASS_NUMBER);
                                        builder.setView(input);
                                        View finalView = view;
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                m_Text = input.getText().toString();
                                                RelativeLayout.LayoutParams lparams = new RelativeLayout.LayoutParams(
                                                        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                                TextView tv = new TextView(MainActivity.this);
                                                tv.setLayoutParams(lparams);
                                                tv.setText(new StringBuilder().append(input.getText().toString()).append("/0").toString());
                                                tv.setX(((start[0] + finalView.getX() + param[0]) / 2) + ((start[0] + finalView.getX() + param[0]) / 20));
                                                tv.setY(((start[1] + finalView.getY() + param[1]) / 2) + ((start[1] + finalView.getY() + param[1]) / 100));
                                                View last = findViewById(lastButtonId);
                                                last.setTag(R.id.type, "blank");
                                                last.setBackgroundResource(R.mipmap.node_blank);
                                                tv.setTag(R.id.id, last.getTag(R.id.id) + "" + finalView.getTag(R.id.id));
                                                tv.setTag(R.integer.capacity, Integer.parseInt(input.getText().toString().length() > 0 ? input.getText().toString() : "0"));
                                                MyCanvas rl = (MyCanvas) findViewById(R.id.rl);
                                                rl.addView(tv);
                                                rl.drawArrow(start[0], start[1], finalView.getX() + param[0], finalView.getY() + param[1]);
                                                finalView.setBackgroundResource(R.mipmap.node_blank);
                                                finalView.setTag(R.id.type, "blank");
                                                start[0] = -9999;
                                                views.add(tv);

                                                Edge edgeIn = new Edge(0, Integer.parseInt(input.getText().toString().length() > 0 ? input.getText().toString() : "0"), last.getTag(R.id.id)+"",false);
                                                Edge edgeOut = new Edge(0, Integer.parseInt(input.getText().toString().length() > 0 ? input.getText().toString() : "0"), point.getName(),false);
                                                flowGraph.getPoints().get(point.getName()).getInputEdges().put(edgeIn.getName(), edgeIn);
                                                flowGraph.getPoints().get(last.getTag(R.id.id)+"").getOutputEdges().put(edgeOut.getName(), edgeOut);
                                            }
                                        });
                                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                finalView.setBackgroundResource(R.mipmap.node_blank);
                                                finalView.setTag(R.id.type, "blank");
                                                start[0] = -9999;
                                                View last = findViewById(lastButtonId);
                                                last.setTag(R.id.type, "blank");
                                                last.setBackgroundResource(R.mipmap.node_blank);
                                            }
                                        });
                                        builder.show();

                                    }
                                } else {
                                    view.setBackgroundResource(R.mipmap.node_blank);
                                    view.setTag(R.id.type, "blank");
                                    start[0] = -9999;
                                }
                                break;

                            } else if (state.equals("sink")) {
                                view.setBackgroundResource(R.mipmap.node_red);
                                view.setTag(R.id.type, "red");
                                flowGraph.setSink(flowGraph.getPoints().get(view.getTag(R.id.id) + ""));
                                state = "done";
                                @SuppressLint("ResourceType") Button btn = (Button) findViewById(99);
                                btn.setText(new StringBuilder().append("Click here to run the algorithm").toString());
                                btn.setBackgroundColor(Color.parseColor("#90EE90"));
                            } else if (state.equals("source")) {
                                view.setBackgroundResource(R.mipmap.node_blue);
                                view.setTag(R.id.type, "blue");
                                flowGraph.setSource(flowGraph.getPoints().get(view.getTag(R.id.id) + ""));
                                state = "sink";
                                @SuppressLint("ResourceType") Button btn = (Button) findViewById(99);
                                btn.setText(new StringBuilder().append("Please select the sink node").toString());
                                btn.setBackgroundColor(Color.parseColor("#9D5783"));
                            }
                        }

                    }

            }
        });
        counter++;
        alphabet++;
        rl.addView(btn);

    }

    public int[] determineBorder(float from_x,float from_y,float to_x,float to_y){

        String quarter = "";


        if(from_x <= to_x && from_y <= to_y){
            quarter = "1";
        }else if(from_x >= to_x && from_y <= to_y){
            quarter = "2";
        }else if(from_x >= to_x && from_y >= to_y){
            quarter = "3";
        }else{
            quarter = "4";
        }

        switch (quarter){
            case "1": if (Math.abs(Math.abs(from_y) - Math.abs(to_y)) <=200){
                return new int[]{0, 75};
            }else if(Math.abs(Math.abs(from_x) - Math.abs(to_x)) <=200){
                return new int[]{75, 0};
            }else{
                return new int[]{25, 25};
            }
            case "2": if (Math.abs(Math.abs(from_y) - Math.abs(to_y)) <=200){
                return new int[]{150, 75};
            }else if(Math.abs(Math.abs(from_x) - Math.abs(to_x)) <=200){
                return new int[]{75, 0};
            }else{
                return new int[]{125, 25};
            }
            case "3": if (Math.abs(Math.abs(from_y) - Math.abs(to_y)) <=200){
                return new int[]{150, 75};
            }else if(Math.abs(Math.abs(from_x) - Math.abs(to_x)) <=200){
                return new int[]{75, 150};
            }else{
                return new int[]{125, 125};
            }
            case "4":
                if (Math.abs(Math.abs(from_y) - Math.abs(to_y)) <=150){
                    return new int[]{0, 75};
                }else if(Math.abs(Math.abs(from_x) - Math.abs(to_x)) <=150){
                    return new int[]{75, 150};
                }else{
                    return new int[]{25, 125};
                }
        }


        return null;
    }




}
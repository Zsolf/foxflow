package com.example.foxflow;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int counter;
    float[] start;
    MyCanvas myCanvas;
    Integer lastButtonId;
    char alphabet;


    private String m_Text = "";



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        counter = 1;
        alphabet = 'A';
        start =  new float[2];
        start[0] = -9999;
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        MyCanvas rl = (MyCanvas) findViewById(R.id.rl);
        myCanvas = findViewById(R.id.rl);
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
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = rl.getChildCount();
                View view = null;
                for(int i=0; i<count; i++) {
                    view = rl.getChildAt(i);
                    if(view instanceof Button && view.getId() == localCount){
                        if(view.getTag(R.id.type) != "green") {
                            view.setBackgroundResource(R.mipmap.node_green);
                            view.setTag(R.id.type,"green");
                            if (start[0] == -9999){
                                start[0] = view.getX() + 75;
                                start[1] = view.getY() + 75;
                                lastButtonId = view.getId();
                            }else{
                                int[] param = determineBorder(start[0],start[1], view.getX() + 75,view.getY() + 75);
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
                                        tv.setText(input.getText().toString());
                                        tv.setX(((start[0] + finalView.getX() + param[0])/2)+((start[0] + finalView.getX() + param[0])/20));
                                        tv.setY( ((start[1] + finalView.getY() + param[1])/2)+((start[1] + finalView.getY() + param[1])/100));
                                        MyCanvas rl = (MyCanvas) findViewById(R.id.rl);
                                        rl.addView(tv);
                                        rl.drawArrow(start[0],start[1], finalView.getX() + param[0],finalView.getY() + param[1]);
                                        finalView.setBackgroundResource(R.mipmap.node_blank);
                                        finalView.setTag(R.id.type,"blank");
                                        start[0] = -9999;
                                        View last =  findViewById(lastButtonId);
                                        last.setTag(R.id.type,"blank");
                                        last.setBackgroundResource(R.mipmap.node_blank);
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        finalView.setBackgroundResource(R.mipmap.node_blank);
                                        finalView.setTag(R.id.type,"blank");
                                        start[0] = -9999;
                                        View last =  findViewById(lastButtonId);
                                        last.setTag(R.id.type,"blank");
                                        last.setBackgroundResource(R.mipmap.node_blank);
                                    }
                                });
                                builder.show();

                            }
                        }else{
                            view.setBackgroundResource(R.mipmap.node_blank);
                            view.setTag(R.id.type,"blank");
                            start[0] = -9999;
                        }
                        break;
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

        System.out.println(quarter);
        System.out.println(Math.abs(from_y) - Math.abs(to_y));

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
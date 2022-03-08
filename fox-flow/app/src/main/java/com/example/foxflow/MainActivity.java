package com.example.foxflow;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
        rl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                float x = event.getX()-50;
                float y = event.getY()-50;

                RelativeLayout.LayoutParams bp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                bp.leftMargin = (int) x;
                bp.topMargin = (int) y;
                addButton(bp);

                return false;
            }
        });
    }

    public void addButton(RelativeLayout.LayoutParams bp){
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
        Button btn = new Button(this);
        btn.setLayoutParams(bp);
        btn.setText("ASD");

        rl.addView(btn);
    }

}
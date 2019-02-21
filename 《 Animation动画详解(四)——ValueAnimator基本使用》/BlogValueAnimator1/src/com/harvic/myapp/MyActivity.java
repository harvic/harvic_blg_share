package com.harvic.myapp;

import android.animation.*;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MyActivity extends Activity {
    private TextView tv;
    private Button btnStart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        tv = (TextView) findViewById(R.id.tv);

        btnStart = (Button) findViewById(R.id.btn);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 二、ValueAnimator简单使用之实例使用ValueAnimator
                 */
//                doAnimation();
                /**
                 * 三、常用方法之1:ofInt与ofFloat
                 */
                doOfFloatAnimation();

            }
        });


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyActivity.this, "clicked me", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * 二、1.ValueAnimator简单使用
     */
    private void doAnimation(){
        ValueAnimator animator = ValueAnimator.ofInt(0,400);
        animator.setDuration(1000);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int curValue = (int)animation.getAnimatedValue();
                tv.layout(curValue,curValue,curValue+tv.getWidth(),curValue+tv.getHeight());
            }
        });
        animator.start();
    }

    /**
     * 三、常用方法之1:ofInt与ofFloat
     */
    private void doOfFloatAnimation(){
        ValueAnimator animator = ValueAnimator.ofFloat(0f,400f,50f,300f);
        animator.setDuration(3000);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float curValueFloat = (Float)animation.getAnimatedValue();
                int curValue = curValueFloat.intValue();
                tv.layout(curValue,curValue,curValue+tv.getWidth(),curValue+tv.getHeight());
            }
        });
        animator.start();
    }

}


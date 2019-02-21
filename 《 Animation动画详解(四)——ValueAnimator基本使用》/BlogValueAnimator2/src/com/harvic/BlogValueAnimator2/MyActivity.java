package com.harvic.BlogValueAnimator2;

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
    private Button btnStart,btnCancel;
    private ValueAnimator repeatAnimator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        tv = (TextView) findViewById(R.id.tv);

        btnStart = (Button) findViewById(R.id.btn);
        btnCancel = (Button)findViewById(R.id.btn_cancel);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 三、常用方法之常用函数
                 */
//                repeatAnimator = doRepeatAnim();

                /**
                 * 三、常用方法之2:两个监听器
                 */
//                repeatAnimator = doAnimatorListener();

                /**
                 * 三、常用方法之2:其它函数
                 */
                repeatAnimator = doRepeatAnim2();
                ValueAnimator newAnimator = repeatAnimator.clone();
                newAnimator.setStartDelay(10000);
                newAnimator.start();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                repeatAnimator.removeAllListeners();
//                repeatAnimator.removeAllUpdateListeners();

                repeatAnimator.cancel();
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
     * 三、常用方法之2:常用函数
     * @return
     */
    private ValueAnimator doRepeatAnim(){
        ValueAnimator animator = ValueAnimator.ofInt(0,400);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int curValue = (int)animation.getAnimatedValue();
                tv.layout(tv.getLeft(),curValue,tv.getRight(),curValue+tv.getHeight());
            }
        });
        animator.setDuration(1000);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.start();
        return animator;
    }

    /**
     * 三、常用方法之2:两个监听器
     * @return
     */
    private ValueAnimator doAnimatorListener(){
        ValueAnimator animator = ValueAnimator.ofInt(0,400);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int curValue = (int)animation.getAnimatedValue();
                tv.layout(tv.getLeft(),curValue,tv.getRight(),curValue+tv.getHeight());
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.d("qijian","animation start");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d("qijian","animation end");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.d("qijian","animation cancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.d("qijian","animation repeat");
            }
        });
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(1000);
        animator.start();
        return animator;
    }


    /**
     * 三、常用方法之2:其它函数
     */
    private ValueAnimator doRepeatAnim2(){
        ValueAnimator animator = ValueAnimator.ofInt(0,400);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int curValue = (int)animation.getAnimatedValue();
                tv.layout(tv.getLeft(),curValue,tv.getRight(),curValue+tv.getHeight());
            }
        });
        animator.setDuration(1000);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        return animator;
    }


}

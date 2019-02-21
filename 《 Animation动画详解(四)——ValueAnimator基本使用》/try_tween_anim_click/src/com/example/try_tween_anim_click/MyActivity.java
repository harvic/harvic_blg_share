package com.example.try_tween_anim_click;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MyActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final TextView tv  = (TextView) findViewById(R.id.tv);

        Button btn  = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TranslateAnimation animation = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 400,
                        Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 400);
                animation.setFillAfter(true);
                animation.setDuration(1000);
                tv.startAnimation(animation);
            }
        });


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyActivity.this,"clicked me",Toast.LENGTH_SHORT).show();
            }
        });

    }
}

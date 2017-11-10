package com.example.animationphysic;

import android.os.Bundle;
import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAnimation();
    }

    private void setAnimation() {
        final TextView tvHello = (TextView) findViewById(R.id.tv_hello);
        final SpringAnimation springAnimation = new SpringAnimation(tvHello, DynamicAnimation.X);
        {
            Log.d("Spring", String.valueOf(tvHello.getX()));
            //springForce.setFinalPosition(tvHello.getX());// create 时设置的 value 为0;
        }
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpringForce springForce = new SpringForce();
                springForce.setFinalPosition(tvHello.getX());
                springForce.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);//弹性阻尼
                springForce.setStiffness(SpringForce.STIFFNESS_LOW); //生硬度
                springAnimation.setSpring(springForce);
                springAnimation.setStartVelocity(3000f);
                springAnimation.start();
            }
        });
    }

}

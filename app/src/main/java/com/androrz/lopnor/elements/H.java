package com.androrz.lopnor.elements;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androrz.lopnor.R;
import com.androrz.lopnor.proton.BreathAnim;

public class H extends AppCompatActivity {

    private TextView mTargetView;
    AnimatorSet mSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h);
        mTargetView = (TextView) findViewById(R.id.textView);
    }

    public void startAnim(View view) {
        //Let's change background's color from blue to red.
        ColorDrawable[] color = {new ColorDrawable(Color.BLUE), new ColorDrawable(Color.RED)};
        final TransitionDrawable trans = new TransitionDrawable(color);
        //This will work also on old devices. The latest API says you have to use setBackground instead.
        mTargetView.setBackground(trans);
        trans.reverseTransition(3000);

        final BreathAnim ba = new BreathAnim(this, R.anim.anim_breath, mTargetView, new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                trans.reverseTransition(3000);
                animation.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mSet = ba.getAnimatorSet();
        ba.start();
    }
}

package com.androrz.lopnor.proton;


import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;

public class BreathAnim {
    AnimatorSet mSet;

    public BreathAnim(Context ctx, int animRid, Object target, Animator.AnimatorListener listener) {
        mSet = (AnimatorSet) AnimatorInflater.loadAnimator(ctx, animRid);
        mSet.setTarget(target);
        mSet.addListener(listener);
    }

    public void start() {
        mSet.start();
    }

    public void cancel() {
        mSet.cancel();
        mSet = null;
    }

    public AnimatorSet getAnimatorSet() {
        return mSet;
    }
}

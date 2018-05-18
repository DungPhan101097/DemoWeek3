package com.example.lap10715.demoweek3.demo_live_fb;

import android.view.animation.Animation;

public class CustomAnimationListener implements Animation.AnimationListener {
    @Override
    public void onAnimationStart(Animation animation) {
        System.out.println("on Animation start");
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        System.out.println("on Animation end");
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}

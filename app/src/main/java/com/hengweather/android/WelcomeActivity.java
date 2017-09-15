package com.hengweather.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

public class WelcomeActivity extends BaseActivity {

    private ImageView welcomeImg = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);
        welcomeImg = (ImageView) this.findViewById(R.id.welcome_img);
        AlphaAnimation anima = new AlphaAnimation(0.3f, 1.0f);
        anima.setDuration(2000);// 设置动画显示时间
        welcomeImg.startAnimation(anima);
        anima.setAnimationListener(new AnimationImpl());
    }

    private class AnimationImpl implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
            welcomeImg.setBackgroundResource(R.drawable.welcome);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            skip(); // 动画结束后跳转到别的页面
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

    }

    private void skip() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}

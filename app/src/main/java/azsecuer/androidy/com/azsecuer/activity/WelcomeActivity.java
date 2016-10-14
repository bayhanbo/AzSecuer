package azsecuer.androidy.com.azsecuer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import azsecuer.androidy.com.azsecuer.R;

public class WelcomeActivity extends Activity implements Animation.AnimationListener {
    private ImageView iv_welcome_icon1, iv_welcome_icon2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();
        initActivity();
    }
    private void initView(){
        iv_welcome_icon1 = (ImageView) findViewById(R.id.iv_welcome_icon1);
        iv_welcome_icon2 = (ImageView) findViewById(R.id.iv_welcome_icon2);
    }
    private void initActivity(){
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.anim_welcome_icon1);
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.anim_weicome_icon2);
        iv_welcome_icon1.startAnimation(animation1);
        iv_welcome_icon2.startAnimation(animation2);
        animation1.setAnimationListener(this);
    }
    @Override
    public void onAnimationStart(Animation animation){
    }
    @Override
    public void onAnimationEnd(Animation animation){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onAnimationRepeat(Animation animation){
    }
}

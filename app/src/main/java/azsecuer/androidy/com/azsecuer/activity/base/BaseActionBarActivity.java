package azsecuer.androidy.com.azsecuer.activity.base;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import azsecuer.androidy.com.azsecuer.R;
import azsecuer.androidy.com.azsecuer.activity.util.LogUtil;

/**
 * Created by lenovo on 2016/8/4.
 */
public abstract class BaseActionBarActivity extends BaseActivity{
    ImageView iv_actionbar_left_icon,iv_actionbar_right_icon;
    TextView tv_actionbar_title;
/**
绑定actionBar的控件
 */
    private void findActionBarView() throws NotFoundActionBarException{
        iv_actionbar_left_icon=(ImageView)findViewById(R.id.iv_actionbar_left_icon);
        iv_actionbar_right_icon=(ImageView)findViewById(R.id.iv_actionbar_rigth_icon);
        tv_actionbar_title=(TextView)findViewById(R.id.tv_actionbar_title);
    }
    class NotFoundActionBarException extends Exception{
        public NotFoundActionBarException(){
            super("是否有ActionBar？这里没有找到");
        }
    }
/**
leftIconResId 左边的资源文件id
rightIconResId 右边的资源文件id
title界面信息
规定：当传入id为-1的情况下，表示当前的控件没有作用 设置为不可见
 */
    protected void setActionBar(int leftIconResId,int rightIconResId,String actionBarTitle){
        try {
            findActionBarView();
        } catch (NotFoundActionBarException e) {
            LogUtil.p("NotFoundActionBarException","没有找到");
        }
        if(leftIconResId==-1){
            iv_actionbar_left_icon.setVisibility(View.INVISIBLE);//设置为不可见
        }else{
            iv_actionbar_left_icon.setImageResource(leftIconResId);
        }
        if(rightIconResId==-1){
            iv_actionbar_right_icon.setVisibility(View.INVISIBLE);//设置为不可见
        }else{
            iv_actionbar_right_icon.setImageResource(rightIconResId);
        }
        if(actionBarTitle==null){
            tv_actionbar_title.setVisibility(View.INVISIBLE);//设置为不可见
        }else{
            tv_actionbar_title.setText(actionBarTitle);
        }
    }
/*
返回型ActionBar
 */
//    protected void setActionBar(String actionBarTitle){
//        setActionBar(R.drawable.btn_homeasup_default,-1,actionBarTitle);
//    }
/**
主界面
 */
    protected void setActionBarHome(){
        setActionBar(R.drawable.ic_launcher,R.drawable.ic_child_configs,getResources().getString(R.string.app_name1));
    }
/**
提供给其他界面的ActionBar设置方法
 */
    protected void setActionBarBack(String title){
        setActionBar(R.drawable.btn_homeasup_default,-1,title);
    }
}

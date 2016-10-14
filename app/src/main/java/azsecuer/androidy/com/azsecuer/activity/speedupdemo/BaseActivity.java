package azsecuer.androidy.com.azsecuer.activity.speedupdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * @author: leejohngoodgame
 * @date: 2016/8/3 13:57
 * @email:18328541378@163.com
 * 所有activity的基类
 */
public abstract class BaseActivity extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        listenerView();
        initData();

    }

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 初始化控件 findViewById()
     */
    public abstract void initView();

    /**
     * 对控件进行监听事件
     */
    public abstract void listenerView();

    /**
     * 无数据传递的Activity
     * @param targetClass 要跳转的Activity
     */
    protected void startActivity(Class<?> targetClass){
        Intent intent = new Intent(this,targetClass);//
        startActivity(intent);
    }

    /**
     * 带有Bundle数据的跳转
     * @param targetClass 跳转的Activity
     * @param bundle 携带数据的Bundle
     */
    protected void startActivity(Class<?> targetClass,Bundle bundle){
        Intent intent = new Intent(this,targetClass);//
        intent.putExtras(bundle);
        startActivity(intent);
    }
}

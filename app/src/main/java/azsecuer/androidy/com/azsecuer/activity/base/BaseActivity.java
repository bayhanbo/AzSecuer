package azsecuer.androidy.com.azsecuer.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by lenovo on 2016/8/3.
 */
public abstract class BaseActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /*
     初始化控件 控件的监听
     */
    public abstract void initView();
    public abstract void listenerView();

    /*
     界面跳转问题
     */
    protected void startActivity(Class<?> targetClass) {
        Intent intent = new Intent(this,targetClass);
        startActivity(intent);
    }

    /*
     带有Bundle数据的跳转
     */
    protected void startActivity(Class<?> targetClass, Bundle bundle) {
        Intent intent = new Intent(this, targetClass);//
        intent.putExtras(bundle);
        startActivity(intent);
    }


}

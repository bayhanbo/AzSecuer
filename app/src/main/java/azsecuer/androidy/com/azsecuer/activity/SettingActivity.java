package azsecuer.androidy.com.azsecuer.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import azsecuer.androidy.com.azsecuer.R;
import azsecuer.androidy.com.azsecuer.activity.base.BaseActionBarActivity;
import azsecuer.androidy.com.azsecuer.activity.config.SettingPrefs;
import azsecuer.androidy.com.azsecuer.activity.notification.MainNotification;
import azsecuer.androidy.com.azsecuer.activity.util.LogUtil;

public class SettingActivity extends BaseActionBarActivity implements CompoundButton.OnCheckedChangeListener{
    private ToggleButton toggleButton_setting_notifi_action,toggleButton_setting_auto_start;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.activity_setting);
        super.onCreate(savedInstanceState);
        setActionBarBack("设置");//todo 设置ActionBar
        initView();
        listenerView();


    }
    /**
     * 当初始化控件的时候 将按钮的状态从SharedPreferences读取出来
     */
    @Override
    public void initView(){
        toggleButton_setting_notifi_action = (ToggleButton)findViewById(R.id.tbtn_setting_notifyaction);
        toggleButton_setting_auto_start=(ToggleButton)findViewById(R.id.tbtn_setting_autostart);
        toggleButton_setting_notifi_action.setChecked(SettingPrefs.getNotification(this));
        toggleButton_setting_auto_start.setChecked(SettingPrefs.getAutoStart(this));//todo 设置
        LogUtil.p("SettingActivity","读取并且设置完成");
    }
    @Override
    public void listenerView(){
        toggleButton_setting_notifi_action.setOnCheckedChangeListener(this);
        toggleButton_setting_auto_start.setOnCheckedChangeListener(this);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
    /**
     *  监听事件 返回
     *  需要注意的是这里应该是finish() 而不是startActivity
     * @param view
     */
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_actionbar_left_icon:
                this.finish();
                break;
        }
    }
    /**
     * 按钮状态切换的监听者
     * 在按钮状态被改变的时候进行SharedPrefrences的写入操作
     * @param buttonView 更改的按钮
     * @param isChecked 开启状态
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
        switch (buttonView.getId()){
            case  R.id.tbtn_setting_autostart:
                SettingPrefs.saveAutoStart(this,isChecked);
                break;
            case  R.id.tbtn_setting_notifyaction:
                SettingPrefs.saveNotifiaction(this,isChecked);
                if(isChecked){
                    MainNotification.openNotifition(this); //todo 开启通知栏消息
                }else{
                    MainNotification.closeNotifition(this);//todo　关闭
                }
                break;
        }
    }
}

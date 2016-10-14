package azsecuer.androidy.com.azsecuer.activity;

import android.app.ActivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import azsecuer.androidy.com.azsecuer.R;
import azsecuer.androidy.com.azsecuer.activity.base.BaseActionBarActivity;
import azsecuer.androidy.com.azsecuer.activity.speedupdemo.SpeedupActivity;
import azsecuer.androidy.com.azsecuer.activity.util.LogUtil;
import azsecuer.androidy.com.azsecuer.activity.view.CustomProc;

/**
 * Created by lenovo on 2016/8/3.
 */
public class HomeActivity extends BaseActionBarActivity {
    private static final String TAG = "HomeActivity";
    CustomProc customProc;
    private int temp = 0;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            customProc.setProgress(temp);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newhome);
        setActionBarHome();
        initView();
        listenerView();
        asyncThread();
    }

    @Override
    public void listenerView() {
        LogUtil.p(TAG, "listenerView");
        findViewById(R.id.iv_actionbar_rigth_icon).setOnClickListener(this);
        customProc.setOnClickListener(this);
        findViewById(R.id.bt_home_speed).setOnClickListener(this);
        findViewById(R.id.tv_home_phoneSpeedup).setOnClickListener(this);
        findViewById(R.id.tv_home_softwareManage).setOnClickListener(this);
        findViewById(R.id.tv_home_phoneDetection).setOnClickListener(this);
        findViewById(R.id.tv_home_numberservice).setOnClickListener(this);
        findViewById(R.id.tv_home_fileManage).setOnClickListener(this);
        findViewById(R.id.tv_home_junkClear).setOnClickListener(this);
        customProc.setOnClickListener(this);
    }
    @Override
    public void initView() {
        LogUtil.p(TAG, "initView");
        customProc = (CustomProc) findViewById(R.id.customPrco);
//        customProc.startAnimSetProgress3(99);//todo 模拟百分比
    }
    public void asyncThread(){
        final ActivityManager activityManager =(ActivityManager)getSystemService(ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> arrayList =activityManager.getRunningAppProcesses();
        new Thread(){
            @Override
            public void run(){
                for (int i = 0; i < arrayList.size(); i++) {
                    handler.sendEmptyMessage(1);
                    temp = i;
                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                super.run();
            }
        }.start();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_actionbar_rigth_icon:
                startActivity(SettingActivity.class);
                break;
            case R.id.customPrco:
                //todo 杀死当前运行进程
                ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                for (ActivityManager.RunningAppProcessInfo process : activityManager.getRunningAppProcesses()){
                    activityManager.killBackgroundProcesses(process.processName);
                }
                asyncThread();
                break;
            case R.id.tv_home_phoneSpeedup:
                startActivity(SpeedupActivity.class);
                break;
            case R.id.tv_home_softwareManage:
                startActivity(SoftwareManageActivity.class);
                break;
            case R.id.tv_home_phoneDetection:
                startActivity(MobileInfoActivity.class);
                break;
            case R.id.tv_home_numberservice:
                startActivity(TelTypeActivity.class);
                break;
            case R.id.tv_home_fileManage:
                startActivity(FileManageActivity.class);
                break;
            case R.id.tv_home_junkClear:
                startActivity(JunkClearActivity.class);
                break;
        }
    }
}

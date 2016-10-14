package azsecuer.androidy.com.azsecuer.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.List;

import azsecuer.androidy.com.azsecuer.R;
import azsecuer.androidy.com.azsecuer.activity.adapter.MobileInfoAdapter;
import azsecuer.androidy.com.azsecuer.activity.base.BaseActionBarActivity;
import azsecuer.androidy.com.azsecuer.activity.entity.MobileChildInfo;
import azsecuer.androidy.com.azsecuer.activity.entity.MobileGroupInfo;
import azsecuer.androidy.com.azsecuer.activity.mgr.MobileManager;
import azsecuer.androidy.com.azsecuer.activity.view.MoveProgressBar;

/**
 * Created by lenovo on 2016/8/15.
 */
public class MobileInfoActivity extends BaseActionBarActivity{
    private BatteryReceiver batteryReceiver;
    private TextView tv_power_info;
    private MoveProgressBar pg_power;
//    private int tempPower = 0;
    private ExpandableListView expandableListView;
    private MobileInfoAdapter adapter;
    private MobileManager mobileManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_mobile_info);
        super.onCreate(savedInstanceState);
        setActionBarBack("手机检测");
        initData();
        initView();
        batteryReceiver=new BatteryReceiver();
        //todo 动态注册 一般来讲成对使用
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver,intentFilter);
    }
    private void initData(){
        adapter=new MobileInfoAdapter(this);
        mobileManager=new MobileManager(this);
        aysnLoad();
    }
    /**
     * 开启异步加载数据
     * 避免阻塞UI线程
     */
    private void aysnLoad(){
        final ProgressDialog progressDialog=ProgressDialog.show(this,null,"正在加载中...请稍等",false,true);
        new Thread(){
            @Override
            public void run(){
                final MobileGroupInfo phoneInfoGroup = new MobileGroupInfo(getResources().getDrawable(R.drawable.setting_info_icon_version), "设备信息");
                final List<MobileChildInfo> phonnChild = mobileManager.getPhoneMessage();
                final MobileGroupInfo sytemInfoGroup =new MobileGroupInfo(getResources().getDrawable(R.drawable.setting_info_icon_cpu), "系统信息");
                final List<MobileChildInfo> systemChild = mobileManager.getSystemMessage();
                final MobileGroupInfo netInfoGroup = new MobileGroupInfo(getResources().getDrawable(R.drawable.setting_info_icon_root), "网络信息");
                final List<MobileChildInfo> netChild = mobileManager.getWIFIMessage();
                final MobileGroupInfo creamInfoGroup = new MobileGroupInfo(getResources().getDrawable(R.drawable.setting_info_icon_camera), "相机信息");
                final List<MobileChildInfo> creamChild = mobileManager.getCameraMessage();
                final MobileGroupInfo ramInfoFroup = new MobileGroupInfo(getResources().getDrawable(R.drawable.setting_info_icon_space), "存储信息");
                final List<MobileChildInfo> ramChild = mobileManager.getMemoryMessage(MobileInfoActivity.this);
                /**
                 * 回到ui
                 * 跟handler非常相似
                 */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addDataToAdapter(phoneInfoGroup, phonnChild);
                        adapter.addDataToAdapter(sytemInfoGroup,systemChild);
                        adapter.addDataToAdapter(netInfoGroup,netChild );
                        adapter.addDataToAdapter(creamInfoGroup, creamChild);
                        adapter.addDataToAdapter(ramInfoFroup,ramChild);
                        adapter.notifyDataSetChanged();
                        progressDialog.cancel();
                    }
                });
                super.run();
            }
        }.start();
    }
    /**
     * 接收到广播以后
     * 1.设置Text
     * 2.设置Progress
     */
    private class BatteryReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context,Intent intent){
            final int currentPower = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
            tv_power_info.setText(currentPower+"%");
            pg_power.setProgressMoved(currentPower);
        }
    }
    @Override
    public void initView(){
        tv_power_info=(TextView)findViewById(R.id.tv_power_info);
        pg_power=(MoveProgressBar)findViewById(R.id.progressBar);
        expandableListView=(ExpandableListView)findViewById(R.id.elv_mobile);
        expandableListView.setAdapter(adapter);
    }
    @Override
    public void listenerView() {
    }
    @Override
    protected void onDestroy() {
        unregisterReceiver(batteryReceiver);//todo 取消广播
        super.onDestroy();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_actionbar_left_icon:
                finish();
                break;
        }
    }
}

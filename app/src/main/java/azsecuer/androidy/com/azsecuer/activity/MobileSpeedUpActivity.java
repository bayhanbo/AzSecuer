package azsecuer.androidy.com.azsecuer.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import azsecuer.androidy.com.azsecuer.R;
import azsecuer.androidy.com.azsecuer.activity.adapter.MobileSpeedUpAdapter;
import azsecuer.androidy.com.azsecuer.activity.base.BaseActionBarActivity;
import azsecuer.androidy.com.azsecuer.activity.db.MobileSpeedUpManager;
import azsecuer.androidy.com.azsecuer.activity.entity.MobileSpeedUpInfo;
import azsecuer.androidy.com.azsecuer.activity.mgr.MemoryManager;
import azsecuer.androidy.com.azsecuer.activity.speedupdemo.AutomoveProgressBar;
import azsecuer.androidy.com.azsecuer.activity.util.PublicUtils;

public class MobileSpeedUpActivity extends BaseActionBarActivity implements CompoundButton.OnCheckedChangeListener{
    private TextView tv_speedup_one,tv_speedup_two,tv_speedup_ram;//todo 手机品牌，型号，运行内存文本信息
    private AutomoveProgressBar moveProgressBar;//todo 运行内存进度条
    private Button bt_speedup_system;//todo 显示or隐藏系统进程按钮

    private ListView lv_loading_listview;//todo 正在运行进程列表
    private ProgressBar progressBar;
    private MobileSpeedUpAdapter mobileSpeedUpAdapter;//todo 手机加速列表(正在运行的进程)适配器

    private View speedup_include_checked_button;//todo 底部checkAndButton视图(在加载中时是隐藏的)
    private CheckBox cb_speedup_checked; //todo 全选
    private Button bt_speedup_clear;//todo 一键清理按钮
    @Override
    protected void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.activity_mobile_speed_up);
        super.onCreate(savedInstanceState);
        setActionBarBack("手机加速");
        initView();
        listenerView();
        initData();
    }
    public void initData(){
        asyncLoadRunningApp();
    }
    @Override
    public void initView(){
        tv_speedup_one=(TextView)findViewById(R.id.tv_speedup_one);
        tv_speedup_two=(TextView)findViewById(R.id.tv_speedup_two);
        tv_speedup_ram=(TextView)findViewById(R.id.tv_speedup_ramMassage);
        moveProgressBar=(AutomoveProgressBar)findViewById(R.id.progressBar_speedup);
        bt_speedup_system=(Button)findViewById(R.id.bt_speedup_system);
        bt_speedup_system.setText(R.string.mobile_speedup_system_show);

        lv_loading_listview=(ListView)findViewById(R.id.lv_loading_listview);
        progressBar=(ProgressBar)findViewById(R.id.pb_loading);
        mobileSpeedUpAdapter=new MobileSpeedUpAdapter(this);
        lv_loading_listview.setAdapter(mobileSpeedUpAdapter);

        speedup_include_checked_button=(View)findViewById(R.id.speedup_include_checked_button);
        cb_speedup_checked=(CheckBox)findViewById(R.id.cb_speedup_checked);
        bt_speedup_clear=(Button)findViewById(R.id.bt_speedup_clear);
        bt_speedup_clear.setText(R.string.mobile_speedup_clear);
    }
    @Override
    public void listenerView(){
        bt_speedup_system.setOnClickListener(this);
        bt_speedup_clear.setOnClickListener(this);
        cb_speedup_checked.setOnCheckedChangeListener(this);
    }
    private void asyncLoadRunningApp(){
        //todo 更新运行内存信息
        updateRam();
        speedup_include_checked_button.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        lv_loading_listview.setVisibility(View.INVISIBLE);
        //todo 新线程异步处理加载正在运行的进程
        //todo 线程这样随意处理，不做管理其实还是很危险的
        new Thread(new Runnable() {
            @Override
            public void run() {
                //todo 加载数据(使用写好的手机加速业务逻辑处理)
                MobileSpeedUpManager sm = MobileSpeedUpManager.getInstance(MobileSpeedUpActivity.this);
                List<MobileSpeedUpInfo> speedupInfos = null;
                //todo 将用户进程重置到适配器
                speedupInfos = sm.getRuningApp(MobileSpeedUpManager.CLASSIFY_USER,true);
                mobileSpeedUpAdapter.resetDataToAdapter(speedupInfos);//todo　setDatas
                //todo UI更新操作
                runOnUiThread(new Runnable() {
                    @Override
                    public void run(){
                        mobileSpeedUpAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.INVISIBLE);
                        lv_loading_listview.setVisibility(View.VISIBLE);
                        speedup_include_checked_button.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();
    }
    /**
     * 内存信息獲取 和設置
     */
    private void updateRam(){
        //todo 获取运行内存数据信息(全部，空闲可用，已使用)
        tv_speedup_one.setText(Build.BRAND.toUpperCase());
        tv_speedup_two.setText(Build.MODEL.toUpperCase());
        final int temp=(int)(((double)(MemoryManager.getTotalRamMemory()//todo 求的是内存使用所占比例
                -MemoryManager.getAvailRamMemory(this))
                /MemoryManager.getTotalRamMemory())*100);
        tv_speedup_ram.setText("内存信息:"+ PublicUtils.formatSize(MemoryManager.getAvailRamMemory(this))+
                "/"+PublicUtils.formatSize(MemoryManager.getTotalRamMemory()));
        moveProgressBar.setProgressAutomove1(temp);
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
        List<MobileSpeedUpInfo> infos = mobileSpeedUpAdapter.getDataFromAdapter();
        for (MobileSpeedUpInfo info : infos) {
            info.isSelected = isChecked;
        }
        mobileSpeedUpAdapter.notifyDataSetChanged();
    }
    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.iv_actionbar_left_icon://todo 返回
                finish();
                break;
            case R.id.bt_speedup_system://todo 显示、隐藏系统进程
                MobileSpeedUpManager sm = MobileSpeedUpManager.getInstance(MobileSpeedUpActivity.this);
                List<MobileSpeedUpInfo> sysInfos = sm.getRuningApp(MobileSpeedUpManager.CLASSIFY_SYS,false);
                //todo 当前按钮显示的文本
                String button_text = bt_speedup_system.getText().toString();
                String button_text_show = getResources().getString(R.string.mobile_speedup_system_show);
                String button_text_hide = getResources().getString(R.string.mobile_speedup_system_hide);
                //todo 当前按钮显示的文本为: 隐藏系统进程
                if(button_text.equals(button_text_hide)) {
                    //todo 将所有系统进程(无需重新加载), 从adapter中移除
                    mobileSpeedUpAdapter.getDataFromAdapter().removeAll(sysInfos);
                    bt_speedup_system.setText(R.string.mobile_speedup_system_show);
                    mobileSpeedUpAdapter.notifyDataSetChanged();
                    lv_loading_listview.setSelection(0); //todo 定位到0位置
                }
                //todo 当前按钮显示的文本为: 显示系统进程
                if (button_text.equals(button_text_show)) {
                    //todo 将所有系统进程(无需重新加载), 添加到adapter中
                    mobileSpeedUpAdapter.getDataFromAdapter().addAll(sysInfos);
                    bt_speedup_system.setText(R.string.mobile_speedup_system_hide);
                    mobileSpeedUpAdapter.notifyDataSetChanged();
                    lv_loading_listview.setSelection(mobileSpeedUpAdapter.getCount() - 1); //todo 定位到最后位置
                }
            case R.id.bt_speedup_clear://todo 一键清理
                List<MobileSpeedUpInfo> infos = mobileSpeedUpAdapter.getDataFromAdapter();
                for (MobileSpeedUpInfo info : infos) {
                    if (info.isSelected) {
                        String packageName = info.processName;
                        MobileSpeedUpManager.getInstance(this).kill(packageName);
                    }
                }
                //todo 在重新加载所有正在运行进程前 重置UI
                cb_speedup_checked.setChecked(false);
                //todo 在重新加载所有正在运行进程前 重置UI
                bt_speedup_system.setText(R.string.mobile_speedup_system_show);
                //todo 重新加载所有正在运行进程(此方法 加载所有,但只添加显示用户进程)
                asyncLoadRunningApp();
                break;
        }
    }
}

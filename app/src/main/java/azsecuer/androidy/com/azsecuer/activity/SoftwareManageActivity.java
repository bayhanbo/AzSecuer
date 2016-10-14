package azsecuer.androidy.com.azsecuer.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import azsecuer.androidy.com.azsecuer.R;
import azsecuer.androidy.com.azsecuer.activity.base.BaseActionBarActivity;
import azsecuer.androidy.com.azsecuer.activity.softwaerManager.NewMemoryManager;
import azsecuer.androidy.com.azsecuer.activity.softwaerManager.PiecharView;
import azsecuer.androidy.com.azsecuer.activity.softwaerManager.SoftwareAllActivity;
import azsecuer.androidy.com.azsecuer.activity.softwaerManager.SoftwareManager;
import azsecuer.androidy.com.azsecuer.activity.speedupdemo.AutomoveProgressBar;

public class SoftwareManageActivity extends BaseActionBarActivity implements View.OnClickListener{
    private TextView tv_room_in, tv_room_out, tv_software_ramMassage_room_in, tv_software_ramMassage_room_out;
    private AutomoveProgressBar moveProgressBarIn,moveProgressBarOut;
    private int inRomProp360,outRomProp360;
    private PiecharView piecharView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_software_manage);
        setActionBarBack("软件管理");
        initView();
        listenerView();
    }
    @Override
    public void initView(){
        //todo 上部结构
        tv_room_in = (TextView) findViewById(R.id.tv_room_in);
        tv_room_out = (TextView) findViewById(R.id.tv_room_out);
        piecharView = (PiecharView) findViewById(R.id.software_manager_icon);
        String inRoom = NewMemoryManager.formatFileSize(NewMemoryManager.getTotalInternalMemorySize(),true);
        String outRoom = NewMemoryManager.formatFileSize(NewMemoryManager.getTotalExternalMemorySize(),true);
        tv_room_in.setText(inRoom);
        tv_room_out.setText(outRoom);
        //todo 中部结构1
        tv_software_ramMassage_room_in = (TextView) findViewById(R.id.tv_software_ramMassage_room_in);
        moveProgressBarIn=(AutomoveProgressBar)findViewById(R.id.progressBar_software_in);
        final long useIn =NewMemoryManager.getTotalInternalMemorySize()-NewMemoryManager.getAvailableInternalMemorySize();
        moveProgressBarIn = (AutomoveProgressBar) findViewById(R.id.progressBar_software_in);
        tv_software_ramMassage_room_in.setText(NewMemoryManager.formatFileSize(useIn,true)+"/"+inRoom);
        final int ratioIn=(int)((float)useIn/(float)NewMemoryManager.getTotalInternalMemorySize()*100);
        moveProgressBarIn.setMax(100);
        moveProgressBarIn.setProgressAutomove1(ratioIn);
        //todo 中部结构2
        tv_software_ramMassage_room_out = (TextView) findViewById(R.id.tv_software_ramMassage_room_out);
        moveProgressBarIn=(AutomoveProgressBar)findViewById(R.id.progressBar_software_out);
        final long useOut=NewMemoryManager.getTotalExternalMemorySize()-NewMemoryManager.getAvailableExternalMemorySize();
        moveProgressBarOut = (AutomoveProgressBar) findViewById(R.id.progressBar_software_out);
        tv_software_ramMassage_room_out.setText(NewMemoryManager.formatFileSize(useOut,true)+"/"+outRoom);
        final int ratioOut=(int)((float)useIn/(float)NewMemoryManager.getTotalExternalMemorySize()*100);
        moveProgressBarOut.setMax(100);
        moveProgressBarOut.setProgressAutomove1(ratioOut);
        //todo 下部结构
        //todo 计算出饼状图 的角度比例
        long totalRom = NewMemoryManager.getTotalInternalMemorySize() + NewMemoryManager.getTotalExternalMemorySize() ;
        inRomProp360 = (int) ((float) NewMemoryManager.getTotalInternalMemorySize() / (float) totalRom * 360);
        outRomProp360 = 360 - inRomProp360;
        int data[][] = new int[][]{{this.getResources().getColor(R.color.progress_bg),inRomProp360,0},
                {this.getResources().getColor(R.color.software_ball_bg),outRomProp360,0}};
        piecharView.setAngleWithAnim2(data);
    }
    @Override
    public void listenerView(){
        findViewById(R.id.tv_software_all).setOnClickListener(this);
        findViewById(R.id.tv_software_system).setOnClickListener(this);
        findViewById(R.id.tv_software_user).setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.iv_actionbar_left_icon:
                finish();
                break;
            case R.id.tv_software_all://todo all
                bundle.putInt("soft_type",SoftwareManager.CLASSIFY_ALL);
                startActivity(SoftwareAllActivity.class,bundle);
                break;
            case R.id.tv_software_system://todo system
                bundle.putInt("soft_type",SoftwareManager.CLASSIFY_SYS);
                startActivity(SoftwareAllActivity.class,bundle);
                break;
            case R.id.tv_software_user://todo user
                bundle.putInt("soft_type",SoftwareManager.CLASSIFY_USER);
                startActivity(SoftwareAllActivity.class,bundle);
                break;
        }
    }
}

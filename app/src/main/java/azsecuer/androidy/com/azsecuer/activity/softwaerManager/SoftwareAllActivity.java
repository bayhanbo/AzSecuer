package azsecuer.androidy.com.azsecuer.activity.softwaerManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.List;

import azsecuer.androidy.com.azsecuer.R;
import azsecuer.androidy.com.azsecuer.activity.base.BaseActionBarActivity;
import azsecuer.androidy.com.azsecuer.activity.speedupdemo.LogUtil;

public class SoftwareAllActivity extends BaseActionBarActivity implements AdapterView.OnItemClickListener{
    private List<SoftwareBrowseInfo> softwareBrowseInfos;
    private ListView listView;
    private ProgressBar loadding_proc;
    private SoftBowserAdapter adapter;
    //todo 生命周期打印测试
    @Override
    protected void onStart(){
        super.onStart();
        LogUtil.d("TAG","onStart");
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        LogUtil.d("TAG","onRestart");
    }
    @Override
    protected void onResume() {
        super.onResume();
        syncLoadData();
        LogUtil.d("TAG","onResume");
    }
    @Override
    protected void onStop(){
        super.onStop();
        LogUtil.d("TAG","onStop");
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        LogUtil.d("TAG","onDestroy");
    }
    @Override
    protected void onPause(){
        super.onPause();
        LogUtil.d("TAG","onPause");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.activity_software_all);
        super.onCreate(savedInstanceState);
        setActionBarBack("软件管理操作");
        initView();listenerView();
    }
    private void syncLoadData(){
        final int softType = this.getIntent().getExtras().getInt("soft_type");
        new Thread(){
            @Override
            public void run(){
                softwareBrowseInfos = SoftwareManager.getInstance(SoftwareAllActivity.this).getSoftware(softType);
                adapter.setDatasToAdapter(softwareBrowseInfos);
                runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        listView.setVisibility(View.VISIBLE);
                        loadding_proc.setVisibility(View.INVISIBLE);
                        adapter.notifyDataSetChanged();
                    }
                });
                super.run();
            }
        }.start();
    }
    @Override
    public void initView(){
        listView = (ListView)findViewById(R.id.lv_software_all);
        loadding_proc = (ProgressBar)findViewById(R.id.pb_loading_soft_all);
        adapter = new SoftBowserAdapter(this);
        listView.setAdapter(adapter);
    }
    @Override
    public void listenerView(){
        listView.setOnItemClickListener(this);
        LogUtil.d("TAG","setOnClick");
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_actionbar_left_icon:
                finish();
                break;
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SoftwareBrowseInfo info = adapter.getItem(position);
        LogUtil.d("TAG",info.packagename);
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + info.packagename));
        startActivity(intent);
    }
}

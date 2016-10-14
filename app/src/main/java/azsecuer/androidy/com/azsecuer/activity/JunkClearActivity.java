package azsecuer.androidy.com.azsecuer.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import azsecuer.androidy.com.azsecuer.R;
import azsecuer.androidy.com.azsecuer.activity.adapter.JunkClearAdapter;
import azsecuer.androidy.com.azsecuer.activity.base.BaseActionBarActivity;
import azsecuer.androidy.com.azsecuer.activity.db.AssetsDBManager;
import azsecuer.androidy.com.azsecuer.activity.db.DBManager;
import azsecuer.androidy.com.azsecuer.activity.entity.JunkClearInfo;
import azsecuer.androidy.com.azsecuer.activity.util.FileUtils;

/**
 * 垃圾清理模块
 */
public class JunkClearActivity extends BaseActionBarActivity{
    private ProgressBar progressBar;
    private ListView listView;
    private List<JunkClearInfo> clearInfoList;
    private JunkClearAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.activity_junk_clear);
        super.onCreate(savedInstanceState);
        initDatas();
        initView();
        listenerView();
    }
    public void initDatas(){
        try {
            DBManager.createFileClear(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(DBManager.dbFileIsExists()){
            try {
                AssetsDBManager.copyDBFileToDB(this,"clearpath.db",DBManager.fileDB);
                Log.i("copyDBFileToDB","-----");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Log.i("NotcopyDBFileToDB","-----");
        }
        asynLoadClearInfo();
    }
    /**异步加载数据*/
    private void asynLoadClearInfo(){
        clearInfoList=new ArrayList<>();
        adapter=new JunkClearAdapter(this);
        new Thread(){
            @Override
            public void run() {
                super.run();
                //TODO 读取数据库
                clearInfoList=DBManager.readTabelJunkFile(JunkClearActivity.this);
                //todo 回到uiThread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setDatas(clearInfoList);
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.INVISIBLE);
                        listView.setVisibility(View.VISIBLE);
                    }
                });
            }
        }.start();
    }
    @Override
    public void initView(){
        setActionBarBack("垃圾清理");
        listView=(ListView)findViewById(R.id.lv_loading_listview);
        progressBar=(ProgressBar)findViewById(R.id.pb_loading);
        listView.setAdapter(adapter);
    }
    @Override
    public void listenerView(){
        findViewById(R.id.bt_clear_button).setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.iv_actionbar_left_icon:
                finish();
                break;
            case R.id.bt_clear_button:
                asynDeleteFile();
                break;
        }
    }
    /**
     *  异步删除数据
     */
    private void asynDeleteFile(){
        //todo　设置
        listView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        new Thread(){
            @Override
            public void run() {
                //todo　获得迭代器对象
                Iterator<JunkClearInfo> iterator = clearInfoList.iterator();
                //todo  进行删除的操作
                while(iterator.hasNext()){
                    JunkClearInfo junkClearInfo = iterator.next();
                    //todo 判定当前的选择状态
                    if(junkClearInfo.isChecked){
                        //todo　删除文件 调用删除文件的路径
                        File file = new File(junkClearInfo.filePath);
                        //todo  执行删除文件的操作
                        FileUtils.deleteFile(file);
                        //todo 集合中对应的也删除掉
                        iterator.remove();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //todo　重新设置数据　并且提醒更新　刷新列表
                        adapter.setDatas(clearInfoList);
                        adapter.notifyDataSetChanged();
                        //todo  重新设置可见属性
                        listView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
                super.run();
            }
        }.start();
    }
}

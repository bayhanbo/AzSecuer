package azsecuer.androidy.com.azsecuer.activity.fileManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import azsecuer.androidy.com.azsecuer.R;
import azsecuer.androidy.com.azsecuer.activity.base.BaseActionBarActivity;
import azsecuer.androidy.com.azsecuer.activity.util.FileUtils;
import azsecuer.androidy.com.azsecuer.activity.util.PublicUtils;

public class FileManagerListActivity extends BaseActionBarActivity implements AdapterView.OnItemClickListener,CompoundButton.OnCheckedChangeListener{
    private TextView tv_filebrowse_number;
    private TextView tv_filebrowse_size;
    private ListView lv_file_context;
    private Button btn_delfile;
    private CheckBox cb_deffile;
    private  long temp;
    private ProgressBar progressBar;
    private FileManagerAdapter fileManagerAdapter;
    private FileSearchManager searchManager;
    /** 文件类型(key) */
    private String fileType;
    /** 所有文件分类集合 */
    private HashMap<String,ArrayList<FileManagerInfo>> fileInfos;
    /** 所有文件大小集合 */
    private HashMap<String, Long> fileSizes;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.activity_file_manager_list);
        super.onCreate(savedInstanceState);
        setActionBarBack("文件浏览");
        initView();
        listenerView();
        loadTheData();
    }
    @Override
    public void initView(){
        tv_filebrowse_number=(TextView)findViewById(R.id.tv_filebrowse_number);
        tv_filebrowse_size = (TextView) findViewById(R.id.tv_filebrowse_size);
        lv_file_context = (ListView) findViewById(R.id.lv_file_context);
        progressBar=(ProgressBar)findViewById(R.id.pb_loading);
        btn_delfile = (Button) findViewById(R.id.bt_speedup_clear);
        cb_deffile = (CheckBox) findViewById(R.id.cb_speedup_checked);
        btn_delfile.setText("删除所选文件");
    }
    @Override
    public void listenerView(){
        cb_deffile.setOnCheckedChangeListener(this);
        btn_delfile.setOnClickListener(this);
        lv_file_context.setOnItemClickListener(this);
    }
    private void loadTheData(){
        //todo 取得FileServcieActivity传入的文件类型
        fileType = getIntent().getStringExtra("fileType");
        //todo 取得文件列表数据信息
        searchManager = FileSearchManager.getInstance(false);
        fileInfos = searchManager.getFileInfos(); //todo 文件实体集合(Map)
        fileSizes = searchManager.getFileSizes(); //todo 文件大小集合(Map)
        long size = fileSizes.get(fileType);
        long count = fileInfos.get(fileType).size();
        //todo 将文件列表数量和大小分别设置到对应的文件控件上
        tv_filebrowse_number.setText("" + count);
        tv_filebrowse_size.setText(PublicUtils.formatSize(size-temp));
        //todo 将文件实体集合数据适配到文件列表控件上
        fileManagerAdapter = new FileManagerAdapter(this,lv_file_context);
        fileManagerAdapter.addDataToAdapter(fileInfos.get(fileType));
        lv_file_context.setAdapter(fileManagerAdapter);
    }
    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.iv_actionbar_left_icon:
                finish();
                break;
            case R.id.bt_speedup_clear:
                Toast.makeText(this,"删除文件",Toast.LENGTH_SHORT).show();
                asynDeleteFile();
                break;
        }
    }
    private void asynDeleteFile(){
        //todo　设置
        new Thread(){
            @Override
            public void run(){
                //todo　获得迭代器对象
                Iterator<FileManagerInfo> iterator = fileInfos.get(fileType).iterator();
                //todo  进行删除的操作
                while(iterator.hasNext()){
                    FileManagerInfo junkClearInfo = iterator.next();
                    //todo 判定当前的选择状态
                    if(junkClearInfo.isSelect){
                        temp += junkClearInfo.file.length();
                        //todo  执行删除文件的操作
                        FileUtils.deleteFile(junkClearInfo.file);
                        //todo 集合中对应的也删除掉
                        iterator.remove();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //todo　重新设置数据　并且提醒更新　刷新列表
                        fileManagerAdapter.setDatas(fileInfos.get(fileType));
                        fileManagerAdapter.notifyDataSetChanged();
                        loadTheData();
                    }
                });
                super.run();
            }
        }.start();
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
        int count=fileManagerAdapter.getCount();
        for(int i=0;i<count;i++){
            fileManagerAdapter.getItem(i).isSelect=isChecked;
        }
        fileManagerAdapter.notifyDataSetChanged();
    }   @Override
    public void onItemClick(AdapterView<?> parent,View view,int position,long id){
        FileManagerInfo fileInfo = fileManagerAdapter.getItem(position);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.fromFile(fileInfo.file);
        String type = fileInfo.openType;
        intent.setDataAndType(data,type);
        startActivity(intent);
    }

}

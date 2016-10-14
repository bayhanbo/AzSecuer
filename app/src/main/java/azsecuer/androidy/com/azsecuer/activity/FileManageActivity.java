package azsecuer.androidy.com.azsecuer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;

import azsecuer.androidy.com.azsecuer.R;
import azsecuer.androidy.com.azsecuer.activity.base.BaseActionBarActivity;
import azsecuer.androidy.com.azsecuer.activity.fileManager.FileClassInfo;
import azsecuer.androidy.com.azsecuer.activity.fileManager.FileManagerListActivity;
import azsecuer.androidy.com.azsecuer.activity.fileManager.FileSearchManager;
import azsecuer.androidy.com.azsecuer.activity.fileManager.FileSearchTypeEvent;
import azsecuer.androidy.com.azsecuer.activity.fileManager.FileServiceAdapter;
import azsecuer.androidy.com.azsecuer.activity.util.PublicUtils;

public class FileManageActivity extends BaseActionBarActivity implements View.OnClickListener,AdapterView.OnItemClickListener{
    private TextView tv_file_sizeAll;
    private ListView lv_file_listView;
    private Button bt_file_search;
    private FileServiceAdapter adapter;
    private FileSearchManager fileSearchManager;
    private HashMap<String,Long> fileSizes;
    private long totalSize; //todo 文件总大小(注意每次进入页面时的重置)
    private Thread searchThread;
    private Handler mainHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            int what = msg.what;
            switch (what) {
                //todo onSearchStart 当开始搜索时更新UI
                case 0:
                    bt_file_search.setClickable(false);
                    bt_file_search.setText("搜索中");
                    break;
                //todo onSearching 每搜索到一个文件更新UI
                case 1:
                    //todo String fileType = (String) msg.obj;
                    //todo long size = fileSizes.get(fileType);
                    tv_file_sizeAll.setText(PublicUtils.formatSize(totalSize));
                    break;
                //todo onSearchEnd 搜索结束更新UI
                case 2:
                    int searchLocationRom = msg.arg1;
                    switch (searchLocationRom) {
                        //todo 内置空间搜索结束后UI更新 (click为true,可再进行深度搜索)
                        case 0:
                            bt_file_search.setClickable(true);
                            bt_file_search.setText("深度搜索");
                            break;
                        //todo 外置空间搜索结束后UI更新(click为false,搜索完毕)
                        case 1:
                            bt_file_search.setClickable(false);
                            bt_file_search.setText("搜索完毕");
                            break;
                    }
                    int count = adapter.getCount();
                    for (int i = 0; i < count; i++) {
                        FileClassInfo info = adapter.getItem(i);
                        info.size = fileSizes.get(info.fileType);
                        info.loadingOver = true;
                    }
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onResume(){
        loadTheData();  //todo 加载数据
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.activity_file_manage);
        super.onCreate(savedInstanceState);
        setActionBarBack("文件管理");
        initView();		//todo 初始加载当前页面控件
        listenerView(); //todo 控件监听
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //todo 离开当前页面时，中止搜索
        fileSearchManager.setStopSearch(true);
        //todo 离开当前页面时，中止线程
        if (searchThread != null) {
            searchThread.interrupt();
            searchThread = null;
        }
        System.gc();
    }
    @Override
    public void initView(){
        tv_file_sizeAll=(TextView)findViewById(R.id.tv_file_sizeAll);
        bt_file_search=(Button)findViewById(R.id.bt_file_search);
        lv_file_listView=(ListView)findViewById(R.id.lv_file_listView);
    }
    @Override
    public void listenerView(){
        bt_file_search.setOnClickListener(this);
        lv_file_listView.setOnItemClickListener(this);
        //todo 注意：设置监听中会将clickable默认设置为true
        //todo 此处是：进入文件管理即开始搜索内置文件,所以默认"深度搜索"不可click
        bt_file_search.setClickable(false);
    }
    private void loadTheData() {
        adapter = new FileServiceAdapter(this);
        adapter.addDataToAdapter(new FileClassInfo("文本文件", FileSearchTypeEvent.TYPE_TXT));
        adapter.addDataToAdapter(new FileClassInfo("图像文件", FileSearchTypeEvent.TYPE_IMAGE));
        adapter.addDataToAdapter(new FileClassInfo("APK文件", FileSearchTypeEvent.TYPE_APK));
        adapter.addDataToAdapter(new FileClassInfo("视屏文件", FileSearchTypeEvent.TYPE_VIDEO));
        adapter.addDataToAdapter(new FileClassInfo("音屏文件", FileSearchTypeEvent.TYPE_AUDIO));
        adapter.addDataToAdapter(new FileClassInfo("压缩文件", FileSearchTypeEvent.TYPE_ZIP));
        adapter.addDataToAdapter(new FileClassInfo("其它文件", FileSearchTypeEvent.TYPE_OTHER));
        lv_file_listView.setAdapter(adapter);
        totalSize = 0;
        fileSearchManager = FileSearchManager.getInstance(true);
        fileSizes = fileSearchManager.getFileSizes();
        //todo 异步搜索内置文件
        asyncSearchInRomFile();
    }
    //todo 异步搜索内置文件
    private void asyncSearchInRomFile(){
        searchThread = new Thread(new Runnable(){
            @Override
            public void run(){
                fileSearchManager.setOnFileSearchListener(searchListener);
                fileSearchManager.startSearchFromInRom(FileManageActivity.this);
            }
        });
        searchThread.start();
    }
    //todo 异步搜索外置文件
    private void asyncSearchOutRomFile() {
        searchThread = new Thread(new Runnable() {
            @Override
            public void run() {
                fileSearchManager.setOnFileSearchListener(searchListener);
                fileSearchManager.startSearchFromOutRom(FileManageActivity.this);
            }
        });
        searchThread.start();
    }
    /** 搜索监听,回调接口 */
    private FileSearchManager.OnFileSearchListener searchListener = new FileSearchManager.OnFileSearchListener(){
        @Override
        public void onSearchStart(int searchLocationRom){
            //todo 线程通信
            mainHandler.sendEmptyMessage(0); //todo onSearchStart
        }
        @Override
        public void onSearching(String fileType,long totalSize){
            //todo 保存文件总大小(全局)
            FileManageActivity.this.totalSize = totalSize;
            //todo 线程通信
            Message message = mainHandler.obtainMessage();
            message.what = 1; //todo onSearching
            message.obj = fileType;
            mainHandler.sendMessage(message);
        }
        @Override
        public void onSearchEnd(boolean isExceptionEnd, int searchLocationRom) {
            //todo 线程通信
            Message message = mainHandler.obtainMessage();
            message.what = 2;
            message.arg1 = searchLocationRom;
            mainHandler.sendMessage(message); //todo onSearchEnd
        }
    };
    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.iv_actionbar_left_icon:
                finish();
                break;
            case R.id.bt_file_search:
                //todo 按下"深度搜索"后,click失效
                bt_file_search.setClickable(false);
                //todo 按下"深度搜索"后,更新Adapter上UI
                int count = adapter.getCount();
                for (int i = 0; i < count; i++) {
                    adapter.getItem(i).loadingOver = false;
                }
                adapter.notifyDataSetChanged();
                //todo 执行"深度搜索" - 异步搜索外置文件
                asyncSearchOutRomFile();
                break;
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent,View view,int position,long id){
        FileClassInfo classInfo = adapter.getItem(position);
        String fileType = classInfo.fileType;
        Intent intent = new Intent(this,FileManagerListActivity.class);
        intent.putExtra("fileType",fileType);
        startActivity(intent);
    }
}

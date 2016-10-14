package azsecuer.androidy.com.azsecuer.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.io.IOException;
import java.util.List;
import azsecuer.androidy.com.azsecuer.R;
import azsecuer.androidy.com.azsecuer.activity.adapter.TelTypeListAdapter;
import azsecuer.androidy.com.azsecuer.activity.base.BaseActionBarActivity;
import azsecuer.androidy.com.azsecuer.activity.db.AssetsDBManager;
import azsecuer.androidy.com.azsecuer.activity.db.DBManager;
import azsecuer.androidy.com.azsecuer.activity.entity.TelTypeInfo;

/**
 * Created by lenovo on 2016/8/9.
 */
public class TelTypeActivity extends BaseActionBarActivity implements AdapterView.OnItemClickListener{
    private ListView list_tel_type;
    private List<TelTypeInfo> datas;
    private TelTypeListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tel_type);
        initView();listenerView();
    }
    public void initDatas(){
        try {
            DBManager.createFile(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(DBManager.dbFileIsExists()){
            try {
                AssetsDBManager.copyDBFileToDB(this,"commonnum.db",DBManager.fileDB);
                Log.i("copyDBFileToDB","-----");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Log.i("NotcopyDBFileToDB","-----");
        }
    }
    @Override
    public void initView() {
        setActionBarBack("通讯大全");
        list_tel_type = (ListView)this.findViewById(R.id.lv_tel_type);
        adapter=new TelTypeListAdapter(this);
        list_tel_type.setAdapter(adapter);
        try {
            DBManager.createFile(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        initDatas();
        adapter.setDatas(DBManager.readTableClassList(this));
    }
    @Override
    public void listenerView(){
        list_tel_type.setOnItemClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_actionbar_left_icon:
                finish();
                break;
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        int idx = ((TelTypeInfo)adapter.getItem(position)).getIdx();
        bundle.putInt("idx",idx);
        startActivity(TelListActivity.class,bundle);
    }
}
